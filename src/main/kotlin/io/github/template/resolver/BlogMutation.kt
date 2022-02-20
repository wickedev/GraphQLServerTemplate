package io.github.template.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.server.operations.Mutation
import io.github.template.entity.Post
import io.github.template.repository.PostRepository
import io.github.template.repository.UserRepository
import io.github.template.utils.get
import io.github.template.values.PostEdge
import io.github.template.values.UploadedPost
import io.github.wickedev.coroutine.reactive.extensions.mono.await
import io.github.wickedev.graphql.Auth
import io.github.wickedev.graphql.types.ID
import io.github.wickedev.spring.reactive.security.jwt.JwtAuthenticationToken
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BlogMutation(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
) : Mutation {

    @Auth
    fun postUpload(
        uploadedPost: UploadedPost,
        @GraphQLIgnore authentication: Authentication
    ): Mono<UploadPost?> = mono {
        val user = userRepository.findByEmail(authentication.name).await()
        val post = postRepository.save(
            Post(
                title = uploadedPost.title,
                content = uploadedPost.content,
                published = true,
                authorId = user?.id
            )
        ).await()
        post?.let { UploadPost(PostEdge(it, it.id)) }
    }

    @Auth(require = "@postResource.ownershipFor(#id, #authentication)")
    fun postDelete(id: ID, @GraphQLIgnore authentication: Authentication): Mono<ID> = mono {
        postRepository.deleteById(id).await()
        id
    }
}

data class UploadPost(
    val post: PostEdge
)

@Component
class PostResource(val postRepository: PostRepository) {
    fun ownershipFor(id: ID, authentication: JwtAuthenticationToken): Boolean {
        val post = postRepository.findById(id).get()
        return post.authorId?.encoded == authentication.getId()
    }
}
