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
                .setOAuthConsumerKey("z6o0LjqUZ98ybdCn0iNl730dU")
                .setOAuthConsumerSecret("J5BwuC44n8WlglrI8cFSS1buJR6Wia6Z8SEDEKOB33fpQxYVR4")
                .setOAuthAccessToken("3043604459-EFULmG2HmsrT8rILQolRiF1YB7jKSuVSRKEskBi")
                .setOAuthAccessTokenSecret("c2PmC9hJ6HlKEo4ijC2YZrN0qAMscjl6LVhP6mae7weyN");
    }

}
