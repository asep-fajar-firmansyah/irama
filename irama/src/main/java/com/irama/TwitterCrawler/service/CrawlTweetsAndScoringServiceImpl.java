package com.irama.TwitterCrawler.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.websocket.Decoder.Text;

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
    	Sentiment sentiment = null;
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
    	PrintWriter pw = null;
		try {
		    pw = new PrintWriter(new File("C:\\Users\\YOGA 520\\Documents\\GitHub\\irama\\irama\\NewData.csv"));
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "no, tweetsIN, tweetsEN, score, sentimen";
		builder.append(ColumnNamesList +"\n");
    	for (Status status : statuses) {
            String userText = status.getText();
            try {
            	if (x<100) {
            		//if (x>37) {
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
            		
				/* 
				 * Save to CSV
				 */
				
				// No need give the headers Like: id, Name on builder.append
				
				builder.append(x+";");
				builder.append(userText+";");
				builder.append(text+";");
				builder.append(score+";");
				builder.append(sentiment);
				builder.append('\n');
				
				
				 System.out.println(x + "----------" + text + "--------" + sentiment.toString() + "\n");
            		//}
            	}else {
            		break;
            	}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
           
            x++;          
        }
    	pw.write(builder.toString());
    	pw.close();
    	scores.put("Sentimen POS", positive);
        scores.put("Sentimen NEG", negative);
        scores.put("Sentimen neu", neutral);
    	System.out.println("done!");
    	return scores;
    }
    public Map<String, String> findSentimen(String sentences) throws IOException, JWNLException{
    	Sentiment sentiment = null;
    	SentimentDetector detector;
    	double score;
    	Pattern pattern;
    	pattern = Pattern.compile("((^|\\s+)(RT|rt|MT|mt|FF|ff|RLRT|rlrt|OH|oh)(\\s+|$))|([#|@]\\s*)", Pattern.CASE_INSENSITIVE);
    	Map<String, String> scores = new HashMap<String, String>();
    	detector = new SentimentDetector();
    	TranslatorService trans = new TranslatorService();
    	String textTranslation = null;
    	String sentencesEn = trans.translateSentence(sentences);
		String text= pattern.matcher(sentencesEn).replaceAll("");
		score = detector.detect(text);
		sentiment = getSentiment(score);
		scores.put("tweet", sentences);
        scores.put("translate in En", sentencesEn);
        scores.put("skor", String.valueOf(score));
        if (sentiment == Sentiment.POSITIVE)
        	scores.put("Sentimen", "POSITIVE");
		else if (sentiment == Sentiment.NEGATIVE)
			scores.put("Sentimen", "NEGATIVE");
		else
			scores.put("Sentimen", "NEUTRAL");
        
        
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

