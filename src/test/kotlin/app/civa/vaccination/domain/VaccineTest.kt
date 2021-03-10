package app.civa.vaccination.domain

import app.civa.vaccination.domain.Species.CANINE
import app.civa.vaccination.domain.Species.FELINE
import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Vaccine should")
internal class VaccineTest {

    @Nested
    @DisplayName("when Expired")
    inner class ExpiredVaccineTest {

        private val vaccine = ExpiredVaccine.msdVaccine

        @Test
        @DisplayName("throw VaccineExpiredException")
        fun mustBeValid() {
            assertThatThrownBy { vaccine.mustBeValid() }
                .isExactlyInstanceOf(VaccineExpiredException::class.java)
                .hasMessage("Vaccine has expired")
        }

        @Test
        @DisplayName("not throw any exception when species match")
        fun mustMatchSpecies() {
            assertThatCode {
                vaccine mustMatch FELINE
                vaccine mustMatch CANINE
            }.doesNotThrowAnyException()
        }
    }

    @Nested
    @DisplayName("when Valid")
    inner class CanineValidVaccineTest {

        private val vaccine = ValidVaccine.zoetisVaccine

        @Test
        @DisplayName("not throw any exception when is valid")
        fun mustBeValid() {
            assertThatCode { vaccine.mustBeValid() }
                .doesNotThrowAnyException()
        }

        @Test
        @DisplayName("throw SpeciesMismatchException when species doesnt match")
        fun mustMatchSpecies() {
            assertThatThrownBy { vaccine mustMatch FELINE }
                .isExactlyInstanceOf(SpeciesMismatchException::class.java)
                .isInstanceOf(DomainException::class.java)
                .hasMessageContaining("Species doesn't match vaccine's species")
        }
    }
}
