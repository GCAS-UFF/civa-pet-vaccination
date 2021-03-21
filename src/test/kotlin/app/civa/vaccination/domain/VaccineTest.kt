package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*

class VaccineTest : BehaviorSpec({
    given("a name, an efficacy and a fabrication") {
        `when`("Vaccine is built") {
            then("it should not throw any exception") {
                shouldNotThrowAny {
                    vaccine {
                        name = mockk()
                        efficacy = mockk()
                        fabrication = mockk()
                    }
                }
            }
        }
    }
    given("an expired Vaccine") {
        `when`("Vaccine is validated") {
            then("it should throw VaccineExpiredException") {
                val fabricationMock = mockk<Fabrication>()
                every { fabricationMock.mustBeValid() } throws
                        VaccineExpiredException("Vaccine has expired", "E", "A")

                val exception = shouldThrowExactly<VaccineExpiredException> {
                    vaccine {
                        name = mockk()
                        efficacy = mockk()
                        fabrication = fabricationMock
                    }.mustBeValid()
                }
                exception shouldHaveMessage "Vaccine has expired"
                exception.explain() shouldBe "Expected: E, Actual: A"
            }
        }
    }
    given("a valid Vaccine") {
        `when`("Vaccine is validated") {
            then("it should not throw any exception") {
                val fabricationMock = mockk<Fabrication>()
                every { fabricationMock.mustBeValid() } returns fabricationMock

                shouldNotThrowAny {
                    vaccine {
                        name = mockk()
                        efficacy = mockk()
                        fabrication = fabricationMock
                    }
                }
            }
        }
    }
    given("any species") {
        `when`("it matches vaccine's species") {
            then("it should not throw any exception") {
                val efficacyMock = mockk<Efficacy>()
                every { efficacyMock.mustMatch(eq(Species.CANINE)) } returns efficacyMock

                shouldNotThrowAny {
                    vaccine {
                        name = mockk()
                        efficacy = efficacyMock
                        fabrication = mockk()
                    } mustMatch Species.CANINE
                }
            }
        }
        `when`("it does not match vaccine's species") {
            then("it should throw VaccineExpiredException") {
                val efficacyMock = mockk<Efficacy>()
                every { efficacyMock.mustMatch(eq(Species.FELINE)) } throws
                        VaccineExpiredException("Vaccine has expired", "E", "A")

                val exception = shouldThrowExactly<VaccineExpiredException> {
                    vaccine {
                        name = mockk()
                        efficacy = efficacyMock
                        fabrication = mockk()
                    } mustMatch Species.FELINE
                }
                exception shouldHaveMessage "Vaccine has expired"
                exception.explain() shouldBe "Expected: E, Actual: A"
            }
        }
    }
    given("a valid instance of Vaccine") {
        `when`("it accepts a VaccineVisitor") {
            then("visitor methods should be called in sequence") {
                val visitorMock = mockk<VaccineVisitor>()
                every { visitorMock.seeName(any()) } just Runs
                every { visitorMock.seeEfficacy(any()) } just Runs
                every { visitorMock.seeFabrication(any()) } just Runs

                vaccine {
                    name = mockk()
                    efficacy = mockk()
                    fabrication = mockk()
                } accepts visitorMock

                verifySequence {
                    visitorMock.seeName(any())
                    visitorMock.seeEfficacy(any())
                    visitorMock.seeFabrication(any())
                }
            }
        }
    }
})
