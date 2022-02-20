package io.github.template.entity

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import io.github.template.repository.UserRepository
import io.github.wickedev.graphql.annotations.Relation
import io.github.wickedev.graphql.interfases.Node
import io.github.wickedev.graphql.types.ID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Table
data class Post(
    @Id
    override val id: ID = ID.Empty,

    val title: String,
    val content: String?,
    val published: Boolean,
    val postedAt: LocalDateTime = LocalDateTime.now(),
    val deletedAt: LocalDateTime? = null,

    @Relation(User::class) val authorId: ID? = null,
) : Node {

    val isAnonymous: Boolean
        get() = authorId == null

    fun author(
        @GraphQLIgnore @Autowired userRepository: UserRepository,
        env: DataFetchingEnvironment
    ): CompletableFuture<User?>? {
        return authorId?.let { userRepository.findById(authorId, env) }
    }
}
