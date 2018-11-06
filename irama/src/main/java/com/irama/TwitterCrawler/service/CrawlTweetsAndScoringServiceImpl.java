package com.irama.TwitterCrawler.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irama.TwitterCrawler.util.SentimentDetector;
import com.irama.TwitterCrawler.util.TranslatorService;

import net.didion.jwnl.JWNLException;

import java.util.regex.Pattern;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 * Created by alicankustemur on 06/11/2017.
 * Modifier by Asep Fajar Firmansyah 
 */
@Service
public class CrawlTweetsAndScoringServiceImpl implements CrawlTweetsAndScoringService {
	@Autowired
   // private static final UserMentionEntity[] UserMentionEntity = null;
	private final TweetService tweetService;
	private static final String DESCRIPTION = "Supporters, opponents and neutral";
	
    public CrawlTweetsAndScoringServiceImpl(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    
    //get All Tweet Properties
    public List<Status> findAllTweet(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	return statuses;
    }
    /*
     * Author : Asep Fajar Firmansyah
     * @see com.irama.TwitterCrawler.service.CrawlTweetsAndScoringService#findAllTextTweet(java.lang.String)
     */
    //get All Text Tweet
    public List<String> findAllTextTweet(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	List<String> listText =  new ArrayList<String>(); 
    	
    	for (Status status : statuses) {
            String userText = status.getText();
            listText.add(userText);          
        }
    	return listText;
    }
    /*
     * Author : Asep Fajar Firmansyah
     * @see com.irama.TwitterCrawler.service.CrawlTweetsAndScoringService#findAllTextTweetTranslate(java.lang.String)
     */
   public List<String> findAllTextTweetTranslate(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	List<String> listText =  new ArrayList<String>(); 
    	TranslatorService trans = new TranslatorService();
    	String userTextTrans = null;
    	for (Status status : statuses) {
            String userText = status.getText();
            try {
				userTextTrans = trans.translateSentence(userText);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            listText.add(userTextTrans);          
        }
    	return listText;
    }
    public Map<String, Double> findScore(String user) throws IOException, JWNLException{
    	SentimentDetector detector;
    	double score;
    	Pattern pattern;
    	pattern = Pattern.compile("((^|\\s+)(RT|rt|MT|mt|FF|ff|RLRT|rlrt|OH|oh)(\\s+|$))|([#|@]\\s*)", Pattern.CASE_INSENSITIVE);
    	Map<String, Double> scores = new HashMap<String, Double>();
    	
    	detector = new SentimentDetector();
    	
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	Map<String,Integer> map = new HashMap<String,Integer>();
    	List<String> listText =  new ArrayList<String>(); 
    	TranslatorService trans = new TranslatorService();
    	String userTextTrans = null;
    	int x=0;
    	for (Status status : statuses) {
            String userText = status.getText();
            try {
            	if (x<=3) {
				userTextTrans = trans.translateSentence(userText);
				//userTextTrans = userTextTrans.replaceAll(",", "");
				String text= pattern.matcher(userTextTrans).replaceAll("");
				score = detector.detect(text);
				scores.put(userTextTrans, score);
            	}else {
            		break;
            	}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            x++;          
        }
    	return scores;
    }
    /*//get specific properties
    public UserMentionEntity[] getAllSpecificPropertiesTweet(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	for (Status status : statuses) {
    		UserMentionEntity[] userMentionEntities = status.getUserMentionEntities();
        }
    	return UserMentionEntity;
    }*/
}

