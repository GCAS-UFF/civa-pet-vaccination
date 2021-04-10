package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.*
import java.time.LocalDate
import java.time.ZoneOffset.UTC

class VaccineApplicationTest : BehaviorSpec({
    given("a vaccine and a pet weight") {
        `when`("vaccine has not expired") {
            then("it should create an application") {
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock.mustBeValid() } returns vaccineMock

                shouldNotThrowAny {
                    VaccineApplication.from(vaccineMock, mockk())
                }

                verify(exactly = 1) { vaccineMock.mustBeValid() }
            }
        }
        `when`("vaccine has expired") {
            then("it should not create and application") {
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock.mustBeValid() } throws ExpiredVaccineException
                    .from(LocalDate.now(UTC).minusDays(2))

                shouldThrowExactly<ExpiredVaccineException> {
                    VaccineApplication.from(vaccineMock, mockk())
                }

                verify(exactly = 1) { vaccineMock.mustBeValid() }
            }
        }
    }
    given("a vaccine application") {
        `when`("toPair method is called") {
            then("it should return a pair of vaccine name and application") {
                val slot = slot<VaccineApplication>()
                val vaccineMock = mockk<Vaccine>()

                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { "Name test" to slot.captured }

                val application = application {
                    id = mockk()
                    vaccine = vaccineMock
                    petWeight = mockk()
                    createdOn = mockk()
                }

                val (name, applicationFromPair) = application.toPair()

                name shouldBe "Name test"
                applicationFromPair shouldBeSameInstanceAs application

                verify(exactly = 1) { vaccineMock pairNameWith application }
            }
        }
        `when`("a matching species is provided") {
            then("it must match") {
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock mustMatch Species.CANINE } returns vaccineMock

                shouldNotThrowAny {
                    application {
                        id = mockk()
                        vaccine = vaccineMock
                        petWeight = mockk()
                        createdOn = mockk()
                    } mustMatch Species.CANINE
                }

                verify(exactly = 1) { vaccineMock mustMatch Species.CANINE }
            }
        }
        `when`("a non matching species is provided") {
            then("it must not match") {
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock mustMatch Species.FELINE } throws SpeciesMismatchException
                    .from(Species.CANINE, setOf(Species.FELINE))

                shouldThrowExactly<SpeciesMismatchException> {
                    application {
                        id = mockk()
                        vaccine = vaccineMock
                        petWeight = mockk()
                        createdOn = mockk()
                    } mustMatch Species.FELINE
                }

                verify(exactly = 1) { vaccineMock mustMatch Species.FELINE }
            }
        }
    }
    given("a valid instance of application") {
        `when`("it accepts a vaccine application visitor") {
            then("visitor methods should be called in sequence") {
                val visitorMock = mockk<VaccineApplicationVisitor>()

                every { visitorMock seeId any() } just Runs
                every { visitorMock seeVaccine any() } just Runs
                every { visitorMock seePetWeight any() } just Runs
                every { visitorMock seeCreatedOn any() } just Runs

                shouldNotThrowAny {
                    application {
                        id = mockk()
                        vaccine = mockk()
                        petWeight = mockk()
                        createdOn = mockk()
                    } accepts visitorMock
                }

                verifySequence {
                    visitorMock seeId any()
                    visitorMock seeVaccine any()
                    visitorMock seePetWeight any()
                    visitorMock seeCreatedOn any()
                }
            }
        }
        `when`("it map status from another application") {
            then("it should map to application date time result") {
                forAll(
                    row(DateTimeStatus.VALID),
                    row(DateTimeStatus.BEFORE),
                    row(DateTimeStatus.SAME),
                    row(DateTimeStatus.INTERVAL)
                ) {
                    val applicationDateTimeMock = mockk<ApplicationDateTime>()
                    every { applicationDateTimeMock mapStatus any() } returns it

                    val application = application {
                        id = mockk()
                        vaccine = mockk()
                        petWeight = mockk()
                        createdOn = applicationDateTimeMock
                    }

                    application mapStatusFrom mockk() shouldBe it

                    verify(exactly = 1) { applicationDateTimeMock mapStatus any() }
                }
            }
        }
    }
})