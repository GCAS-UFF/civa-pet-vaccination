package app.civa.vaccination.domain

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage

class DomainExceptionTest : ExpectSpec({
    context("an exception is instantiated") {
        val exception = object : DomainException(
            message = "Domain Exception Test Message",
            expected = "Expected value",
            actual = "Actual Value"
        ) {}

        expect("message to be correct") {
            exception shouldHaveMessage "Domain Exception Test Message"
        }

        expect("exception to explain itself") {
            exception.explain() shouldBe "Expected: Expected value, Actual: Actual Value"
        }
    }
})
