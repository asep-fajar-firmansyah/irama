package com.irama.TwitterCrawler.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.irama.TwitterCrawler.util.Sentiment;

import net.didion.jwnl.JWNLException;
import twitter4j.Status;

/**
 * Created by alicankustemur on 06/11/2017.
 * Modifier by Asep Fajar Firmansyah
 */
public interface CrawlTweetsAndScoringService {
   public List<Status> findAllTweet(String user);
   public List<String> findAllTextTweet(String user);
   public Map<String, String> findAllTextTweetTranslate(String user);
   public Map<String, Sentiment> findScore(String user) throws IOException, JWNLException; 
   public Map<String, Integer> findPersonalSentimentSummary(String user) throws IOException, JWNLException;
}

