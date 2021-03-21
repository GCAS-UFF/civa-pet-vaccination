package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContainOnlyOnce
import io.kotest.matchers.string.shouldNotContain
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import java.util.*

class FabricationTest : BehaviorSpec({
    given("a company, a Batch and an ExpirationDate") {
        `when`("Fabrication is built") {
            and("company have whitespace") {
                then("it should remove the whitespace") {
                    val fabrication = fabrication {
                        company = "  Zoetis "
                        batch = mockk()
                        expirationDate = mockk()
                    }
                    fabrication.toString() shouldContainOnlyOnce "company='Zoetis'"
                    fabrication.toString() shouldNotContain "  Zoetis "
                }
            }
            and("all inputs are valid") {
                then("it should not throw any exceptions") {
                    shouldNotThrowAny {
                        fabrication {
                            company = UUID.randomUUID().toString()
                            batch = mockk()
                            expirationDate = mockk()
                        }
                    }
                }
            }
        }
    }
    given("an expiration date before now") {
        `when`("Fabrication is validated") {
            then("it should throw VaccineExpiredException") {
                val expirationDateMock = mockk<ExpirationDate>()
                every { expirationDateMock.mustBeValid() } throws
                        VaccineExpiredException("Test expiration", "E", "A")

                val exception = shouldThrowExactly<VaccineExpiredException> {
                    fabrication {
                        company = UUID.randomUUID().toString()
                        batch = mockk()
                        expirationDate = expirationDateMock
                    }.mustBeValid()
                }
                exception shouldHaveMessage "Test expiration"
                exception.explain() shouldBe "Expected: E, Actual: A"
            }
        }
    }
    given("an expiration date after now") {
        `when`("Fabrication is validated") {
            then("it should not throw any exception") {
                val expirationDateMock = mockk<ExpirationDate>()
                every { expirationDateMock.mustBeValid() } returns expirationDateMock

                shouldNotThrowAny {
                    fabrication {
                        company = UUID.randomUUID().toString()
                        batch = mockk()
                        expirationDate = expirationDateMock
                    }.mustBeValid()
                }
            }
        }
    }
    given("a valid instance of Fabrication") {
        `when`("it accepts a FabricationVisitor") {
            then("visitor methods should be called in sequence") {
                val visitorMock = mockk<FabricationVisitor>()
                every { visitorMock.seeCompany(any()) } just Runs
                every { visitorMock.seeBatch(any()) } just Runs
                every { visitorMock.seeExpirationDate(any()) } just Runs

                fabrication {
                    company = UUID.randomUUID().toString()
                    batch = mockk()
                    expirationDate = mockk()
                } accepts visitorMock

                verifySequence {
                    visitorMock.seeCompany(any())
                    visitorMock.seeBatch(any())
                    visitorMock.seeExpirationDate(any())
                }
            }
        }
    }
})
