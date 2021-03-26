package app.civa.vaccination.domain

interface VaccineVisitor {

    infix fun seeName(name: Name)
    infix fun seeEfficacy(efficacy: Efficacy)
    infix fun seeFabrication(fabrication: Fabrication)
}
