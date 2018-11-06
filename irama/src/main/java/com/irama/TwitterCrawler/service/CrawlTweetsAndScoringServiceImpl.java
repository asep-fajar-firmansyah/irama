package com.irama.TwitterCrawler.service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irama.TwitterCrawler.util.TranslatorService;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

/**
 * Created by alicankustemur on 06/11/2017.
 */
@Service
public class CrawlTweetsAndScoringServiceImpl implements CrawlTweetsAndScoringService {
	@Autowired
   // private static final UserMentionEntity[] UserMentionEntity = null;
	private final TweetService tweetService;
    
    public CrawlTweetsAndScoringServiceImpl(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    
    //get All Tweet Properties
    public List<Status> findAllTweet(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	return statuses;
    }
    
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
    
    /*//get specific properties
    public UserMentionEntity[] getAllSpecificPropertiesTweet(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	for (Status status : statuses) {
    		UserMentionEntity[] userMentionEntities = status.getUserMentionEntities();
        }
    	return UserMentionEntity;
    }*/
}

