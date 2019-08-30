package hartman.websub.publisher

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import org.springframework.data.rest.core.annotation.RestResource
import java.time.ZonedDateTime
import javax.persistence.Entity

@Entity
@RestResource(path = "entries", rel = "entry")
class FeedEntry(
        var title: String,
        var updated: ZonedDateTime = ZonedDateTime.now()
) : AbstractEntity<Long>() {
    override fun toString() = "${super.toString()}, title: $title"
}

@RepositoryRestResource(collectionResourceRel = "entries", itemResourceRel = "entry")
interface FeedEntryRepository: PagingAndSortingRepository<FeedEntry, Long>