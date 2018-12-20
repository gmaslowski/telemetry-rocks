package rocks.telemetry.users.handler

import rocks.telemetry.users.api.UserDto
import rocks.telemetry.users.api.UserWithPasswordDto
import rocks.telemetry.users.entity.User

fun User.asDto() = UserDto(id!!, uuid!!, firstName!!, lastName!!)

fun User.asUserWithPasswordDto() = UserWithPasswordDto(id!!, email!!, firstName!!, lastName!!, passwordHash!!, passwordSalt!!)
