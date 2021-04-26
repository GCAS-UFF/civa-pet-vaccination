package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldNotContain
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.assertDoesNotThrow
import java.util.*

class ApplicationsTest : BehaviorSpec({
    given("a vaccine application to be added") {
        `when`("it is the first application of that name") {
            then("it should be added successfully") {
                val slot = slot<VaccineApplication>()
                val vaccineMock = mockk<Vaccine>()
                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { "Name Test" to slot.captured }

                val applications = Applications()

                shouldNotThrowAny {
                    applications add application {
                        id = mockk()
                        vaccine = vaccineMock
                        createdOn = mockk()
                        petWeight = mockk()
                    }
                }

                applications.shouldNotBeNull().shouldNotBeEmpty()
                applications.size shouldBe 1
                applications shouldContain ("Name Test" to listOf(slot.captured))

                verify(exactly = 1) { vaccineMock pairNameWith capture(slot) }
            }
        }
        `when`("it is not the first application of that name") {
            and("it has happened after interval") {
                then("it should be added to applications successfully") {
                    val slot = slot<VaccineApplication>()
                    val vaccineMock = mockk<Vaccine>()
                    every {
                        vaccineMock pairNameWith capture(slot)
                    } answers { "Name Test" to slot.captured }

                    val vaccineApplicationMock = mockk<VaccineApplication>()
                    every {
                        vaccineApplicationMock mapStatusFrom any()
                    } returns DateTimeStatus.VALID
                    every {
                        vaccineApplicationMock.toPair()
                    } answers { "Name Test" to vaccineApplicationMock }

                    val applications = Applications()
                    applications add vaccineApplicationMock

                    shouldNotThrowAny {
                        applications add application {
                            id = mockk()
                            vaccine = vaccineMock
                            createdOn = mockk()
                            petWeight = mockk()
                        }
                    }

                    applications.shouldNotBeNull().shouldNotBeEmpty()
                    applications.size shouldBe 1
                    applications shouldContain ("Name Test" to listOf(
                        vaccineApplicationMock,
                        slot.captured
                    ))

                    verify {
                        vaccineApplicationMock.toPair()
                        vaccineMock pairNameWith capture(slot)
                        vaccineApplicationMock mapStatusFrom any()
                    }
                }
            }
            and("it is not valid") {
                then("it should not be added") {
                    forAll(
                        row(DateTimeStatus.BEFORE),
                        row(DateTimeStatus.SAME),
                        row(DateTimeStatus.INTERVAL)
                    ) {
                        val slot = slot<VaccineApplication>()
                        val vaccineMock = mockk<Vaccine>()
                        every {
                            vaccineMock pairNameWith capture(slot)
                        } answers { "Name Test" to slot.captured }

                        val vaccineApplicationMock = mockk<VaccineApplication>()
                        every { vaccineApplicationMock mapStatusFrom any() } returns it
                        every {
                            vaccineApplicationMock.toPair()
                        } answers { "Name Test" to vaccineApplicationMock }

                        val applications = Applications()
                        applications add vaccineApplicationMock

                        shouldThrowExactly<InvalidApplicationException> {
                            applications add application {
                                id = mockk()
                                vaccine = vaccineMock
                                createdOn = mockk()
                                petWeight = mockk()
                            }
                        }
                        applications.shouldNotBeNull().shouldNotBeEmpty()
                        applications.size shouldBe 1
                        applications shouldNotContain ("Name Test" to listOf(
                            vaccineApplicationMock,
                            slot.captured
                        ))

                        verify {
                            vaccineApplicationMock.toPair()
                            vaccineMock pairNameWith capture(slot)
                            vaccineApplicationMock mapStatusFrom any()
                        }
                    }
                }
            }
        }
    }
    given("an application id to be removed") {
        `when`("the application is found") {
            and("there's only one application with certain name") {
                then("it should be removed successfully") {
                    val slots = mutableListOf<VaccineApplication>()
                    val vaccineMock = mockk<Vaccine>()
                    every {
                        vaccineMock pairNameWith capture(slots)
                    } answers { "Name Test" to slots.last() }

                    val uuid = UUID.randomUUID()
                    val vaccineApplication = application {
                        id = uuid
                        vaccine = vaccineMock
                        createdOn = mockk()
                        petWeight = mockk()
                    }

                    val applications = Applications()
                    applications add vaccineApplication

                    assertDoesNotThrow { applications deleteBy uuid }

                    applications.shouldNotBeNull().shouldBeEmpty()
                    applications shouldNotContain ("Name Test" to listOf(vaccineApplication))

                    verify { vaccineMock pairNameWith ofType<VaccineApplication>() }
                }
            }
            and("there's more than one application with the same name") {
                then("it should be removed successfully") {
                    val slots = mutableListOf<VaccineApplication>()
                    val vaccineMock = mockk<Vaccine>()
                    every {
                        vaccineMock pairNameWith capture(slots)
                    } answers { "Name Test" to slots.last() }

                    val createdOnMock = mockk<ApplicationDateTime>()
                    every { createdOnMock mapStatus any() } returns DateTimeStatus.VALID

                    val uuid = UUID.randomUUID()
                    val vaccineApplication = application {
                        id = uuid
                        vaccine = vaccineMock
                        createdOn = createdOnMock
                        petWeight = mockk()
                    }

                    val applications = Applications()
                    applications add vaccineApplication
                    applications add application {
                        id = UUID.randomUUID()
                        vaccine = vaccineMock
                        createdOn = mockk()
                        petWeight = mockk()
                    }

                    assertDoesNotThrow { applications deleteBy uuid }

                    applications.shouldNotBeNull().shouldNotBeEmpty()
                    applications shouldNotContain ("Name Test" to listOf(vaccineApplication))

                    verify { vaccineMock pairNameWith ofType<VaccineApplication>() }
                }
            }
        }
        `when`("applications is empty") {
            then("there won't be any to remove") {
                val applications = Applications()

                shouldThrowExactly<ApplicationNotFoundException> {
                    applications deleteBy UUID.randomUUID()
                }
                applications.shouldNotBeNull().shouldBeEmpty()
            }
        }
    }
    given("an application id to be found") {
        `when`("the application is present") {
            then("it must be retrieved") {
                val slots = mutableListOf<VaccineApplication>()
                val vaccineMock = mockk<Vaccine>()
                every {
                    vaccineMock pairNameWith capture(slots)
                } answers { "Name Test" to slots.last() }

                val uuid = UUID.randomUUID()
                val vaccineApplication = application {
                    id = uuid
                    vaccine = vaccineMock
                    createdOn = mockk()
                    petWeight = mockk()
                }

                val applications = Applications()
                applications add vaccineApplication

                shouldNotThrowAny {
                    val foundApplication = applications findBy uuid
                    foundApplication shouldBeSameInstanceAs vaccineApplication
                }
            }
        }
        `when`("the application is not found") {
            then("it should not be retrieved") {
                val applications = Applications()
                shouldThrowExactly<ApplicationNotFoundException> {
                    applications findBy UUID.randomUUID()
                }
            }
        }
    }
})
