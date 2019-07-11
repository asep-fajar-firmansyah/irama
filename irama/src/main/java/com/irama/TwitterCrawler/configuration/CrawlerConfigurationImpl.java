package com.irama.TwitterCrawler.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by alicankustemur on 29/10/2017.
 * Modifier by Asep Fajar FIrmansyah
 */
@Configuration
public class CrawlerConfigurationImpl implements CrawlerConfiguration {

    private final Environment environment;

    public CrawlerConfigurationImpl(Environment environment) {
        this.environment = environment;
    }

    public ConfigurationBuilder getConfigurationBuilder() {
        return new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey("YOUR_CONSUMER_KEY")
                .setOAuthConsumerSecret("YOUR_CONSUMER_SECRET")
                .setOAuthAccessToken("YOUR_ACCESS_TOKEN")
                .setOAuthAccessTokenSecret("YOUR_ACCESS_TOKEN_SECRET");
    }

}
