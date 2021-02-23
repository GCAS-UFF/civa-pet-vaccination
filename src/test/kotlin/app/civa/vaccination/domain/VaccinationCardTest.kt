package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDate
import java.time.Month.AUGUST
import java.time.ZoneOffset
import java.util.*

@DisplayName("Vaccination Card")
internal class VaccinationCardTest {

    companion object {

        private val vaccine = Vaccine(
            setOf(Species.FELINE, Species.CANINE),
            "Antirrábica",
            "Nobivac® Raiva",
            "MSD",
            Batch.from("200/21"),
            ExpirationDate(LocalDate.now(ZoneOffset.UTC).plusMonths(6))
        )

        private val petWeight = PetWeight(4.67)

        private val application = VaccineApplication(vaccine, petWeight)
    }

    @BeforeEach
    fun setup() {
        ReflectionTestUtils.setField(
            application, "createdOn",
            ApplicationDateTime.of(10, AUGUST, 2021, 10, 0)
        )
    }

    @Test
    @DisplayName("should add an application when card is empty")
    fun shouldAddWhenEmpty() {
        val petID = UUID.randomUUID()
        val card = VaccinationCard(petID, Species.CANINE)

        assertThat(card).isNotNull
            .isExactlyInstanceOf(VaccinationCard::class.java)
            .hasFieldOrPropertyWithValue("species", Species.CANINE)
            .hasFieldOrPropertyWithValue("petID", petID)

        assertThatCode { card.addApplication(application) }
            .doesNotThrowAnyException()

        assertThat(card.countAll()).isEqualTo(1)
    }

    @Test
    @DisplayName("should add an application when its after interval")
    fun shouldAddWhenValid() {
        val card = VaccinationCard(UUID.randomUUID(), Species.CANINE)

        val applicationValid = VaccineApplication(vaccine, petWeight)

        ReflectionTestUtils.setField(
            applicationValid, "createdOn",
            ApplicationDateTime.of(21, AUGUST, 2021, 10, 1)
        )

        assertThatCode {
            card.addApplication(application)
            card.addApplication(applicationValid)
        }.doesNotThrowAnyException()

        assertThat(card.countAll()).isEqualTo(2)
    }

    @Test
    @DisplayName("should throw IllegalApplicationException when its added before interval")
    fun shouldThrowIllegalApplicationException() {
        val card = VaccinationCard(UUID.randomUUID(), Species.CANINE)

        assertThatCode { card.addApplication(application) }
            .doesNotThrowAnyException()

        val applicationInterval = VaccineApplication(vaccine, petWeight)

        ReflectionTestUtils.setField(
            applicationInterval, "createdOn",
            ApplicationDateTime.of(15, AUGUST, 2021, 10, 0)
        )

        assertThatThrownBy { card.addApplication(applicationInterval) }
            .isExactlyInstanceOf(IllegalApplicationException::class.java)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Provided application cannot be added today. Status=INTERVAL")

        assertThat(card.countAll()).isEqualTo(1)
    }
}
