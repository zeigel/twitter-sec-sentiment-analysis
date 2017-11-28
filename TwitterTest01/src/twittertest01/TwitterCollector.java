/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittertest01;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import configuration.ConfigurationHandler;
/**
 *
 * @author zeigel
 */
public class TwitterCollector {

    ConfigurationBuilder cb = new ConfigurationBuilder();

    TwitterCollector() {

        cb.setJSONStoreEnabled(true);

        Twitter twitter = new TwitterFactory(cb.build()).getInstance();

        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(ConfigurationHandler.consumerKey, ConfigurationHandler.consumerSecret);

        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(ConfigurationHandler.accessToken, ConfigurationHandler.accessTokenSecret));
        try {
            twitter.getAPIConfiguration();
            String screenName = twitter.getScreenName();
            System.out.println(screenName);
        } catch (TwitterException te) {
            System.out.println("Error:" + te.getErrorMessage());
            te.printStackTrace();
        }
        
        
    }

}
