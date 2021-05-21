package app.civa.vaccination.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import java.time.Instant

data class AuditMetadata(
    @CreatedDate
    var createdOn: Instant?,
    @LastModifiedDate
    var updatedOn: Instant?,
    @Version
    var version: Long?
) {
    companion object {
        fun creation() = AuditMetadata(null, null, null)
    }
}