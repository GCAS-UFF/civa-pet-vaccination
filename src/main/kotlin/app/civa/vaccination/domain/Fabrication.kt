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

    fun accept(visitor: FabricationVisitor) {
        visitor.seeCompany(company)
        visitor.seeBatch(batch)
        visitor.seeExpirationDate(expirationDate)
    }

    fun mustBeValid() = apply { expirationDate.mustBeValid() }

}

fun fabrication(lambda: FabricationBuilder.() -> Unit): Fabrication {
    val builder = FabricationEntityBuilder()
    builder.lambda()
    return builder.build()
}
