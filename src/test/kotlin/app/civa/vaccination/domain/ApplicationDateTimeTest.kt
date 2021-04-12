package app.civa.vaccination.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.Month.AUGUST

class ApplicationDateTimeTest : BehaviorSpec({
    given("an application date time") {
        val dateTime = ApplicationDateTime.of(
            day = GIVEN_DAY, month = AUGUST, year = 2021,
            hour = 16, minute = 0
        )

        `when`("the difference of days is greater or equal than $VACCINATION_INTERVAL_DAYS") {
            val day = GIVEN_DAY + VACCINATION_INTERVAL_DAYS

            then("it should map to VALID") {
                val other = ApplicationDateTime.of(
                    day = day,
                    month = AUGUST, year = 2021,
                    hour = 16, minute = 0
                )
                dateTime mapStatus other shouldBe DateTimeStatus.VALID
            }
        }

        `when`("the difference of days is less than $VACCINATION_INTERVAL_DAYS") {
            val day = GIVEN_DAY + VACCINATION_INTERVAL_DAYS - 1

            then("it should map to INTERVAL") {
                val other = ApplicationDateTime.of(
                    day = day,
                    month = AUGUST, year = 2021,
                    hour = 16, minute = 0
                )
                dateTime mapStatus other shouldBe DateTimeStatus.INTERVAL
            }
        }

        `when`("there's no difference of days") {
            val day = GIVEN_DAY

            then("it should map to SAME") {
                val other = ApplicationDateTime.of(
                    day = day, month = AUGUST, year = 2021,
                    hour = 16, minute = 0
                )
                dateTime mapStatus other shouldBe DateTimeStatus.SAME
            }
        }

        `when`("it's before given date time") {
            val day = GIVEN_DAY - 1

            then("it should map to BEFORE") {
                val other = ApplicationDateTime.of(
                    day = day, month = AUGUST, year = 2021,
                    hour = 16, minute = 0
                )
                dateTime mapStatus other shouldBe DateTimeStatus.BEFORE
            }
        }
    }
}) {
    companion object {
        private const val GIVEN_DAY = 10
        private const val VACCINATION_INTERVAL_DAYS = 7
    }
}
