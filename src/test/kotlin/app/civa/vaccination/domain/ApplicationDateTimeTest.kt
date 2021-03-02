package app.civa.vaccination.domain

import app.civa.vaccination.domain.ApplicationDateTime.Companion.VACCINATION_INTERVAL_DAYS
import app.civa.vaccination.domain.DateTimeStatus.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Month.AUGUST

@DisplayName("Application Date Time should")
internal class ApplicationDateTimeTest {

    companion object {

        private val a1 = ApplicationDateTime.of(10, AUGUST, 2021, 16, 0)
    }

    @Test
    @DisplayName("be SAME_DAY and equal when there's no difference of days")
    fun statusSameDay() {
        val a2 = ApplicationDateTime.of(10, AUGUST, 2021, 16, 0)

        assertThat(a1.mapStatusFrom(a2)).isEqualTo(SAME)
        assertThat(a2.mapStatusFrom(a1)).isEqualTo(SAME)
        assertThat(a1).isEqualTo(a2)
    }


    @Test
    @DisplayName("be BEFORE_TODAY and equal when application is before now")
    fun statusBeforeToday() {
        val a2 = ApplicationDateTime.of(9, AUGUST, 2021, 16, 0)

        assertThat(a1.mapStatusFrom(a2)).isEqualTo(BEFORE)
        assertThat(a1).isEqualTo(a2)
    }

    @Test
    @DisplayName(
        "be INTERVAL and equal when the difference of days is less than" +
        "$VACCINATION_INTERVAL_DAYS"
    )
    fun compareToShouldBeEqualInterval() {
        val a2 = ApplicationDateTime.of(15, AUGUST, 2021, 16, 0)

        assertThat(a1.mapStatusFrom(a2)).isEqualTo(INTERVAL)
        assertThat(a1).isEqualTo(a2)
    }

    @Test
    @DisplayName(
        "be VALID and not equal when the difference of days is more than" +
        "$VACCINATION_INTERVAL_DAYS"
    )
    fun compareToShouldNotBeEqual() {
        val a2 = ApplicationDateTime.of(20, AUGUST, 2021, 16, 0)

        assertThat(a1.mapStatusFrom(a2)).isEqualTo(VALID)
        assertThat(a1).isNotEqualTo(a2)
    }
}
