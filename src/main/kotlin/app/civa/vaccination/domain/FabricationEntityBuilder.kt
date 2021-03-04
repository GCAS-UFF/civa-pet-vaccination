package app.civa.vaccination.domain

class FabricationEntityBuilder : FabricationBuilder {

    override lateinit var company: String
    override lateinit var batch: Batch
    override lateinit var expirationDate: ExpirationDate

    override fun build() = Fabrication(this)

}
