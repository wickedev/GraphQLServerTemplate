package io.github.template.interfaces

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import io.github.wickedev.spring.reactive.security.IdentifiableUserDetails

@GraphQLIgnore
interface SimpleUserDetails : IdentifiableUserDetails {
    @GraphQLIgnore
    override fun isAccountNonExpired(): Boolean = true

    @GraphQLIgnore
    override fun isAccountNonLocked(): Boolean = true

    @GraphQLIgnore
    override fun isCredentialsNonExpired(): Boolean = true

    @GraphQLIgnore
    override fun isEnabled(): Boolean = true
}
