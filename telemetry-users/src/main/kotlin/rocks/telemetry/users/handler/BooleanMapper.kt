package rocks.telemetry.users.handler

import rocks.telemetry.users.api.ApiVerifiedDto

fun Boolean.asDto() = ApiVerifiedDto(this)
