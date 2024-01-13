package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.dto.FitnessNewsDTO;
import org.unibl.etf.services.RssService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class FitnessNewsController {

    private final RssService rssService;

    public FitnessNewsController(RssService rssService) {
        this.rssService = rssService;
    }

    @GetMapping
    public List<FitnessNewsDTO> getNews(){
        return this.rssService.consumeFeed();
    }
}
