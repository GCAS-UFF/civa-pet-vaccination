package app.civa.vaccination.domain

import app.civa.vaccination.domain.Species.CANINE
import app.civa.vaccination.domain.Species.FELINE
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset.UTC

@DisplayName("Vaccine should")
internal class VaccineTest {

    @Nested
    @DisplayName("when Expired")
    inner class ExpiredVaccineTest {

        private val vaccine = ExpiredVaccine.msdVaccine

        @Test
        @DisplayName("throw IllegalStateException")
        fun mustBeValid() {
            assertThatThrownBy { vaccine.mustBeValid() }
                .isExactlyInstanceOf(IllegalStateException::class.java)
                .hasMessage("Vaccine has expired")
        }

        @Test
        @DisplayName("not throw IllegalStateException when species match")
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
        @DisplayName("throw IllegalStateException when species doesnt match")
        fun mustMatchSpecies() {
            assertThatCode { vaccine mustMatch CANINE }
                .doesNotThrowAnyException()

            assertThatThrownBy { vaccine mustMatch FELINE }
                .isInstanceOf(IllegalStateException::class.java)
                .hasMessage("Species doesn't match vaccine's species")
        }

        @Test
        @DisplayName("be equal when name and species are the same")
        fun beEqualWhenNameAndSpeciesCoincide() {
            assertThat(vaccine)
                .isEqualTo(
                    vaccine {
                        name = name {
                            classification = "Múltipla V10"
                            commercial = "Fake®"
                        }
                        efficacy = efficacy {
                            species = setOf(CANINE)
                            agents = setOf("Fake 1", "Fake 2")
                        }
                        fabrication = fabrication {
                            company = "Zoetis"
                            batch = Batch from "021/21"
                            expirationDate = ExpirationDate from Period.ofDays(60)
                        }
                    }
                )
        }

        @Test
        @DisplayName("not be equal when name is the same but species are different")
        fun notEqualWhenSpeciesDiffer() {
            assertThat(vaccine)
                .isNotEqualTo(
                    vaccine {
                        name = name {
                            classification = "Múltipla V10"
                            commercial = "Fake®"
                        }
                        efficacy = efficacy {
                            species = setOf(FELINE)
                            agents = setOf("Fake 1", "Fake 2")
                        }
                        fabrication = fabrication {
                            company = "MSD"
                            batch = Batch from "021/21"
                            expirationDate = ExpirationDate from Period.ofDays(60)
                        }
                    }
                )
        }

        @Test
        @DisplayName("not be equal when species are the same but name is different")
        fun notEqualWhenNamesDiffer() {
            assertThat(vaccine)
                .isNotEqualTo(
                    vaccine {
                        name = name {
                            classification = "Múltipla V8"
                            commercial = "Vanguard® HTLP 5 CV-L"
                        }
                        efficacy = efficacy {
                            species = setOf(FELINE)
                            agents = setOf("Fake 1", "Fake 2")
                        }
                        fabrication = fabrication {
                            company = "MSD"
                            batch = Batch from "021/21"
                            expirationDate = ExpirationDate from Period.ofDays(60)
                        }
                    }
                )
        }
    }
}
