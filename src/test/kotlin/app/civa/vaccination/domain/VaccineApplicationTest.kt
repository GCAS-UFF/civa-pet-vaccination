package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import java.time.Duration
import java.time.Month.AUGUST
import java.time.Period
import java.time.temporal.ChronoUnit.MONTHS

@DisplayName("Vaccine application should")
internal class VaccineApplicationTest {

    companion object {

        private val vaccine = Vaccine(
            setOf(Species.FELINE, Species.CANINE),
            "Antirrábica",
            "Nobivac® Raiva",
            "MSD",
            Batch.from("200/21"),
            ExpirationDate from Period.ofMonths(6)
        )

        private val petWeight = PetWeight from 4.67
    }

    @Test
    @DisplayName("be created successfully when input is valid")
    fun createSuccessfully() {
        assertThatCode { vaccine.apply(petWeight) }
            .doesNotThrowAnyException()
    }

    @Test
    @DisplayName("throw exception when pet weight fails validation")
    fun exceptionWhenPetWeightIsWrong() {
        assertThatThrownBy { vaccine.apply(PetWeight from 0.0) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Pet weight must be positive: 0 (ZERO) or greater")
    }

    @Nested
    @DisplayName("Equals")
    inner class Equals {

        private val applicationToday = vaccine.apply(petWeight)

        @BeforeEach
        fun setup() {
            ReflectionTestUtils.setField(
                applicationToday, "createdOn",
                ApplicationDateTime.of(10, AUGUST, 2021, 10, 0)
            )
        }

        @Test
        @DisplayName("when it happens on the same day")
        fun isEqualWhenSame() {
            val applicationSame = vaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                applicationSame, "createdOn",
                ApplicationDateTime.of(10, AUGUST, 2021, 10, 0)
            )

            assertThat(applicationToday).isEqualTo(applicationSame)
        }

        @Test
        @DisplayName("when it happens before now")
        fun isEqualWhenBefore() {
            val applicationBefore = vaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                applicationBefore, "createdOn",
                ApplicationDateTime.of(9, AUGUST, 2021, 10, 0)
            )

            assertThat(applicationToday).isEqualTo(applicationBefore)
        }

        @Test
        @DisplayName("when it happens during interval")
        fun isEqualWhenInterval() {
            val applicationInterval = vaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                applicationInterval, "createdOn",
                ApplicationDateTime.of(15, AUGUST, 2021, 10, 0)
            )

            assertThat(applicationToday).isEqualTo(applicationInterval)
        }

        @Test
        @DisplayName("not when it happens after interval")
        fun isNotEqualWhenValid() {
            val applicationValid = vaccine.apply(petWeight)

            ReflectionTestUtils.setField(
                applicationValid, "createdOn",
                ApplicationDateTime.of(20, AUGUST, 2021, 10, 0)
            )

            assertThat(applicationToday).isNotEqualTo(applicationValid)
        }
    }
}
