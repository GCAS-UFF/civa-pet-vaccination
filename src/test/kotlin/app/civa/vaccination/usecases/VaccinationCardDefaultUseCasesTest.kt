package app.civa.vaccination.usecases

import app.civa.vaccination.domain.Species
import app.civa.vaccination.domain.VaccinationCard
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.util.*

class VaccinationCardDefaultUseCasesTest : WordSpec({
    lateinit var persistenceMock: VaccinationCardPersistence
    lateinit var useCases: VaccinationCardUseCases

    beforeEach {
        persistenceMock = mockk()
        useCases = VaccinationCardDefaultUseCases(persistenceMock)
    }

    "Create vaccination card use cases" When {
        "provided petId is not associated with an existing card" should {
            "return new vaccination card" {
                val petId = UUID.randomUUID()
                val species = Species.CANINE

                coEvery {
                    persistenceMock.createOne(eq(petId), any())
                } returns VaccinationCard.of(petId to species)

                val answer = useCases.createOne(petId, species)
                answer.shouldNotBeNull()
                    .shouldBeInstanceOf<String>()
                    .shouldNotBeEmpty()

                coVerify { persistenceMock.createOne(eq(petId), any()) }
            }
        }
        "provided petId is already associated with an existing card" should {
            "return null" {
                val petId = UUID.randomUUID()
                val species = Species.CANINE

                coEvery {
                    persistenceMock.createOne(eq(petId), any())
                } returns null

                val answer = useCases.createOne(petId, species)
                answer.shouldBeNull()

                coVerify { persistenceMock.createOne(eq(petId), any()) }
            }
        }
    }

    "Find vaccination card use cases" When {
        "provided cardId is present" should {
            "return found card" {
                val cardId = UUID.randomUUID()

                coEvery {
                    persistenceMock.findById(eq(cardId))
                } returns mockk()

                val answer = useCases.findById(cardId)
                answer.shouldNotBeNull()
                answer.shouldBeInstanceOf<VaccinationCard>()

                coVerify { persistenceMock.findById(eq(cardId)) }
            }
        }
        "provided cardId is empty" should {
            "return null" {
                coEvery {
                    persistenceMock.findById(any())
                } returns null

                val answer = useCases.findById(UUID.randomUUID())
                answer.shouldBeNull()

                coVerify { persistenceMock.findById(any()) }
            }
        }
    }
})
