package io.github.template.values

import io.github.wickedev.graphql.types.PageInfo

data class PostConnect(
    val edges: List<PostEdge>,
    val pageInfo: PageInfo
)