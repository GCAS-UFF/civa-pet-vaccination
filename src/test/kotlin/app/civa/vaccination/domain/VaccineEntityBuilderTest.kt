package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset.UTC

internal class VaccineEntityBuilderTest {

    companion object {

        private val species = setOf(Species.CANINE)
        private val name = "Múltipla V8"
        private val commercialName = "Vanguard® HTLP 5 CV-L"
        private val company = "Zoetis"
        private val batch = Batch.from("231/21")
        private val expirationDate = LocalDate.now(UTC).plusMonths(6)

    }

    @Test
    @DisplayName("build entity successfully")
    fun build() {
        assertThatCode {
            val vaccine = VaccineEntityBuilder()
                .species(species)
                .name(name)
                .commercialName(commercialName)
                .company(company)
                .batch(batch)
                .expirationDate(ExpirationDate of expirationDate)
                .build()

            assertThat(vaccine).isNotNull
                .isExactlyInstanceOf(Vaccine::class.java)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("species", species)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("commercialName", commercialName)
                .hasFieldOrPropertyWithValue("company", company)
                .hasFieldOrPropertyWithValue("batch", batch)
                .hasFieldOrPropertyWithValue("expirationDate", ExpirationDate of expirationDate)

        }.doesNotThrowAnyException()
    }
}
