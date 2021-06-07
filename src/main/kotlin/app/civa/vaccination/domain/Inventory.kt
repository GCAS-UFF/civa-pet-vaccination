package app.civa.vaccination.domain

import java.util.*

class Inventory(
    val id: UUID,
    private val vetId: UUID,
    private val stash: Stash
) {

    companion object {
        infix fun from(vetId: UUID): Inventory {
            return Inventory(
                id = UUID.randomUUID(),
                vetId,
                stash = Stash()
            )
        }
    }
}
