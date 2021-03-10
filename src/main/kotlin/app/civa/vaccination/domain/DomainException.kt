package app.civa.vaccination.domain

abstract class DomainException(
    override val message: String,
    protected open val expected: String,
    protected open val actual: String
) : Exception(message) {

    fun explain() = "Expected: $expected, Actual: $actual"
}
