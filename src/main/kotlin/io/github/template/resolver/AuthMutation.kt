package io.github.template.resolver

import com.expediagroup.graphql.generator.annotations.GraphQLUnion
import com.expediagroup.graphql.server.operations.Mutation
import graphql.schema.DataFetchingEnvironment
import io.github.template.entity.User
import io.github.template.exception.InvalidEmailOrPasswordError
import io.github.template.exception.UserAlreadyExistError
import io.github.template.repository.UserRepository
import io.github.template.utils.getRandomString
import io.github.template.values.AuthResponseWithID
import io.github.template.values.UnionResult
import io.github.wickedev.coroutine.reactive.extensions.mono.await
import io.github.wickedev.graphql.exceptions.ApolloError
import io.github.wickedev.spring.reactive.security.jwt.ReactiveJwtAuthenticationService
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AuthMutation(
    private val jwtAuthenticationService: ReactiveJwtAuthenticationService,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val i18n: MessageSourceAccessor
) : Mutation {

    @GraphQLUnion(
        name = "SignUpResponse",
        possibleTypes = [User::class, UserAlreadyExistError::class],
    )
    suspend fun signUp(email: String, password: String, name: String?, env: DataFetchingEnvironment): UnionResult {
        val exists = userRepository.existsByEmail(email).await()

        if (exists) {
            val message = i18n.getMessage("user_already_exist_exception")
            return UserAlreadyExistError(message)
        }

        return userRepository.save(
            User(
                email = email,
                name = name ?: getRandomString(8),
                hashSalt = passwordEncoder.encode(password),
                roles = listOf()
            )
        ).await()
    }

    @GraphQLUnion(
        name = "LoginResponse",
        possibleTypes = [AuthResponseWithID::class, InvalidEmailOrPasswordError::class],
    )
    suspend fun login(email: String, password: String, env: DataFetchingEnvironment): UnionResult {
        val auth = jwtAuthenticationService.signIn(email, password).await()
        if (auth == null) {
            val message = i18n.getMessage("invalid_email_or_password")
            return InvalidEmailOrPasswordError(message)
        }
        val user = userRepository.findByEmail(email).await()
        return AuthResponseWithID(
            user?.id?.encoded ?: "",
            auth.accessToken,
            auth.tokenType,
            auth.expiresIn,
            auth.scope,
            auth.refreshToken,
            auth.refreshExpiresIn
        )
    }

    suspend fun refresh(token: String, env: DataFetchingEnvironment): AuthResponseWithID {
        val path = env.executionStepInfo.path
        val sourceLocation = env.field.sourceLocation

        val auth = jwtAuthenticationService.refresh(token).await()
            ?: throw ApolloError.AuthenticationError("invalid refresh token", path, sourceLocation)

        val user = userRepository.findByEmail(auth.user.username).await()

        return AuthResponseWithID(
            user?.id?.encoded ?: "",
            auth.accessToken,
            auth.tokenType,
            auth.expiresIn,
            auth.scope,
            auth.refreshToken,
            auth.refreshExpiresIn
        )
    }
}
