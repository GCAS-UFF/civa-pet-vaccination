package app.civa.vaccination.domain

class Fabrication
private constructor(
    private val company: String,
    private val batch: Batch,
    private val expirationDate: ExpirationDate
) {

    constructor(builder: FabricationBuilder) : this(
        builder.company.trim(),
        builder.batch,
        builder.expirationDate
    )

    infix fun accepts(visitor: FabricationVisitor) {
        visitor.seeCompany(company)
        visitor.seeBatch(batch)
        visitor.seeExpirationDate(expirationDate)
    }

    fun mustBeValid() = apply {
        expirationDate.mustBeValid()
    }

    override fun toString() =
        "Fabrication(company='$company', batch=$batch, expirationDate=$expirationDate)"
}

fun fabrication(lambda: FabricationBuilder.() -> Unit): Fabrication {
    val builder = object : FabricationBuilder {
        override lateinit var company: String
        override lateinit var batch: Batch
        override lateinit var expirationDate: ExpirationDate

        override fun build() = Fabrication(this)
    }
    builder.lambda()
    return builder.build()
}
