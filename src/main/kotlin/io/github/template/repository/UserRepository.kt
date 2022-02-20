package io.github.template.repository

import io.github.template.entity.User
import io.github.wickedev.graphql.spring.data.r2dbc.repository.interfaces.GraphQLR2dbcRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
interface UserRepository : GraphQLR2dbcRepository<User> {
    fun existsByEmail(email: String): Mono<Boolean>

    fun findByEmail(email: String): Mono<User?>
}