package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.*
import java.util.*

class VaccinationCardTest : BehaviorSpec({
    given("a pair of petId and species") {
        then("it should be able to create a new vaccination card") {
            shouldNotThrowAny {
                val card = VaccinationCard of (UUID.randomUUID() to Species.CANINE)
                card.shouldNotBeNull()
                card.shouldBeInstanceOf<VaccinationCard>()
            }
        }
    }
    given("an empty vaccination card") {
        `when`("a valid application is added") {
            then("it should be added successfully") {
                val vaccineApplicationMock = mockk<VaccineApplication>()
                every {
                    vaccineApplicationMock mustMatch Species.CANINE
                } returns vaccineApplicationMock

                val applicationsMock = mockk<Applications>()
                every { applicationsMock add vaccineApplicationMock } just Runs

                val card = vaccinationCard {
                    id = UUID.randomUUID()
                    petId = UUID.randomUUID()
                    species = Species.CANINE
                    applications = applicationsMock
                }

                shouldNotThrowAny { card add vaccineApplicationMock }

                verify(exactly = 1) {
                    vaccineApplicationMock mustMatch Species.CANINE
                    applicationsMock add vaccineApplicationMock
                }
            }
        }
        `when`("an application is removed") {
            then("it should not be removed successfully") {
                val applicationsMock = mockk<Applications>()
                every {
                    applicationsMock deleteBy any()
                } throws ApplicationNotFoundException("Test Message", "Expected", "Actual")

                shouldThrowExactly<ApplicationNotFoundException> {
                    vaccinationCard {
                        id = UUID.randomUUID()
                        petId = UUID.randomUUID()
                        species = Species.CANINE
                        applications = applicationsMock
                    } deleteBy UUID.randomUUID()
                }

                verify(exactly = 1) { applicationsMock deleteBy any() }
            }
        }
        `when`("an application is searched for") {
            then("it should not be found") {
                val applicationsMock = mockk<Applications>()
                every {
                    applicationsMock findBy any()
                } throws ApplicationNotFoundException("Test Message", "Expected", "Actual")

                shouldThrowExactly<ApplicationNotFoundException> {
                    vaccinationCard {
                        id = UUID.randomUUID()
                        petId = UUID.randomUUID()
                        species = Species.CANINE
                        applications = applicationsMock
                    } findBy UUID.randomUUID()
                }

                verify(exactly = 1) { applicationsMock findBy any() }
            }
        }
    }
    given("a valid vaccination card instance") {
        `when`("it accepts a visitor") {
            then("visitor methods should be called sequentially") {
                val visitorMock = mockk<VaccinationCardVisitor>()
                every { visitorMock.seeId(any()) } just Runs
                every { visitorMock.seePetId(any()) } just Runs
                every { visitorMock.seeSpecies(any()) } just Runs
                every { visitorMock.seeApplications(any()) } just Runs

                val card = VaccinationCard of (UUID.randomUUID() to Species.CANINE)
                shouldNotThrowAny { card accepts visitorMock }

                verifySequence {
                    visitorMock.seeId(any())
                    visitorMock.seePetId(any())
                    visitorMock.seeSpecies(any())
                    visitorMock.seeApplications(any())
                }
            }
        }
    }
})
