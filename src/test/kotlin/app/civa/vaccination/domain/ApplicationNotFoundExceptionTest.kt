package app.civa.vaccination.domain

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.util.*

class ApplicationNotFoundExceptionTest : ExpectSpec({
    context("exception is instantiated") {
        val uuid = UUID.randomUUID().toString()

        val exception = ApplicationNotFoundException(
            message = "Vaccine Application not found",
            expected = uuid,
            actual = "null"
        )

        expect("message to be correct") {
            exception shouldHaveMessage "Vaccine Application not found"
        }

        expect("exception to explain itself") {
            exception.explain() shouldBe "Expected: $uuid, Actual: null"
        }
    }
})
