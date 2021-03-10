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
            PetWeight from 0.56
            PetWeight from 2.56
            PetWeight from 5.5
            PetWeight from 8.7123189
            PetWeight from 10.051
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("generate correct value when no rounding is necessary")
    fun noRounding() {
        assertThat(PetWeight from 5.78)
            .isExactlyInstanceOf(PetWeight::class.java)
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("value",5.78)
    }

    @Test
    @DisplayName("round up when value is closer to ceil")
    fun roundUp() {
        assertThat(PetWeight from 2.678)
            .isExactlyInstanceOf(PetWeight::class.java)
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("value",2.68)
    }

    @Test
    @DisplayName("round down when value is closer to floor")
    fun roundDown() {
        assertThat(PetWeight from 2.674)
            .isExactlyInstanceOf(PetWeight::class.java)
            .hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("value",2.67)
    }

    @Test
    @DisplayName("throw exception when weight is negative or zero")
    fun exceptionWhenWeightIsZero() {
        assertThatThrownBy { PetWeight from 0.0 }
            .isInstanceOf(InvalidPetWeightException::class.java)
            .hasMessage("Pet weight must be positive: 0 (ZERO) or greater")
            .hasFieldOrPropertyWithValue("actual", "0.0" )
            .hasFieldOrPropertyWithValue("expected", "0.0 to be bigger than 0.0")
    }
}
