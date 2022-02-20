package io.github.template

import io.github.template.entity.User
import io.github.template.repository.UserRepository
import io.github.template.utils.fixture
import io.github.wickedev.coroutine.reactive.extensions.mono.await
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserRepositoryTest(val userRepository: UserRepository) : DescribeSpec({

    lateinit var saved: User

    beforeSpec {
         saved = userRepository.save(fixture<User>()).await()
    }

    describe("user repository") {
        it("should be return user entity") {
            val user = userRepository.findById(saved.id).await()

            user shouldNotBe null
            user shouldBe saved
        }
    }
})