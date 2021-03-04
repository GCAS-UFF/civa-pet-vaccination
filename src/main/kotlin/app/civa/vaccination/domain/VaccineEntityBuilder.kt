package app.civa.vaccination.domain

class VaccineEntityBuilder : VaccineBuilder {

    override lateinit var name: Name
    override lateinit var efficacy: Efficacy
    override lateinit var fabrication: Fabrication

    override fun build() = Vaccine(this)

}
