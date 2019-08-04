package com.akushch.adlershof.domain.subscription

import arrow.core.Either
import arrow.core.Option
import arrow.effects.IO
import com.akushch.adlershof.domain.user.User
import java.util.UUID

typealias CreateSubscription = (ValidSubscriptionCreation, User) -> IO<Subscription>
typealias ValidateSubscriptionCreation = (SubscriptionCreation, User) -> IO<Either<SubscriptionCreateError, ValidSubscriptionCreation>>
typealias ExistsByName = (String, User) -> IO<Boolean>
typealias GetById = (UUID, User) -> IO<Option<Subscription>>
typealias DeleteSubscription = (UUID) -> IO<UUID>