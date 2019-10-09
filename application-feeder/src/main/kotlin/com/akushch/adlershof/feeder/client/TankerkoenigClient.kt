package com.akushch.adlershof.feeder.client

import com.akushch.adlershof.common.toIO
import com.akushch.adlershof.feeder.config.TankerkoenigProperties
import com.akushch.adlershof.feeder.model.AreaApi
import com.akushch.adlershof.feeder.model.TankerkoenigResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration
import javax.annotation.PostConstruct

@Component
class TankerkoenigClient(
    builder: WebClient.Builder,
    properties: TankerkoenigProperties
) {

    private val webClient = builder.configure(properties.api).build()
    private val apiKey = properties.api.apiKey

    fun getStationsInArea(area: AreaApi) =
        with(area) {
            webClient.get()
                .uri("/json/list.php?lat=${lat.value}&lng=${lon.value}&rad=$radius&type=all&apikey=$apiKey")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<TankerkoenigResponse>()
                .toIO()
        }

    private fun WebClient.Builder.configure(properties: TankerkoenigProperties.Api) = with(properties) {
        baseUrl(url)
            .filter { request, next ->
                next.exchange(request)
                    .timeout(timeout)
                    .retry(retryAttempts)
            }
    }
}