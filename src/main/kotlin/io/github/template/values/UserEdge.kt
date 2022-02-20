package io.github.template.values

import io.github.template.entity.User
import io.github.wickedev.graphql.types.ID

data class UserEdge(
    val node: User,
    val cursor: ID
)