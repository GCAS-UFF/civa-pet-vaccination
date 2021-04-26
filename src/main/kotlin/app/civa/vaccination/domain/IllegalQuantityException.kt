package app.civa.vaccination.domain

class IllegalQuantityException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {

    companion object {
        infix fun from(quantity: Int) = IllegalQuantityException(
            message = "Quantity must be positive",
            expected = "quantity greater than 0",
            actual = "$quantity"
        )

        fun of(quantity: Int, quantityWanted: Int) = IllegalQuantityException(
            message = "Not enough quantity to serve",
            expected = "Quantity greater than $quantityWanted",
            actual = "$quantity"
        )
    }
}
