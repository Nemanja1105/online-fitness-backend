package org.unibl.etf.services.impl;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.dto.FitnessNewsDTO;
import org.unibl.etf.services.LogService;
import org.unibl.etf.services.RssService;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssServiceImpl implements RssService {

    @Value("${rss.link}")
    private String feedUrl;

    private final LogService logService;

    private final HttpServletRequest request;

    public RssServiceImpl(LogService logService, HttpServletRequest request) {
        this.logService = logService;
        this.request = request;
    }

    @Override
    public List<FitnessNewsDTO> consumeFeed() {
        List<FitnessNewsDTO> result = new ArrayList<>();
        try {
            URL feedSource = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedSource));
            for (SyndEntry entry : feed.getEntries()) {
                FitnessNewsDTO item = new FitnessNewsDTO();
                item.setCategory(entry.getCategories().get(0).getName());
                item.setTitle(entry.getTitle());
                item.setDescription(entry.getDescription().getValue());
                item.setLink(entry.getLink());
                result.add(item);
            }
        }
        catch(Exception e){
            this.logService.error(e.getMessage());
        }
        return result;
    }
}
