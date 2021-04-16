package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate
import java.time.ZoneOffset.UTC
import kotlin.random.Random

class VaccineControlTest : BehaviorSpec({
    given("a valid vaccine") {
        val vaccineMock = mockk<Vaccine>()
        every { vaccineMock.mustBeValid() } returns vaccineMock

        and("a positive quantity to add") {
            val quantity = Random.nextInt(1, 100)

            `when`("a control is instantiated from these values") {
                then("it should be created successfully") {
                    shouldNotThrowAny {
                        val control = VaccineControl.from(quantity, vaccineMock)
                        control.shouldBeInstanceOf<VaccineControl>()
                    }
                }
            }
        }
        and("a quantity of zero to add") {
            val quantity = 0

            `when`("a control is instantiated from these values") {
                then("it should not be created") {
                    shouldThrowExactly<IllegalQuantityException> {
                        VaccineControl.from(quantity, vaccineMock)
                    }
                }
            }
        }
    }
    given("an invalid vaccine") {
        val vaccineMock = mockk<Vaccine>()
        every {
            vaccineMock.mustBeValid()
        } throws ExpiredVaccineException.from(LocalDate.now(UTC).minusDays(1))

        `when`("a control is instantiated from it") {
            then("it should not be created") {
                shouldThrowExactly<ExpiredVaccineException> {
                    VaccineControl.from(Random.nextInt(1, 100), vaccineMock)
                }
            }
        }
    }
    given("a vaccine control") {
        val vaccineMock = mockk<Vaccine>()
        every { vaccineMock.mustBeValid() } returns vaccineMock

        val control = VaccineControl.from(Random.nextInt(1, 100), vaccineMock)

        `when`("a certain quantity is added") {
            val quantity = Random.nextInt(1, 100)

            then("it should return a new control with updated quantity") {
                shouldNotThrowAny {
                    control increaseBy quantity
                }
            }
        }
    }
})
