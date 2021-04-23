package app.civa.vaccination.domain

import java.util.*

class VaccineControlNotFoundException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {

        private const val VACCINE_CONTROL_NOT_FOUND = "Vaccine Control not found"

        infix fun from(applicationId: UUID) = VaccineControlNotFoundException(
            message = VACCINE_CONTROL_NOT_FOUND,
            expected = applicationId.toString(),
            actual = "null"
        )

        infix fun of(vaccineName: String) = VaccineControlNotFoundException(
            message = VACCINE_CONTROL_NOT_FOUND,
            expected = "List of VaccineControl of $vaccineName",
            actual = "null"
        )
    }
}