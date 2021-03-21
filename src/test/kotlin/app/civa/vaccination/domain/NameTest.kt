package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.*

class NameTest : BehaviorSpec({
    given("a commercial name and a classification name") {
        `when`("Name is built") {
            then("it should not throw any exception") {
                shouldNotThrowAny {
                    name {
                        classification = "Antirrábica"
                        commercial = "Nobivac Raiva"
                    }
                }
            }
        }
        `when`("any property has whitespace") {
            then("it should remove the whitespace") {
                name {
                    classification = " Antirrábica       "
                    commercial = "    Nobivac Raiva"
                }.toString() shouldBe
                        "Name(classification='Antirrábica', commercial='Nobivac Raiva')"
            }
        }
        `when`("any property is empty") {
            then("it should throw InvalidNameException") {
                forAll(
                    row("", ""),
                    row("", "Nobivac Raiva"),
                    row("Antirrábica", "")
                ) { a, b ->
                    val exception = shouldThrowExactly<InvalidNameException> {
                        name { classification = a; commercial = b }
                    }
                    exception shouldHaveMessage "Name must not be blank"
                    exception.explain() shouldBe "Expected: Any string, Actual: \"\""
                }
            }
        }
    }
    given("a vaccine application") {
        `when`("its paired with Name") {
            then("it should return a pair of classification name and application") {
                val applicationMock = mockk<VaccineApplication>()

                val pair = name {
                    classification = "Antirrábica"
                    commercial = "Nobivac Raiva"
                } pairWith applicationMock

                pair.first shouldBe "Antirrábica"
                pair.second shouldBeSameInstanceAs applicationMock
            }
        }
    }
    given("a valid instance of Name") {
        `when`("it accepts a name visitor") {
            then("visitor methods should be called in sequence") {
                val visitorMock = mockk<NameVisitor>()
                every { visitorMock.seeClassification(any()) } just Runs
                every { visitorMock.seeCommercial(any()) } just Runs

                name {
                    classification = "Antirrábica"
                    commercial = "Nobivac Raiva"
                } accepts visitorMock

                verifySequence {
                    visitorMock.seeClassification(any())
                    visitorMock.seeCommercial(any())
                }
            }
        }
    }
})
