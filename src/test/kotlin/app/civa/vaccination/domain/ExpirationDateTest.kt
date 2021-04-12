package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset.UTC

class ExpirationDateTest : BehaviorSpec({
    given("a date-based amount of time") {
        `when`("ExpirationDate is validated") {
            then("it must be valid") {
                shouldNotThrowAny {
                    val date = ExpirationDate from Period.ofMonths(4)
                    date.mustBeValid()

                    val expirationDate = LocalDate.now(UTC).plusMonths(4)
                    date.toString() shouldBe "ExpirationDate(value=$expirationDate)"
                }
            }
        }
    }
    given("a date after now") {
        `when`("ExpirationDate is validated") {
            then("it must be valid") {
                shouldNotThrowAny {
                    val date = ExpirationDate of LocalDate.now(UTC).plusDays(20)
                    date.mustBeValid()
                }
            }
        }
    }
    given("a date before now") {
        `when`("ExpirationDate is validated") {
            then("it must be expired") {
                val date = LocalDate.now(UTC).minusDays(20)
                val exception = shouldThrowExactly<ExpiredVaccineException> {
                    val expirationDate = ExpirationDate of date
                    expirationDate.mustBeValid()
                }
                exception shouldHaveMessage "Vaccine has expired"
                exception.explain() shouldBe
                        "Expected: Before ${LocalDate.now(UTC)}, Actual: $date"
            }
        }
    }
})
