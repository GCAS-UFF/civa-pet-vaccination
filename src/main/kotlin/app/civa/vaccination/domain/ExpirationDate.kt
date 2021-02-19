package app.civa.vaccination.domain

import java.time.LocalDate
import java.time.Month
import java.time.ZoneOffset

class ExpirationDate
constructor(private val value: LocalDate) {

    companion object {

        const val VACCINE_EXPIRED = "Vaccine has expired"

        fun of(day: Int, month: Month, year: Int): ExpirationDate {
            return ExpirationDate(LocalDate.of(year, month, day))
        }
    }

    private fun hasExpired(): Boolean {
        val today = LocalDate.now(ZoneOffset.UTC)
        return value.isBefore(today)
    }

    fun mustBeValid() {
        when {
            hasExpired() -> throw IllegalStateException(VACCINE_EXPIRED)
        }
    }
}