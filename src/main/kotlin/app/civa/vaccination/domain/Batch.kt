package app.civa.vaccination.domain

import java.time.Year
import java.time.format.DateTimeFormatter

class Batch
private constructor(private val number: String, private val year: Year) {

    val value: String
        get() = "$number/${year.format(yearDigit)}"

    companion object {

        private val yearDigit = DateTimeFormatter.ofPattern("yy")

        fun from(value: String): Batch {
            mustMatchRegex(value)

            val (prefix, suffix) = value.split("/")
            val year = Year.parse("20${suffix}")

            return Batch(prefix, year)
        }

        private fun regexMatches(value: String) =
            value.matches(Regex("\\d{3}/\\d{2}"))

        private fun mustMatchRegex(value: String) {
            when (regexMatches(value)) {
                false -> throw IllegalArgumentException()
            }
        }
    }

    override fun toString(): String {
        return "Batch(number='$number', year=$year)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Batch
        return when {
            number != other.number -> false
            year != other.year -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + year.hashCode()
        return result
    }

}

