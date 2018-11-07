package com.irama.TwitterCrawler.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.irama.TwitterCrawler.util.Sentiment;
import com.irama.TwitterCrawler.util.SentimentDetector;
import com.irama.TwitterCrawler.util.TranslatorService;

import net.didion.jwnl.JWNLException;
import twitter4j.Status;

/**
 * Created by alicankustemur on 06/11/2017.
 * Modifier by Asep Fajar Firmansyah 
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
   public Map<String, String> findAllTextTweetTranslate(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	Map<String,String> map = new HashMap<String,String>();
    	List<String> listText =  new ArrayList<String>(); 
    	TranslatorService trans = new TranslatorService();
    	String userTextTrans = null;
    	int x=0;
    	for (Status status : statuses) {
    		if (x<=3) {
            String userText = status.getText();
            try {
				userTextTrans = trans.translateSentence(userText);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            map.put(userText, userTextTrans);
    		}else {
        		break;
        	}
    		x++;
        }
    	return map;
    }
    public Map<String, Sentiment> findScore(String user) throws IOException, JWNLException{
    	Sentiment sentiment;
    	SentimentDetector detector;
    	double score;
    	Pattern pattern;
    	pattern = Pattern.compile("((^|\\s+)(RT|rt|MT|mt|FF|ff|RLRT|rlrt|OH|oh)(\\s+|$))|([#|@]\\s*)", Pattern.CASE_INSENSITIVE);
    	Map<String, Sentiment> scores = new HashMap<String, Sentiment>();
    	
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
				sentiment = getSentiment(score);
				scores.put(userTextTrans, sentiment);
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
    private Sentiment getSentiment(Double score) {

		Sentiment sentiment;
		int objective;
		int supporter = 0, opponent = 0;// neutral=0;

		if (score > 0)
			supporter++;
		else if (score < 0)
			opponent++;
		// else neutral++;
		objective = supporter - opponent;

		if (objective == 0)
			sentiment = Sentiment.NEUTRAL;
		else if (objective > 0)
			sentiment = Sentiment.POSITIVE;
		else
			sentiment = Sentiment.NEGATIVE;

		return sentiment;
	}
    public List<String> findAllTextTweetBasedPeriod(String user){
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	List<String> listText =  new ArrayList<String>(); 
    	
    	for (Status status : statuses) {
            String userText = status.getText();
            listText.add(userText);          
        }
    	return listText;
    }
    public Map<String, Integer> findPersonalSentimentSummary(String user) throws IOException, JWNLException{
    	Sentiment sentiment;
    	SentimentDetector detector;
    	double score;
    	Pattern pattern;
    	pattern = Pattern.compile("((^|\\s+)(RT|rt|MT|mt|FF|ff|RLRT|rlrt|OH|oh)(\\s+|$))|([#|@]\\s*)", Pattern.CASE_INSENSITIVE);
    	Map<String, Integer> scores = new HashMap<String, Integer>();
    	int positive=0;
    	int negative=0;
    	int neutral=0;
    	detector = new SentimentDetector();
    	
    	List<Status> statuses = tweetService.getAllTweetsByUser(user);
    	TranslatorService trans = new TranslatorService();
    	String userTextTrans = null;
    	int x=0;
    	for (Status status : statuses) {
            String userText = status.getText();
            try {
            	//if (x<=3) {
				userTextTrans = trans.translateSentence(userText);
				//userTextTrans = userTextTrans.replaceAll(",", "");
				String text= pattern.matcher(userTextTrans).replaceAll("");
				score = detector.detect(text);
				sentiment = getSentiment(score);
				if (sentiment == Sentiment.POSITIVE)
					positive++;
				else if (sentiment == Sentiment.NEGATIVE)
					negative++;
				else
					neutral++;
            	//}else {
            	//	break;
            	//}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            scores.put("Sentimen POS", positive);
            scores.put("Sentimen NEG", negative);
            scores.put("Sentimen neu", neutral);
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

