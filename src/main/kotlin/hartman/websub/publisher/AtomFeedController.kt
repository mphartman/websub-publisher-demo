package hartman.websub.publisher

import com.rometools.rome.feed.atom.Entry
import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.atom.Link
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.view.feed.AbstractAtomFeedView
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

const val FEED_ID = "urn:uuid:55ac1bf9-5345-4bd3-9ba6-62c3aa5e892d"

@Controller
@RequestMapping("/feed", produces = ["application/atom+xml"])
class AtomFeedController(@Autowired val feedEntryRepository: FeedEntryRepository) {

    @GetMapping
    fun feed(model: Model): String {
        model.addAttribute("feedContent", feedEntryRepository.findAll(Sort.by("updated").descending()))
        return "atomFeedView"
    }

}

@Component("atomFeedView")
class AtomFeedView : AbstractAtomFeedView() {

    override fun buildFeedMetadata(model: MutableMap<String, Any>, feed: Feed, request: HttpServletRequest) {
        val entries: Iterable<FeedEntry> = model["feedContent"] as Iterable<FeedEntry>
        feed.id = FEED_ID
        feed.title = "My Demo Feed"
        val link = Link()
        link.rel = "self"
        link.href = "/feed"
        feed.alternateLinks = mutableListOf(link)
        feed.updated = if (entries.firstOrNull() == null) Date() else Date.from(entries.first().updated.toInstant())
    }

    override fun buildFeedEntries(model: MutableMap<String, Any>, request: HttpServletRequest, response: HttpServletResponse): MutableList<Entry> {
        val entries: Iterable<FeedEntry> = model["feedContent"] as Iterable<FeedEntry>
        return entries.map {
            val entry = Entry()
            entry.id = it.getId().toString()
            entry.title = it.title
            entry.updated = Date.from(it.updated.toInstant())
            entry
        }.toMutableList()
    }

}