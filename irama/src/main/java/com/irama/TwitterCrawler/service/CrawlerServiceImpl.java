package com.irama.TwitterCrawler.service;

import com.irama.TwitterCrawler.configuration.CrawlerConfiguration;
import org.springframework.stereotype.Service;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.util.List;

/**
 * Created by alicankustemur on 30/10/2017.
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    private final CrawlerConfiguration crawlerConfiguration;
    private Twitter twitter;

    public CrawlerServiceImpl(CrawlerConfiguration crawlerConfiguration) {
        this.crawlerConfiguration = crawlerConfiguration;
    }

    @Override
    public Twitter createTwitter() {
        ConfigurationBuilder configurationBuilder = crawlerConfiguration.getConfigurationBuilder();
        if (twitter == null) {
            twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
        }
        return twitter;
    }

    @Override
    public List<Status> getTweetsByUserAndPageNumber(String user, int pageNumber) throws TwitterException {
        Paging page = new Paging(pageNumber, 100);
        return createTwitter().getUserTimeline(user, page);
    }
    
    @Override
    public List<Status> getTweetsByQuery(Query query, int pageNumber) throws TwitterException {
        Paging page = new Paging(pageNumber, 100);
        List<Status> tweets = null;
        try {
            QueryResult result;
            do {
                result = createTwitter().search(query);
                tweets = result.getTweets();
            } while ((query = result.nextQuery()) != null);
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to search tweets: " + te.getMessage());
            System.exit(-1);
        }
        return tweets;
    }


}
