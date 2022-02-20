package io.github.template.values

import io.github.template.entity.Post
import io.github.wickedev.graphql.types.ID

data class PostEdge(
    val node: Post,
    val cursor: ID
)
