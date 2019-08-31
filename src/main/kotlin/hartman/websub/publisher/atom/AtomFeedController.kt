package hartman.websub.publisher.atom

import com.rometools.rome.feed.atom.Entry
import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.atom.Link
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@Controller
class AtomFeedController(@Autowired private val atomFeedService: AtomFeedService) {

    @GetMapping("/latest", produces = ["application/atom+xml"])
    fun latest(model: Model): String {
        val latestFeed = atomFeedService.latest()
        model.addAttribute("feedContent", latestFeed)
        return "atomFeedView"
    }

    @GetMapping("/archive/{archiveId}", produces = ["application/atom+xml"])
    fun archive(@PathVariable("archiveId", required = true) archiveId: String, model: Model): String {
        val archive = atomFeedService.archive(archiveId)
        if (archive == null) {
            throw ArchiveNotFoundException()
        } else {
            model.addAttribute("feedContent", archive)
            return "atomFeedView"
        }
    }
}

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "archive not found")
class ArchiveNotFoundException : RuntimeException()

@Component("atomFeedView")
class AtomFeedView : AbstractAtomFeedView() {

    override fun buildFeedMetadata(model: MutableMap<String, Any>, feed: Feed, request: HttpServletRequest) {
        val atomFeed = model["feedContent"] as AtomFeed
        feed.id = atomFeed.id
        feed.title = atomFeed.title
        val link = Link()
        link.rel = "self"
        link.href = "/archive/${atomFeed.id}"
        feed.alternateLinks = mutableListOf(link)
        feed.updated = Date.from(atomFeed.updated.toInstant())
    }

    override fun buildFeedEntries(model: MutableMap<String, Any>, request: HttpServletRequest, response: HttpServletResponse): MutableList<Entry> {
        val atomFeed = model["feedContent"] as AtomFeed
        return atomFeed.entries.map {
            val entry = Entry()
            entry.id = it.id
            entry.title = it.title
            entry.updated = Date.from(it.updated.toInstant())
            entry
        }.toMutableList()
    }

}