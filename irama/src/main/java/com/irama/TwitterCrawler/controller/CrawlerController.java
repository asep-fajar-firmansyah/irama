package com.irama.TwitterCrawler.controller;

import com.irama.TwitterCrawler.service.CrawlTweetsAndScoringService;
import com.irama.TwitterCrawler.service.TweetService;
import com.irama.TwitterCrawler.service.TweetServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

import java.util.List;
import java.util.Map;

/**
 * Created by alicankustemur on 29/10/2017.
 */
@RestController
@RequestMapping(value= {"/crawler"}, produces="application/json; charset=UTF-8")
public class CrawlerController {
	@Autowired
	private final CrawlTweetsAndScoringService crawlTweetsAndScoringService;
   
    public CrawlerController(CrawlTweetsAndScoringService crawlTweetsAndScoringService) {
        this.crawlTweetsAndScoringService = crawlTweetsAndScoringService;
    }

   
    @GetMapping("/all-properies-tweet/{user}")
    public List<Status>  findAllPropertiesTweetCrawler(@PathVariable String user) {
       return crawlTweetsAndScoringService.findAllTweet(user) ;
    }
    @GetMapping("/tweet/{user}")
    public List<String>  findAllTextTweetCrawler(@PathVariable String user) {
       return crawlTweetsAndScoringService.findAllTextTweet(user) ;
    }
    @GetMapping("/tweet/translate/{user}")
    public List<String>  getAllTextTweetTranslateCrawler(@PathVariable String user) {
       return crawlTweetsAndScoringService.findAllTextTweetTranslate(user) ;
    }

}

