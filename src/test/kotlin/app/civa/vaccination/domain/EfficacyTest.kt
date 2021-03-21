package app.civa.vaccination.domain

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*

class EfficacyTest : BehaviorSpec({
    given("a species collection and an agents collection") {
        `when`("Efficacy is built") {
            and("collections are not empty") {
                then("it should not throw any exception") {
                    shouldNotThrowAny {
                        efficacy {
                            agents = setOf("Raiva")
                            species = setOf(Species.CANINE, Species.FELINE)
                        }
                    }
                }
            }
        }
        and("any collection is empty") {
            then("it should throw InvalidEfficacyException") {
                forAll(
                    row(setOf(), setOf()),
                    row(setOf(""), setOf()),
                    row(setOf(), setOf(Species.FELINE))
                ) { a, b ->
                    val exception = shouldThrowExactly<InvalidEfficacyException> {
                        efficacy { agents = a; species = b }
                    }
                    exception shouldHaveMessage "Collection must not be empty"
                    exception.explain() shouldBe
                            "Expected: Collection with at least one element, Actual: Empty collection"
                }
            }
        }
    }
    given("an Species") {
        `when`("Efficacy matches it") {
            then("it should not throw any exceptions") {
                forAll(
                    row(setOf(Species.FELINE), Species.FELINE),
                    row(setOf(Species.CANINE), Species.CANINE),
                    row(setOf(Species.CANINE, Species.FELINE), Species.CANINE),
                    row(setOf(Species.CANINE, Species.FELINE), Species.FELINE)
                ) { actual, expected ->
                    shouldNotThrowAny {
                        efficacy {
                            species = actual
                            agents = setOf("raiva")
                        } mustMatch expected
                    }
                }
            }
        }
        `when`("Efficacy doesnt match it") {
            then("it should throw SpeciesMismatchException") {
                forAll(
                    row(setOf(Species.FELINE), Species.CANINE),
                    row(setOf(Species.CANINE), Species.FELINE)
                ) { actual, expected ->
                    val exception = shouldThrowExactly<SpeciesMismatchException> {
                        efficacy {
                            species = actual
                            agents = setOf("raiva")
                        } mustMatch expected
                    }
                    exception shouldHaveMessage "Species doesn't match vaccine's species"
                    exception.explain() shouldBe "Expected: $expected, Actual: $actual"
                }
            }
        }
    }
    given("a valid instance of Efficacy") {
        `when`("it accepts an efficacy visitor") {
            then("visitor methods should be called in sequence") {
                val visitorMock = mockk<EfficacyVisitor>()
                every { visitorMock.seeSpecies(any()) } just Runs
                every { visitorMock.seeAgents(any()) } just Runs

                efficacy {
                    species = setOf(Species.FELINE, Species.CANINE)
                    agents = setOf("raiva")
                } accepts visitorMock

                verifySequence {
                    visitorMock.seeSpecies(setOf(Species.FELINE, Species.CANINE))
                    visitorMock.seeAgents(setOf("raiva"))
                }
            }
        }
    }
})
