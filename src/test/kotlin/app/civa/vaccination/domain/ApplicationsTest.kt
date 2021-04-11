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
                    every { vaccineApplicationMock mapStatusFrom any() } returns DateTimeStatus.VALID
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
                    applications shouldContain ("Name Test" to listOf(vaccineApplicationMock, slot.captured))

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
                        applications shouldNotContain ("Name Test" to listOf(vaccineApplicationMock, slot.captured))

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
        `when`("the id is found") {
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
                applications.size shouldBe 0
                applications shouldNotContain ("Name Test" to listOf(vaccineApplication))

                verify { vaccineMock pairNameWith any() }
            }
        }
    }
})

//    inner class Add {
//
//        @Test
//        @DisplayName("successfully when vaccine name is different")
//        fun shouldAddApplicationWhenNameIsDifferent() {
//            val applications = Applications()
//            val anotherApplication = msdVaccine.apply(petWeight)
//
//            assertThatCode {
//                applications add application
//                applications add anotherApplication
//            }.doesNotThrowAnyException()
//
//            assertThat(applications)
//                .isNotNull
//                .isNotEmpty
//                .size().isEqualTo(2)
//
//            assertThat(applications["Antirrábica"])
//                .isNotNull
//                .isNotEmpty
//                .contains(anotherApplication)
//                .size().isEqualTo(1)
//
//            assertThat(applications["Múltipla V10"])
//                .isNotNull
//                .isNotEmpty
//                .contains(application)
//                .size().isEqualTo(1)
//        }
//
//
//    @Nested
//    @DisplayName("Remove entry")
//    inner class Remove {
//
//        @Test
//        @DisplayName("throw NoSuchElementException when its not found")
//        fun shouldNotRemoveApplicationById() {
//            val applications = Applications()
//
//            assertThat(applications)
//                .isNotNull
//                .isEmpty()
//
//            assertThatThrownBy { applications deleteBy application.id }
//                .isExactlyInstanceOf(ApplicationNotFoundException::class.java)
//                .hasMessage("Vaccine Application not found")
//
//            assertThat(applications)
//                .isNotNull
//                .isEmpty()
//        }
//    }
//
//    @Nested
//    @DisplayName("Find entry")
//    inner class Find {
//
//        @Test
//        @DisplayName("successfully by id when its found")
//        fun shouldFindById() {
//            val applications = Applications()
//
//            assertThatCode {
//                applications add application
//                applications findBy application.id
//            }.doesNotThrowAnyException()
//
//            assertThat(applications)
//                .isNotNull
//                .isNotEmpty
//                .size().isEqualTo(1)
//
//            assertThat(applications["Múltipla V10"])
//                .isNotNull
//                .isNotEmpty
//                .contains(application)
//                .size().isEqualTo(1)
//        }
//
//        @Test
//        @DisplayName("return null when its not found")
//        fun shouldNotFindById() {
//            val applications = Applications()
//
//            assertThat(applications)
//                .isNotNull
//                .isEmpty()
//
//            assertThatCode {
//                val entry = applications findBy application.id
//                assertThat(entry).isNull()
//            }.doesNotThrowAnyException()
//
//            assertThat(applications)
//                .isNotNull
//                .isEmpty()
//        }
//    }
//
//    @Test
//    @DisplayName("successfully count all applications")
//    fun shouldCountAll() {
//        val applications = Applications()
//        val newApplication = msdVaccine.apply(petWeight)
//
//        assertThatCode {
//            applications add application
//            applications add newApplication
//        }.doesNotThrowAnyException()
//
//        assertThat(applications.countAll()).isEqualTo(2)
//    }
//}
