package app.civa.vaccination.domain

import java.util.*

/**
 * Stores a vaccine and manage its quantity
 *
 * This class is responsible for managing the quantity of a single stored vaccine.
 * [VaccineControl] is designed as an immutable object
 *
 * @property id the UUID of the control
 * @property quantity the amount of [Vaccine] stored
 * @property vaccine the [Vaccine] stored
 * @constructor is private and therefore unavailable, should use factory method instead
 */
class VaccineControl
private constructor(
    val id: UUID,
    private val quantity: Int,
    private val vaccine: Vaccine
) {
    companion object {
        /**
         * Factory method that instantiates a new [VaccineControl] if the provided quantity
         * is positive
         * @return a new instance of VaccineControl
         * @throws IllegalQuantityException when the quantity provided is not positive.
         */
        fun from(quantity: Int, vaccine: Vaccine) = VaccineControl(
            id = UUID.randomUUID(),
            quantity = quantity.mustBePositive(),
            vaccine = vaccine.mustBeValid()
        )
    }

    /**
     * Increases [quantity] if the value provided is positive.
     * @return a new instance of [VaccineControl] with updated [quantity]
     * @throws IllegalQuantityException when the [quantityToBeAdded] provided is not positive.
     * @throws ExpiredVaccineException when the [vaccine] has already expired.
     */
    infix fun increaseBy(quantityToBeAdded: Int) = VaccineControl(
        id,
        quantity = this.quantity + quantityToBeAdded.mustBePositive(),
        vaccine = vaccine.mustBeValid()
    )

    /**
     * Decreases quantity if the value provided is positive and [quantity] is enough.
     * If an argument is not provided, [quantityWanted] will default to 1.
     * @return a [Pair] of updated [VaccineControl] and [Vaccine]
     * @throws IllegalQuantityException if [quantityWanted] provided is not positive.
     *  or [quantity] is not enough.
     */
    fun retrieve(quantityWanted: Int = 1) = VaccineControl(
        id,
        quantity = this.subtractQuantity(quantityWanted),
        vaccine = vaccine.mustBeValid()
    ) to vaccine

    private infix fun subtractQuantity(quantityWanted: Int): Int {
        this.quantity mustHaveEnough quantityWanted.mustBePositive()
        return this.quantity - quantityWanted
    }

    fun getVaccineKey() = this.vaccine.makeKey()

}

private fun Int.mustBePositive() =
    when (this <= 0) {
        true -> throw IllegalQuantityException from this
        else -> this
    }

private infix fun Int.mustHaveEnough(quantityWanted: Int) {
    when (this < quantityWanted) {
        true -> throw IllegalQuantityException.of(this, quantityWanted)
    }
}
