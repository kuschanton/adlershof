package com.akushch.adlershof.domain.subscription

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.data.EitherT
import arrow.data.extensions.eithert.monad.binding
import arrow.effects.ForIO
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import arrow.effects.extensions.io.monad.binding
import arrow.effects.extensions.io.monad.monad
import arrow.effects.fix
import com.akushch.adlershof.domain.user.User

data class CreateSubscriptionCommand(val data: SubscriptionCreation, val user: User)

sealed class SubscriptionCreateError {
    object NameAlreadyExists: SubscriptionCreateError()
}

interface CreateSubscriptionUseCase {
    val createSubscription: CreateSubscription
    val validateSubscriptionCreation: ValidateSubscriptionCreation

    fun CreateSubscriptionCommand.runUseCase(): IO<Either<SubscriptionCreateError, Subscription>> {
        val res = binding {
            val (validSubscriptionCreation) = validateSubscriptionCreation(data, user)
            val vsc = validSubscriptionCreation.fold(
                { it.left() },
                { createSubscription(it, user).bind().right() }
            )
            vsc
        }

        // Equals to
        val asd = binding<ForIO, SubscriptionCreateError, Subscription>(IO.monad()) {
            val validSubscriptionCreation2 = EitherT(validateSubscriptionCreation(data, user)).bind()
            EitherT(createSubscription(validSubscriptionCreation2, user).map { it.right() }).bind()
        }.value().fix()

        // Equals to
        val foo = fx {
            val (validSubscriptionCreation) = validateSubscriptionCreation(data, user)
            val vsc = validSubscriptionCreation.fold(
                { it.left() },
                { createSubscription(it, user).bind().right() }
            )
            vsc
        }

        return res
    }

}