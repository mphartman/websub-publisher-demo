package hartman.websub.publisher.atom

import org.springframework.stereotype.Service

@Service
class AtomFeedService(private val atomFeedRepository: AtomFeedRepository) {

    fun latest(): AtomFeed {
        TODO()
    }

    fun archive(archiveId: String): AtomFeed? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}