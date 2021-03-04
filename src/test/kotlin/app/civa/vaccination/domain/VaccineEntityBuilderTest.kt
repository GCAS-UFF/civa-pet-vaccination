package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.ZoneOffset.UTC

internal class VaccineEntityBuilderTest {

    @Test
    @DisplayName("build entity successfully")
    fun build() {
        val species = setOf(Species.CANINE)
        val name = "Múltipla V8"
        val commercialName = "Vanguard® HTLP 5 CV-L"
        val company = "Zoetis"
        val batch = Batch.from("231/21")
        val expirationDate = LocalDate.now(UTC).plusMonths(6)

        assertThatCode {
            val vaccine = VaccineEntityBuilder()
                .species(species)
                .name(name)
                .commercialName(commercialName)
                .company(company)
                .batch(batch)
                .expirationDate(expirationDate)
                .build()

            assertThat(vaccine)
                .isExactlyInstanceOf(Vaccine::class.java)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("species", species)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("commercialName", commercialName)
                .hasFieldOrPropertyWithValue("company", company)
                .hasFieldOrPropertyWithValue("batch", batch)
                .hasFieldOrPropertyWithValue("expirationDate", ExpirationDate(expirationDate))

        }.doesNotThrowAnyException()
    }
}
