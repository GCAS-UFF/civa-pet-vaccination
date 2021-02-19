package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDate
import java.time.Month.AUGUST
import java.time.ZoneOffset

@DisplayName("Applications should")
internal class ApplicationsTest {

    companion object {

        private val msdVaccine = Vaccine(
            setOf(Species.FELINE, Species.CANINE),
            "Antirrábica",
            "Nobivac® Raiva",
            "MSD",
            Batch.from("012/22"),
            ExpirationDate(LocalDate.now(ZoneOffset.UTC).plusMonths(6))
        )

        private val zoetisVaccine = Vaccine(
            setOf(Species.CANINE),
            "Múltipla V10",
            "Vanguard® Plus",
            "Zoetis",
            Batch.from("202/01"),
            ExpirationDate(LocalDate.now(ZoneOffset.UTC).plusDays(60))
        )

        private val petWeight = PetWeight(2.64)

        private val application = VaccineApplication(zoetisVaccine, petWeight)
    }

    @BeforeEach
    fun setup() {
        ReflectionTestUtils.setField(
            application,
            "createdOn",
            ApplicationDateTime.of(10, AUGUST, 2021, 10, 0)
        )
    }

    @Nested
    @DisplayName("Add entry")
    inner class Add {

        @Test
        @DisplayName("successfully when vaccine name is different")
        fun shouldAddApplicationWhenNameIsDifferent() {
            val applications = Applications()
            val newApplication = VaccineApplication(msdVaccine, petWeight)

            assertThatCode {
                applications.addEntry(application)
                applications.addEntry(newApplication)
            }.doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(2)

            assertThat(applications["Antirrábica"])
                .isNotNull
                .isNotEmpty
                .contains(newApplication)
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("successfully when createdOn is after last application interval")
        fun shouldAddApplicationWhenCreatedOnIsDifferent() {
            val applications = Applications()

            val newApplication = VaccineApplication(msdVaccine, petWeight)
            ReflectionTestUtils.setField(
                newApplication,
                "createdOn",
                ApplicationDateTime.of(20, AUGUST, 2021, 10, 0)
            )

            assertThatCode {
                applications.addEntry(application)
                applications.addEntry(newApplication)
            }.doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(2)

            assertThat(applications["Antirrábica"])
                .isNotNull
                .isNotEmpty
                .contains(newApplication)
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("throw exception when application is duplicated")
        fun shouldNotAddApplicationWhenItsDuplicated() {
            val applications = Applications()

            assertThatThrownBy {
                applications.addEntry(application)
                applications.addEntry(application)
            }.isExactlyInstanceOf(IllegalApplicationException::class.java)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Provided application already added. Status=SAME")

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("throw exception when application is before today")
        fun shouldNotAddApplicationWhenItsStatusIsBefore() {
            val applications = Applications()

            val newApplication = VaccineApplication(zoetisVaccine, petWeight)
            ReflectionTestUtils.setField(
                newApplication,
                "createdOn",
                ApplicationDateTime.of(9, AUGUST, 2021, 10, 0)
            )

            assertThatThrownBy {
                applications.addEntry(application)
                applications.addEntry(newApplication)
            }.isExactlyInstanceOf(IllegalApplicationException::class.java)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Provided application is before today. Status=BEFORE")

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("throw exception when application is before interval")
        fun shouldNotAddApplicationWhenItsStatusIsInterval() {
            val applications = Applications()

            val newApplicationMock = VaccineApplication(zoetisVaccine, petWeight)
            ReflectionTestUtils.setField(
                newApplicationMock,
                "createdOn",
                ApplicationDateTime.of(15, AUGUST, 2021, 10, 0)
            )

            assertThatThrownBy {
                applications.addEntry(application)
                applications.addEntry(newApplicationMock)
            }.isExactlyInstanceOf(IllegalApplicationException::class.java)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessage("Provided application cannot be added today. Status=INTERVAL")

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }
    }

    @Nested
    @DisplayName("Remove entry")
    inner class Remove {

        @Test
        @DisplayName("successfully by id when its found")
        fun shouldRemoveApplicationById() {
            val applications = Applications()
            val newApplication = VaccineApplication(msdVaccine, petWeight)

            assertThatCode {
                applications.addEntry(application)
                applications.addEntry(newApplication)
            }.doesNotThrowAnyException()

            assertThatCode { applications.deleteById(application.id) }
                .doesNotThrowAnyException()

            assertThat(applications["Múltipla V10"]).isNull()

            assertThat(applications["Antirrábica"])
                .isNotNull
                .isNotEmpty
                .contains(newApplication)
                .size().isEqualTo(1)

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("throw NoSuchElementException when its not found")
        fun shouldNotRemoveApplicationById() {
            val applications = Applications()

            assertThat(applications)
                .isNotNull
                .isEmpty()

            assertThatThrownBy { applications.deleteById(application.id) }
                .isExactlyInstanceOf(NoSuchElementException::class.java)
                .isInstanceOf(RuntimeException::class.java)
                .hasMessage("Vaccine Application not found")

            assertThat(applications)
                .isNotNull
                .isEmpty()
        }
    }

    @Nested
    @DisplayName("Find entry")
    inner class Find {

        @Test
        @DisplayName("successfully by id when its found")
        fun shouldFindById() {
            val applications = Applications()

            assertThatCode { applications.addEntry(application) }
                .doesNotThrowAnyException()

            assertThatCode { applications.findById(application.id) }
                .doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application)
                .size().isEqualTo(1)
        }

        @Test
        @DisplayName("return null when its not found")
        fun shouldNotFindById() {
            val applications = Applications()

            assertThat(applications)
                .isNotNull
                .isEmpty()

            assertThatCode {
                val entry = applications.findById(application.id)
                assertThat(entry).isNull()
            }.doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isEmpty()
        }
    }

    @Test
    @DisplayName("successfully count all applications")
    fun shouldCountAll() {
        val applications = Applications()
        val newApplication = VaccineApplication(msdVaccine, petWeight)

        assertThatCode {
            applications.addEntry(application)
            applications.addEntry(newApplication)
        }.doesNotThrowAnyException()

        assertThat(applications.countAll()).isEqualTo(2)
    }
}
