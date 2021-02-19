package app.civa.vaccination.domain

import app.civa.vaccination.domain.ExpirationDate.Companion.VACCINE_EXPIRED
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset.UTC

@DisplayName("Expiration Date should")
internal class ExpirationDateTest {

    @Test
    @DisplayName("not throw any exception when date is after now")
    fun shouldNotThrowExceptionWhenItsValid() {
        assertThatCode {
            ExpirationDate(LocalDate.now(UTC).plusMonths(6))
                .mustBeValid()
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("throw IllegalStateException when date is before now")
    fun shouldThrowExceptionWhenItsExpired() {
        assertThatThrownBy {
            ExpirationDate(LocalDate.now(UTC).minusDays(1))
                .mustBeValid()
        }.isExactlyInstanceOf(IllegalStateException::class.java)
            .hasMessage(VACCINE_EXPIRED)
    }
}