package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.Month.AUGUST
import java.time.Period
import java.util.*

@DisplayName("Vaccination Card")
internal class VaccinationCardTest {

    companion object {

        private val vaccine = vaccine {
            name = name {
                classification = "Antirrábica"
                commercial = "Nobivac® Raiva"
            }
            efficacy = efficacy {
                species = setOf(Species.FELINE, Species.CANINE)
                agents = setOf("Raiva")
            }
            fabrication = fabrication {
                company = "MSD"
                batch = Batch from "200/21"
                expirationDate = ExpirationDate from Period.ofMonths(6)
            }
        }

        private val petWeight = PetWeight from 4.67

        private val application = vaccine.apply(petWeight)
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

        assertThatCode { card add application }
            .doesNotThrowAnyException()

        assertThat(card.size).isEqualTo(1)
    }

    @Test
    @DisplayName("should add an application when its after interval")
    fun shouldAddWhenValid() {
        val card = VaccinationCard(UUID.randomUUID(), Species.CANINE)

        val validApplication = vaccine.apply(petWeight)

        ReflectionTestUtils.setField(
            validApplication, "createdOn",
            ApplicationDateTime.of(21, AUGUST, 2021, 10, 1)
        )

        assertThatCode {
            card add application
            card add validApplication
        }.doesNotThrowAnyException()

        assertThat(card.size).isEqualTo(2)
    }

    @Test
    @DisplayName("should throw IllegalApplicationException when its added before interval")
    fun shouldThrowIllegalApplicationException() {
        val card = VaccinationCard(UUID.randomUUID(), Species.CANINE)

        assertThatCode { card add application }
            .doesNotThrowAnyException()

        val applicationOnInterval = vaccine.apply(petWeight)

        ReflectionTestUtils.setField(
            applicationOnInterval, "createdOn",
            ApplicationDateTime.of(15, AUGUST, 2021, 10, 0)
        )

        assertThatThrownBy { card add applicationOnInterval }
            .isExactlyInstanceOf(IllegalApplicationException::class.java)
            .hasMessage("Provided application cannot be added today. Status=INTERVAL")

        assertThat(card.size).isEqualTo(1)
    }
}
