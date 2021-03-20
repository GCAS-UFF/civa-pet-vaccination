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

    infix fun accepts(visitor: NameVisitor) {
        visitor.visitClassification(classification)
        visitor.visitCommercial(commercial)
    }

    infix fun pairWith(application: VaccineApplication) =
        this.classification to application

    override fun toString() =
        "Name(classification='$classification', commercial='$commercial')"

}

fun name(lambda: NameBuilder.() -> Unit): Name {
    val builder = object : NameBuilder {
        override lateinit var classification: String
        override lateinit var commercial: String

        override fun build() = Name(this)
    }
    builder.lambda()
    return builder.build()
}
