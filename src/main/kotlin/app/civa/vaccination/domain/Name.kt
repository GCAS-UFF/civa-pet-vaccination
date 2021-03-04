package app.civa.vaccination.domain

class Name
private constructor(
    private val classification: String,
    private val commercial: String
) {

    constructor(builder: NameBuilder) : this(
        builder.classification,
        builder.commercial
    )

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
