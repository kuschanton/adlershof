package com.akushch.adlershof.domain.subscription

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import arrow.effects.IO
import arrow.effects.extensions.io.fx.fx
import com.akushch.adlershof.domain.user.User
import java.util.UUID

interface ValidateSubscriptionCreateService {
    val existsByName: ExistsByName

    fun SubscriptionCreation.validate(user: User): IO<Either<SubscriptionCreateError, ValidSubscriptionCreation>> {
        return fx {
            if (existsByName(name, user).bind()) {
                SubscriptionCreateError.NameAlreadyExists.left()
            } else {
                ValidSubscriptionCreation(
                    id = UUID.randomUUID(),
                    area = area,
                    name = name
                ).right()
            }
        }
    }

}
