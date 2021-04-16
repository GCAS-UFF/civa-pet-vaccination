package app.civa.vaccination.domain

class IllegalQuantityException(
    override val message: String,
    override val expected: String,
    override val actual: String
) : DomainException(message, expected, actual) {
companion object {
    infix fun fromNew(quantity:Int) = IllegalQuantityException(
        message = "Quantity must be positive when instantiating",
        expected = "quantity greater than 0",
        actual = "$quantity"
    )
}
}
