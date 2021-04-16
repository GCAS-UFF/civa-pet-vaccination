package app.civa.vaccination.domain

import java.util.*

class VaccineControl
private constructor(
    private val id: UUID,
    private val quantity: Int,
    private val vaccine: Vaccine
) {
    infix fun increaseBy(quantity: Int) = VaccineControl(
        id,
        quantity = this.quantity + quantity.mustBePositive(),
        vaccine = vaccine.mustBeValid()
    )

    infix fun retrieve(quantity: Int) = VaccineControl(
        id,
        quantity = this.quantity - quantity.mustBePositive(),
        vaccine = vaccine.mustBeValid()
    )

    companion object {
        fun from(quantity: Int, vaccine: Vaccine) = VaccineControl(
            id = UUID.randomUUID(),
            quantity = quantity.mustBePositive(),
            vaccine = vaccine.mustBeValid()
        )
    }
}

fun Int.mustBePositive(): Int {
    if (this <= 0) throw IllegalQuantityException fromNew this
    else return this
}
