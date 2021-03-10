package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.Month.AUGUST
import java.time.Period

@DisplayName("Applications should")
internal class ApplicationsTest {

    companion object {

        private val msdVaccine = vaccine {
            name = name {
                classification = "Antirrábica"
                commercial = "Nobivac® Raiva"
            }
            efficacy = efficacy {
                species = setOf(Species.CANINE, Species.FELINE)
                agents = setOf("Raiva")
            }
            fabrication = fabrication {
                company = "MSD"
                batch = Batch from "012/22"
                expirationDate = ExpirationDate from Period.ofMonths(6)
            }
        }

        private val zoetisVaccine =  vaccine {
            name = name {
                classification = "Múltipla V10"
                commercial = "Vanguard® Plus"
            }
            efficacy = efficacy {
                species = setOf(Species.CANINE)
                agents = setOf("1", "2", "3")
            }
            fabrication = fabrication {
                company = "Zoetis"
                batch = Batch from "202/01"
                expirationDate =  ExpirationDate from Period.ofMonths(1)
            }
        }

        private val petWeight = PetWeight from 2.64

        private val application = zoetisVaccine.apply(petWeight)
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
            val anotherApplication = msdVaccine.apply(petWeight)

            assertThatCode {
                applications add application
                applications add anotherApplication
            }.doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(2)

            assertThat(applications["Antirrábica"])
                .isNotNull
                .isNotEmpty
                .contains(anotherApplication)
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

            val anotherApplication = zoetisVaccine.apply(petWeight)
            ReflectionTestUtils.setField(
                anotherApplication,
                "createdOn",
                ApplicationDateTime.of(20, AUGUST, 2021, 10, 0)
            )

            assertThatCode {
                applications add application
                applications add anotherApplication
            }.doesNotThrowAnyException()

            assertThat(applications)
                .isNotNull
                .isNotEmpty
                .size().isEqualTo(1)

            assertThat(applications["Múltipla V10"])
                .isNotNull
                .isNotEmpty
                .contains(application, anotherApplication)
                .size().isEqualTo(2)
        }

        @Test
        @DisplayName("throw exception when application is duplicated")
        fun shouldNotAddApplicationWhenItsDuplicated() {
            val applications = Applications()

            assertThatThrownBy {
                applications add application
                applications add application
            }.isExactlyInstanceOf(InvalidApplicationException::class.java)
                .hasMessage("Provided application already added")

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
            val anotherApplication = zoetisVaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                anotherApplication,
                "createdOn",
                ApplicationDateTime.of(9, AUGUST, 2021, 10, 0)
            )

            assertThatThrownBy {
                applications add application
                applications add anotherApplication
            }.isExactlyInstanceOf(InvalidApplicationException::class.java)
                .hasMessage("Provided application is before today")

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
            val anotherApplication = zoetisVaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                anotherApplication,
                "createdOn",
                ApplicationDateTime.of(15, AUGUST, 2021, 10, 0)
            )

            assertThatThrownBy {
                applications add application
                applications add anotherApplication
            }.isExactlyInstanceOf(InvalidApplicationException::class.java)
                .hasMessage("Provided application cannot be added today")

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
            val newApplication = msdVaccine.apply(petWeight)

            assertThatCode {
                applications add application
                applications add newApplication
            }.doesNotThrowAnyException()

            assertThatCode { applications deleteBy application.id }
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

            assertThatThrownBy { applications deleteBy application.id }
                .isExactlyInstanceOf(ApplicationNotFoundException::class.java)
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

            assertThatCode {
                applications add application
                applications findBy application.id
            }.doesNotThrowAnyException()

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
                val entry = applications findBy application.id
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
        val newApplication = msdVaccine.apply(petWeight)

        assertThatCode {
            applications add application
            applications add newApplication
        }.doesNotThrowAnyException()

        assertThat(applications.countAll()).isEqualTo(2)
    }
}
