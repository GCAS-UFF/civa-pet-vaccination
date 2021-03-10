package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset.UTC

@DisplayName("Expiration Date should")
internal class ExpirationDateTest {

    @Test
    @DisplayName("not throw any exception when date is after now")
    fun shouldNotThrowExceptionWhenItsValid() {
        assertThatCode {
            val date = ExpirationDate of LocalDate.now(UTC).plusMonths(6)
            date.mustBeValid()
        }.doesNotThrowAnyException()

        assertThatCode {
            val date = ExpirationDate from Period.ofMonths(6)
            date.mustBeValid()
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("throw IllegalStateException when date is before now")
    fun shouldThrowExceptionWhenItsExpired() {
        assertThatThrownBy {
            val date = ExpirationDate of LocalDate.now(UTC).minusDays(1)
            date.mustBeValid()
        }.isExactlyInstanceOf(VaccineExpiredException::class.java)
            .hasMessage("Vaccine has expired")
    }
}
