package app.civa.vaccination.domain

class EfficacyEntityBuilder : EfficacyBuilder {

    override lateinit var species: Collection<Species>
    override lateinit var agents: Collection<String>

    override fun build() = Efficacy(this)

}
