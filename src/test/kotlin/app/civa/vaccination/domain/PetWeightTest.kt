package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf

class PetWeightTest : BehaviorSpec({
    given("positive numbers or zero as inputs") {
        `when`("PetWeight is instantiated") {
            then("it should not throw any exceptions") {
                forAll(
                    row(0.56),
                    row(2.56),
                    row(5.5),
                    row(8.7123189),
                    row(10.051)
                ) {
                    shouldNotThrowAny {
                        PetWeight from it
                    }
                }
            }
        }
    }

    given("negative numbers or zero as inputs") {
        `when`("PetWeight is instantiated") {
            then("it should throw InvalidPetWeightException") {
                forAll(
                    row(0.0),
                    row(-2.56),
                    row(-9995.5),
                ) {
                    val exception = shouldThrowExactly<InvalidPetWeightException> {
                        PetWeight from it
                    }
                    exception shouldHaveMessage "Pet weight must be positive: 0 (ZERO) or greater"
                    exception.explain() shouldBe "Expected: $it to be bigger than 0.0, Actual: $it"
                }
            }
        }
    }

    given("a value that does not need rounding") {
        `when`("property value is accessed") {
            then("it should return correct information") {
                (PetWeight from 5.78)
                    .shouldBeInstanceOf<PetWeight>()
                    .value shouldBe 5.78
            }
        }
    }

    given("a value that needs rounding") {
        `when`("value is closer to ceil") {
            then("it should round up") {
                (PetWeight from 2.678)
                    .shouldBeInstanceOf<PetWeight>()
                    .value shouldBe 2.68
            }
        }
        `when`("value is closer to floor") {
            then("it should round down") {
                (PetWeight from 2.674)
                    .shouldBeInstanceOf<PetWeight>()
                    .value shouldBe 2.67
            }
        }
    }
})
