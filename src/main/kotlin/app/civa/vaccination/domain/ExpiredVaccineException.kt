package app.civa.vaccination.domain

import java.time.LocalDate
import java.time.ZoneOffset.UTC

class ExpiredVaccineException
constructor(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        private const val VACCINE_EXPIRED = "Vaccine has expired"

        infix fun from(passedDate: LocalDate) =
            ExpiredVaccineException(
                message = VACCINE_EXPIRED,
                expected = "Before ${LocalDate.now(UTC)}",
                actual = "$passedDate"
            )
    }
}
