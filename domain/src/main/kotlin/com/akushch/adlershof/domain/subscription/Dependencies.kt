package com.akushch.adlershof.domain.subscription

import arrow.core.Either
import arrow.effects.IO
import com.akushch.adlershof.domain.user.User

typealias CreateSubscription = (ValidSubscriptionCreation, User) -> IO<Subscription>
typealias ValidateSubscriptionCreation = (SubscriptionCreation, User) -> IO<Either<SubscriptionCreateError, ValidSubscriptionCreation>>
typealias ExistsByName = (String, User) -> IO<Boolean>