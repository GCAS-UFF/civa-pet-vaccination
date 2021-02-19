package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("Pet Weight should")
internal class PetWeightTest {

    @Test
    @DisplayName("be created successfully when value is valid")
    fun createSuccessfully() {
        assertThatCode {
            PetWeight(0.56)
            PetWeight(2.56)
            PetWeight(5.5)
            PetWeight(8.7123189)
            PetWeight(10.051)
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("be accessed successfully when no rounding is necessary")
    fun noRounding() {
        val weight = PetWeight(5.78)
        assertThat(weight.get()).isEqualTo(5.78)
    }

    @Test
    @DisplayName("round up when value is closer to ceil")
    fun roundUp() {
        val petWeight = PetWeight(2.678)
        assertThat(petWeight.get()).isEqualTo(2.68)
    }

    @Test
    @DisplayName("round down when value is closer to floor")
    fun roundDown() {
        val petWeight = PetWeight(2.674)
        assertThat(petWeight.get()).isEqualTo(2.67)
    }

    @Test
    @DisplayName("throw exception when value is negative or zero")
    fun exceptionWhenWeightIsZero() {
        assertThatThrownBy { PetWeight(0.0) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Pet weight must be positive: 0 (ZERO) or greater")
    }
}
