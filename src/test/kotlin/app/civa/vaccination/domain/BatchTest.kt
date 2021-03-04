package app.civa.vaccination.domain

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

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
        listOf("1231232", "99/999", "/", "1234/12")
            .forEach { throwIllegalArgumentWhenInputIsInvalid(it) }
    }

    private fun throwIllegalArgumentWhenInputIsInvalid(invalidValue: String) {
        assertThatThrownBy { Batch from invalidValue }
            .isExactlyInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("get value when creation is successful")
    fun getValueSuccessfully() {
        assertThatCode {
            val value = "002/21"
            val batch = Batch from value

            assertThat(batch)
                .isExactlyInstanceOf(Batch::class.java)
                .hasNoNullFieldsOrProperties()
                .hasFieldOrPropertyWithValue("prefix", "002")
                .hasFieldOrPropertyWithValue("suffix", "21")
                .hasFieldOrPropertyWithValue("value", value)

        }.doesNotThrowAnyException()
    }
}
