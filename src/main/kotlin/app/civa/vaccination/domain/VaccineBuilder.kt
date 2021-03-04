package app.civa.vaccination.domain

interface VaccineBuilder : Builder<Vaccine> {

    var name: Name
    var efficacy: Efficacy
    var fabrication: Fabrication

}
