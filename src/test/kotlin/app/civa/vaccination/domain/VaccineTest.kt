package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset.UTC

@DisplayName("Vaccine should")
internal class VaccineTest {

    @Nested
    @DisplayName("when Expired")
    inner class ExpiredVaccine {

        private val vaccine = Vaccine(
            setOf(Species.FELINE, Species.CANINE),
            "Antirrábica",
            "Nobivac® Raiva",
            "MSD",
            Batch.from("002/20"),
            ExpirationDate(LocalDate.now(UTC).minusDays(1))
        )

        @Test
        @DisplayName("throw IllegalStateException when is expired")
        fun mustBeValid() {
            assertThatThrownBy { vaccine.mustBeValid() }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Vaccine has expired")
        }

        @Test
        @DisplayName("not throw IllegalStateException when species match")
        fun mustMatchSpecies() {
            assertThatCode {
                vaccine mustMatch Species.FELINE
                vaccine mustMatch Species.CANINE
            }.doesNotThrowAnyException()
        }
    }

    @Nested
    @DisplayName("Canine Valid should")
    inner class CanineValidVaccine {

        private val vaccine = Vaccine(
            setOf(Species.CANINE),
            "Múltipla V10",
            "Vanguard® Plus",
            "Zoetis",
            Batch.from("021/21"),
            ExpirationDate(LocalDate.now(UTC).plusDays(60))
        )

        @Test
        @DisplayName("not throw any exception when is valid")
        fun mustBeValid() {
            assertThatCode { vaccine.mustBeValid() }
                .doesNotThrowAnyException()
        }

        @Test
        @DisplayName("throw IllegalStateException when species doesnt match")
        fun mustMatchSpecies() {
            assertThatCode { vaccine mustMatch Species.CANINE }
                .doesNotThrowAnyException()

            assertThatThrownBy { vaccine mustMatch Species.FELINE }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Species doesn't match vaccine's species")
        }

        @Test
        @DisplayName("be equal when name and species are the same")
        fun beEqualWhenNameAndSpeciesCoincide() {
            val otherVaccine = Vaccine(
                setOf(Species.CANINE),
                "Múltipla V10",
                "Fake®",
                "Fake",
                Batch.from("086/21"),
                ExpirationDate(LocalDate.now(UTC).plusDays(60))
            )
            assertThat(vaccine).isEqualTo(otherVaccine)
        }

        @Test
        @DisplayName("not be equal when name is the same but species are different")
        fun notEqualWhenSpeciesDiffer() {
            val otherVaccine = Vaccine(
                setOf(Species.FELINE),
                "Múltipla V10",
                "Vanguard® Plus",
                "Zoetis",
                Batch.from("086/21"),
                ExpirationDate(LocalDate.now(UTC).plusDays(60))
            )
            assertThat(vaccine).isNotEqualTo(otherVaccine)
        }

        @Test
        @DisplayName("not be equal when species are the same but name is different")
        fun notEqualWhenNamesDiffer() {
            val otherVaccine = Vaccine(
                setOf(Species.CANINE),
                "Múltipla V8",
                "Vanguard® HTLP 5 CV-L",
                "Zoetis",
                Batch.from("081/21"),
                ExpirationDate(LocalDate.now(UTC).plusDays(60))
            )
            assertThat(vaccine).isNotEqualTo(otherVaccine)
        }
    }
}
