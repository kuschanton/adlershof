package io.r2dbc.postgresql

import io.r2dbc.postgresql.authentication.AuthenticationHandler
import io.r2dbc.postgresql.authentication.PasswordAuthenticationHandler
import io.r2dbc.postgresql.authentication.SASLAuthenticationHandler
import io.r2dbc.postgresql.client.Client
import io.r2dbc.postgresql.client.ReactorNettyClient
import io.r2dbc.postgresql.client.StartupMessageFlow
import io.r2dbc.postgresql.codec.DefaultCodecsOverride
import io.r2dbc.postgresql.codec.PGPointCodec
import io.r2dbc.postgresql.message.backend.AuthenticationMessage
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.ConnectionFactoryMetadata
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

class PostgresqlConnectionFactoryOverride(
    private val configuration: PostgresqlConnectionConfiguration
) : ConnectionFactory {

    private val clientFactory: Mono<out Client>

    init {
        clientFactory = Mono.defer {
            ReactorNettyClient.connect(configuration.host, configuration.port, configuration.connectTimeout)
                .cast(Client::class.java)
        }
    }

    override fun getMetadata(): ConnectionFactoryMetadata = PostgresqlConnectionFactoryMetadata.INSTANCE

    override fun create(): Publisher<out Connection> =
        clientFactory
            .delayUntil { client ->
                StartupMessageFlow
                    .exchange(
                        configuration.applicationName,
                        ::getAuthenticationHandler,
                        client,
                        configuration.database,
                        configuration.username
                    )
                    .handle(PostgresqlServerErrorException::handleErrorResponse)
            }
            .map { client ->
                PostgresqlConnection(
                    client,
                    DefaultCodecsOverride(client.byteBufAllocator, listOf(PGPointCodec(client.byteBufAllocator))),
                    DefaultPortalNameSupplier.INSTANCE,
                    IndefiniteStatementCache(client)
                )
            }
            .delayUntil(::setSchema)

    override fun toString(): String {
        return "PostgresqlConnectionFactory{" +
                "clientFactory=$clientFactory" +
                ", configuration=$configuration" +
                '}'.toString()
    }

    private fun getAuthenticationHandler(message: AuthenticationMessage): AuthenticationHandler = when {
        PasswordAuthenticationHandler.supports(message) -> PasswordAuthenticationHandler(
            this.configuration.password,
            this.configuration.username
        )
        SASLAuthenticationHandler.supports(message) -> SASLAuthenticationHandler(
            this.configuration.password,
            this.configuration.username
        )
        else -> throw IllegalStateException("Unable to provide AuthenticationHandler capable of handling $message")
    }

    private fun setSchema(connection: PostgresqlConnection): Mono<Void> =
        if (configuration.schema == null) {
            Mono.empty()
        } else connection
            .createStatement(String.format("SET SCHEMA '%s'", this.configuration.schema))
            .execute()
            .then()
}