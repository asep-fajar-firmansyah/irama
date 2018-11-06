package com.irama.TwitterCrawler.service;

import java.util.List;
import java.util.Map;

import twitter4j.Status;

/**
 * Created by alicankustemur on 06/11/2017.
 */
public interface CrawlTweetsAndScoringService {
   public List<Status> findAllTweet(String user);
    public List<String> findAllTextTweet(String user);
    public List<String> findAllTextTweetTranslate(String user);
}

