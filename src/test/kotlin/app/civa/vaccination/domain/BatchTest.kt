package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class BatchTest : BehaviorSpec({
    given("valid inputs") {
        `when`("Batch is instantiated") {
            then("it should not throw any exceptions") {
                forAll(
                        row("000/00"),
                        row("086/21"),
                        row("999/99")
                ) { shouldNotThrowAny { Batch from it } }
            }
        }
    }

    given("invalid inputs") {
        `when`("Batch is instantiated") {
            then("it should throw InvalidBatchException") {
                forAll(
                        row("1231232"),
                        row("99/999"),
                        row("/"),
                        row("1234/12")
                ) {
                    shouldThrowExactly<InvalidBatchException> {
                        Batch from it
                    }
                }
            }
        }
    }

    given("a valid Batch instance") {
        `when`("property value is accessed") {
            then("it should return correct information") {
                (Batch from "002/21")
                        .shouldBeInstanceOf<Batch>()
                        .value shouldBe "002/21"
            }
        }
    }
})
