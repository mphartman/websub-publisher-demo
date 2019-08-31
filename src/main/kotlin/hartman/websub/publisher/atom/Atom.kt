package hartman.websub.publisher.atom

import org.springframework.data.annotation.Id
import java.time.ZonedDateTime

data class AtomLink(val rel: String, val href: String)

data class AtomEntry(
        val id: String,
        val title: String,
        val updated: ZonedDateTime,
        val author: String?,
        val summary: String?,
        val content: String?
)

data class AtomFeed(
        @Id val id: String,
        val title: String,
        val updated: ZonedDateTime,
        val description: String?,
        val archive: Boolean = false,
        val links: List<AtomLink> = emptyList(),
        val entries: List<AtomEntry> = emptyList()
)
