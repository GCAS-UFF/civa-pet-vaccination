package app.civa.vaccination.domain
//
//import org.assertj.core.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import org.springframework.test.util.ReflectionTestUtils
//import java.time.Month.AUGUST
//
//@DisplayName("Vaccine application should")
//internal class VaccineApplicationTest {
//
//    private val vaccine = ValidVaccine.msdVaccine
//    private val petWeight = PetWeight from 4.67
//
//    @Test
//    @DisplayName("be created successfully when input is valid")
//    fun createSuccessfully() {
//        assertThatCode { vaccine.apply(petWeight) }
//            .doesNotThrowAnyException()
//    }
//
//    @Test
//    @DisplayName("throw exception when pet weight fails validation")
//    fun exceptionWhenPetWeightIsWrong() {
//        assertThatThrownBy { vaccine.apply(PetWeight from 0.0) }
//            .isInstanceOf(InvalidPetWeightException::class.java)
//            .hasMessage("Pet weight must be positive: 0 (ZERO) or greater")
//    }
//
//}
