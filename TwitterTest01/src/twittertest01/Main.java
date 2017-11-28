/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittertest01;

import configuration.ConfigurationHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterResponse;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author zeigel
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, TwitterException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        // TODO code application logic here
        
        
        ConfigurationHandler.initialize();
        
        String consumerKey = ConfigurationHandler.consumerKey;
        String consumerSecret = ConfigurationHandler.consumerSecret; 
        String accessToken = ConfigurationHandler.accessToken;
        String accessTokenSecret = ConfigurationHandler.accessTokenSecret;

        //Instantiate a re-usable and thread-safe factory
        ConfigurationBuilder cb = new ConfigurationBuilder();
        
        cb.setJSONStoreEnabled(true);

        //Instantiate a new Twitter instance
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        
        
        //setup OAuth Consumer Credentials
        twitter.setOAuthConsumer(consumerKey, consumerSecret);

        //setup OAuth Access Token
        twitter.setOAuthAccessToken(new AccessToken(accessToken, accessTokenSecret));

        twitter.getAPIConfiguration();
        

        String screenName = twitter.getScreenName();
        System.out.println(screenName);

        /*
        ResponseList<Status> timeline = twitter.getUserTimeline("alination");

        //twitter.search(new Query("{}"));
        for (Status status : timeline) {
            System.out.println(status.getText());
        }
        
        
        List<Status> statuses = twitter.getMentionsTimeline();
        
        
         */
        //System.out.println("Showing @alination's mentions.");
        
        
        /*try {
        
        DBConection.openConnection();
        
        Query nq = new Query("@SSP_CDMX");
        nq.setCount(100);
        
        QueryResult qr = twitter.search(nq);
        
        for (Status status : qr.getTweets()) {
        System.out.println("@" + status.getUser().getScreenName() + " - " + status.getText());
        boolean res = DBConection.saveRawTweet(status);
        System.out.println(res);
        }
        
        } catch (Exception e) {
        try {
        DBConection.closeConnection();
        } catch (ClassNotFoundException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        }*/

        Query query = new Query("@PoliciaFedMx");
        //Query query = new Query("@SSP_CDMX");
        //Query query = new Query("@PGJDF_CDMX");

        
        
        DBConection.openConnection();
        
        int numberOfTweets = 500;
        long lastID = Long.MAX_VALUE;
        ArrayList<Status> tweets = new ArrayList<Status>();
        while (tweets.size() < numberOfTweets) {
            
            if (numberOfTweets - tweets.size() > 100) {
                query.setCount(100);
            } else {
                query.setCount(numberOfTweets - tweets.size());
            }
            
            
            
            try {
                QueryResult result = twitter.search(query);
                tweets.addAll(result.getTweets());
                System.out.println("has next"+ result.hasNext());
                System.out.println("nextQUery"+ result.nextQuery());
                
                System.out.println("Gathered " + tweets.size() + " tweets");
                for (Status t : tweets) {
                    //boolean res = DBConection.saveRawTweet(t);
                    boolean res = DBConection.saveRawTweetExtended(t);
                    
                    System.out.println(t.getText() + "::" + res + "" + t.getGeoLocation());
                    if (t.getId() < lastID) {
                        lastID = t.getId();
                    }
                }

            } catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
            };
            query.setMaxId(lastID - 1);
            
            
            
        }



        DBConection.closeConnection();

        //System.out.println("Successfully updated the status to [" + status.getText() + "].");
    }

}
