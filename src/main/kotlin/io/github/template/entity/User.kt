package io.github.template.entity

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import graphql.schema.DataFetchingEnvironment
import io.github.template.interfaces.SimpleUserDetails
import io.github.template.repository.PostRepository
import io.github.template.values.PostConnect
import io.github.template.values.PostEdge
import io.github.wickedev.graphql.interfases.Node
import io.github.wickedev.graphql.types.Backward
import io.github.wickedev.graphql.types.ID
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Table("users")
data class User(
    @Id override val id: ID = ID.Empty,
    @GraphQLIgnore val hashSalt: String,

    val email: String,
    val name: String?,
    val roles: List<String>,
    val deletedAt: LocalDateTime? = null,
) : Node, SimpleUserDetails {

    @GraphQLIgnore
    override fun getIdentifier(): String = id.encoded

    @GraphQLIgnore
    override fun getUsername(): String = email

    @GraphQLIgnore
    override fun getPassword(): String = hashSalt

    @GraphQLIgnore
    override fun getAuthorities(): Collection<GrantedAuthority> = roles.map { SimpleGrantedAuthority(it) }

    fun posts(
        last: Int?,
        before: ID?,
        @GraphQLIgnore @Autowired postRepository: PostRepository,
        env: DataFetchingEnvironment
    ): CompletableFuture<PostConnect> {
        return postRepository.connectionByAuthorId(id, Backward(last, before), env)
            .thenApply { PostConnect(it.edges.map { e -> PostEdge(e.node, e.cursor) }, it.pageInfo) }
    }
}
