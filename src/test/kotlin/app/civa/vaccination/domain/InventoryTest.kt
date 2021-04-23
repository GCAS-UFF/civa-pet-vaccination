package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldNotBeNull
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlin.random.Random

class InventoryTest : BehaviorSpec({
    given("an empty inventory") {
        `when`("a vaccine and a positive quantity is provided") {
            then("vaccine control should be added to the inventory") {
                val inventory = Inventory()

                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock.mustBeValid() } returns vaccineMock
                every { vaccineMock pairNameWith capture(slot) } answers {
                    Pair("Vaccine Test", slot.captured)
                }

                shouldNotThrowAny {
                    inventory.add(vaccineMock, Random.nextInt(1, 100))
                }

                inventory.shouldNotBeNull().shouldNotBeEmpty()
                inventory.size shouldBeExactly 1
                inventory.contains("Vaccine Test").shouldBeTrue()

                verify {
                    vaccineMock.mustBeValid()
                    vaccineMock pairNameWith ofType<VaccineControl>()
                }
            }
        }
        `when`("a vaccine is provided") {
            then("one vaccine control should be added to the inventory") {
                val inventory = Inventory()

                val slot = slot<VaccineControl>()
                val vaccineMock = mockk<Vaccine>()
                every { vaccineMock.mustBeValid() } returns vaccineMock
                every { vaccineMock pairNameWith capture(slot) } answers {
                    Pair("Vaccine Test", slot.captured)
                }

                shouldNotThrowAny {
                    inventory.add(vaccineMock)
                }

                inventory.shouldNotBeNull().shouldNotBeEmpty()
                inventory.size shouldBeExactly 1
                inventory.contains("Vaccine Test").shouldBeTrue()

                verify {
                    vaccineMock.mustBeValid()
                    vaccineMock pairNameWith ofType<VaccineControl>()
                }
            }
        }
    }
})
