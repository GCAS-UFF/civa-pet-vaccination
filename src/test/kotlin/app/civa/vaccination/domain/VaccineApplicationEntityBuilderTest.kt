package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

internal class VaccineApplicationEntityBuilderTest {
    companion object {

        private val id = UUID.randomUUID()

        private val vaccine = Vaccine(
            setOf(Species.CANINE),
            "Múltipla V10",
            "Vanguard® Plus",
            "Zoetis",
            Batch.from("021/21"),
            ExpirationDate of LocalDate.now(ZoneOffset.UTC).plusDays(60)
        )

        private val petWeight = PetWeight from 2.67
        private val createdOn = ApplicationDateTime.now()
    }

    @Test
    @DisplayName("build entity successfully")
    fun build() {
        assertThatCode {
            val application = VaccineApplicationEntityBuilder()
                .id(id)
                .vaccine(vaccine)
                .petWeight(petWeight)
                .createdOn(createdOn)
                .build()

            assertThat(application).isNotNull
                .isExactlyInstanceOf(VaccineApplication::class.java)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrPropertyWithValue("vaccine", vaccine)
                .hasFieldOrPropertyWithValue("petWeight", petWeight)
                .hasFieldOrPropertyWithValue("createdOn", createdOn)

        }.doesNotThrowAnyException()
    }
}