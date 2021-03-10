package app.civa.vaccination.domain

class Name
private constructor(
    private val classification: String,
    private val commercial: String
) {

    constructor(builder: NameBuilder) : this(
        builder.classification.trim(),
        builder.commercial.trim()
    ) {
        mustNotBeEmpty(builder.classification, builder.commercial)
    }

    private fun mustNotBeEmpty(vararg values: String) {
        values.forEach {
            if (it.isEmpty()) throw InvalidNameException()
        }
    }

    fun accept(visitor: NameVisitor) {
        visitor.visitClassification(classification)
        visitor.visitCommercial(commercial)
    }

    infix fun pairWith(application: VaccineApplication) =
        this.classification to application

}

fun name(lambda: NameBuilder.() -> Unit): Name {
    val builder = NameEntityBuilder()
    builder.lambda()
    return builder.build()
}
