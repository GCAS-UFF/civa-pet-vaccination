package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.types.shouldBeSameInstanceAs
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import java.util.*
import kotlin.random.Random

class StashTest : BehaviorSpec({
    given("an empty stash") {
        `when`("a valid vaccine and a positive quantity is provided") {
            then("vaccine control should be added to the stash") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()

                every { vaccineMock.mustBeValid() } returns vaccineMock
                every { vaccineMock pairNameWith capture(slot) } answers {
                    Pair("Vaccine Test", slot.captured)
                }

                val stash = Stash()

                shouldNotThrowAny {
                    stash.add(vaccineMock, quantity = Random.nextInt(1, 100))
                }

                stash.shouldNotBeNull().shouldNotBeEmpty()
                stash.size shouldBeExactly 1
                stash.contains("Vaccine Test").shouldBeTrue()

                verify {
                    vaccineMock.mustBeValid()
                    vaccineMock pairNameWith ofType<VaccineControl>()
                }
            }
        }
        `when`("a valid vaccine is provided") {
            then("one vaccine control should be added to the stash") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock.mustBeValid() } returns vaccineMock
                every { vaccineMock pairNameWith capture(slot) } answers {
                    Pair("Vaccine Test", slot.captured)
                }

                val stash = Stash()

                shouldNotThrowAny {
                    stash.add(vaccineMock)
                }

                stash.shouldNotBeNull().shouldNotBeEmpty()
                stash.size shouldBeExactly 1
                stash.contains("Vaccine Test").shouldBeTrue()

                verify {
                    vaccineMock.mustBeValid()
                    vaccineMock pairNameWith ofType<VaccineControl>()
                }
            }
        }
        `when`("a valid vaccine and a negative quantity is provided") {
            then("no vaccine control should be added to the stash") {
                val vaccineMock = mockk<Vaccine>()

                val stash = Stash()

                shouldThrowExactly<IllegalQuantityException> {
                    stash.add(vaccineMock, quantity = -1)
                }

                stash.shouldNotBeNull().shouldBeEmpty()
                stash.contains("Vaccine Test").shouldBeFalse()
            }
        }
        `when`("an invalid vaccine is provided") {
            then("no vaccine control should be added to the stash") {
                val vaccineMock = mockk<Vaccine>()

                every {
                    vaccineMock.mustBeValid()
                } throws ExpiredVaccineException("Expired Vaccine Test", "E", "A")

                val stash = Stash()

                shouldThrowExactly<ExpiredVaccineException> {
                    stash.add(vaccineMock)
                }

                stash.shouldNotBeNull().shouldBeEmpty()
                stash.contains("Vaccine Test").shouldBeFalse()

                verify { vaccineMock.mustBeValid() }
            }
        }
        `when`("a control id is provided") {
            then("it should not find any match") {
                shouldThrowExactly<VaccineControlNotFoundException> {
                    Stash() findVaccineControlById UUID.randomUUID()
                }
            }
        }
        `when`("a vaccine name is provided") {
            then("it should not find any match") {
                shouldThrowExactly<VaccineControlNotFoundException> {
                    Stash() findVaccineByName "Random Name"
                }
            }
        }
    }
    given("an stash with a collection of vaccine control") {
        `when`("an existing vaccine name is provided") {
            then("a collection of control should be returned") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()

                every { vaccineMock.mustBeValid() } returns vaccineMock
                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { Pair("Vaccine Test", slot.captured) }

                val stash = Stash()
                val control = stash.add(vaccineMock)

                shouldNotThrowAny {
                    val controlList = stash findVaccineByName "Vaccine Test"

                    controlList.shouldNotBeNull().shouldNotBeEmpty()
                    controlList.contains(control)
                }
            }
        }
        `when`("a valid control id and a quantity is provided") {
            then("control should be increased") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()

                every { vaccineMock.mustBeValid() } returns vaccineMock
                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { Pair("Vaccine Test", slot.captured) }

                val stash = Stash()
                val control = stash.add(vaccineMock)

                shouldNotThrowAny {
                    val updatedControl = stash.increaseStock(control.id)

                    updatedControl shouldNotBeSameInstanceAs control

                    stash.contains("Vaccine Test").shouldBeTrue()

                    stash["Vaccine Test"]!!
                        .contains(updatedControl)
                        .shouldBeTrue()

                    stash["Vaccine Test"]!!
                        .contains(control)
                        .shouldBeFalse()
                }
            }
        }
        `when`("a valid control id is provided") {
            then("a pair of control and vaccine should be retrieved") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()

                every { vaccineMock.mustBeValid() } returns vaccineMock
                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { Pair("Vaccine Test", slot.captured) }

                val stash = Stash()
                val control = stash.add(vaccineMock)

                shouldNotThrowAny {
                    val vaccine = stash.retrieve(control.id)

                    stash.shouldNotBeNull().shouldNotBeEmpty()
                    stash.size shouldBeExactly 1
                    vaccine shouldBeSameInstanceAs vaccineMock
                }
            }
            then("it should find a single control") {
                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()

                every { vaccineMock.mustBeValid() } returns vaccineMock
                every {
                    vaccineMock pairNameWith capture(slot)
                } answers { Pair("Vaccine Test", slot.captured) }

                val stash = Stash()
                val control = stash.add(vaccineMock)

                shouldNotThrowAny {
                    stash findVaccineControlById control.id
                        .shouldNotBeNull()
                }
            }
        }
    }
})
