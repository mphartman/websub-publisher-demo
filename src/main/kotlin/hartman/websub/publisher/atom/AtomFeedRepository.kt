package hartman.websub.publisher.atom

import org.springframework.data.repository.CrudRepository

interface AtomFeedRepository: CrudRepository<AtomFeed, String> {
}