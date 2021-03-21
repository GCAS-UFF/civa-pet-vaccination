package app.civa.vaccination.domain

interface VaccineVisitor {

    fun seeName(name: Name)
    fun seeEfficacy(efficacy: Efficacy)
    fun seeFabrication(fabrication: Fabrication)
}
