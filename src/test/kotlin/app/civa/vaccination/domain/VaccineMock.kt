package app.civa.vaccination.domain

import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset

class ValidVaccine {
    companion object {

        val zoetisVaccine = vaccine {
            name = name {
                classification = "Múltipla V10"
                commercial = "Vanguard® Plus"
            }
            efficacy = efficacy {
                species = setOf(Species.CANINE)
                agents = setOf(
                    "Cinomose canina",
                    "Hepatite infecciosa canina",
                    "Doença respiratória",
                    "Parainfluenza canina",
                    "Enterite",
                    "Parvovírus canino",
                    "Leptospiroses"
                )
            }
            fabrication = fabrication {
                company = "Zoetis"
                batch = Batch from "021/21"
                expirationDate = ExpirationDate from Period.ofDays(60)
            }
        }

        val msdVaccine = vaccine {
            name = name {
                classification = "Antirrábica"
                commercial = "Nobivac® Raiva"
            }
            efficacy = efficacy {
                species = setOf(Species.FELINE, Species.CANINE)
                agents = setOf("Raiva")
            }
            fabrication = fabrication {
                company = "MSD"
                batch = Batch from "200/21"
                expirationDate = ExpirationDate from Period.ofMonths(6)
            }
        }

    }
}

class ExpiredVaccine {
    companion object {

        val msdVaccine = vaccine {
            name = name {
                classification = "Antirrábica"
                commercial = "Nobivac® Raiva"
            }
            efficacy = efficacy {
                species = setOf(Species.CANINE, Species.FELINE)
                agents = setOf("Raiva")
            }
            fabrication = fabrication {
                company = "MSD"
                batch = Batch from "002/20"
                expirationDate = ExpirationDate of LocalDate.now(ZoneOffset.UTC).minusDays(1)
            }
        }
    }
}

