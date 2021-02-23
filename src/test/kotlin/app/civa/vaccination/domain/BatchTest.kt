package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.Year

@DisplayName("Batch should")
internal class BatchTest {

    @Test
    @DisplayName("be created successfully when input matches pattern")
    fun createdSuccessfully() {
        assertThatCode {
            Batch.from("000/00")
            Batch.from("086/21")
            Batch.from("999/99")
        }.doesNotThrowAnyException()
    }

    @Test
    @DisplayName("throw IllegalArgumentException when input doesn't match pattern")
    fun failWhenValueIsWrong() {
        assertThatThrownBy {
            Batch.from("1231232")
            Batch.from("99/999")
            Batch.from("/")
            Batch.from("1234/12")
        }.isExactlyInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("get value when creation is successful")
    fun getValueSuccessfully() {
        val value = "002/21"

        assertThatCode {
            val batch = Batch.from(value)

            assertThat(batch)
                .isExactlyInstanceOf(Batch::class.java)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("number", "002")
                .hasFieldOrPropertyWithValue("year", Year.of(2021))
                .hasFieldOrPropertyWithValue("value", value)

        }.doesNotThrowAnyException()
    }
}