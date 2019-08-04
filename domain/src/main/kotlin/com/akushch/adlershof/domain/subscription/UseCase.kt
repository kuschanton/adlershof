package com.akushch.adlershof.domain.subscription

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.data.EitherT
import arrow.data.extensions.eithert.monad.binding
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import arrow.effects.extensions.io.monad.monad
import arrow.effects.fix
import com.akushch.adlershof.domain.user.User
import java.util.UUID

data class CreateSubscriptionCommand(val data: SubscriptionCreation, val user: User)
data class DeleteSubscriptionCommand(val data: SubscriptionDeletion, val user: User)

sealed class SubscriptionCreateError {
    object NameAlreadyExists: SubscriptionCreateError()
}

sealed class SubscriptionDeleteError {
    object SubscriptionNotFound: SubscriptionDeleteError()
}

interface CreateSubscriptionUseCase {
    val createSubscription: CreateSubscription
    val validateSubscriptionCreation: ValidateSubscriptionCreation

    fun CreateSubscriptionCommand.runUseCase(): IO<Either<SubscriptionCreateError, Subscription>> {
        return binding<ForIO, SubscriptionCreateError, Subscription>(IO.monad()) {
            val validSubscriptionCreation2 = EitherT(validateSubscriptionCreation(data, user)).bind()
            EitherT(createSubscription(validSubscriptionCreation2, user).map { it.right() }).bind()
        }.value().fix()
    }
}

interface DeleteSubscriptionUseCase {
    val deleteSubscription: DeleteSubscription
    val getById: GetById

    fun DeleteSubscriptionCommand.runUseCase(): IO<Either<SubscriptionDeleteError, UUID>> {
        return fx {
            getById(data.id, user).bind().fold(
                { SubscriptionDeleteError.SubscriptionNotFound.left() },
                { deleteSubscription(data.id).bind().right() }
            )
        }
    }
}