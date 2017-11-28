/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twittertest01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;

import java.sql.Statement;
import java.sql.ResultSet;
import twitter4j.TwitterObjectFactory;
import twitter4j.conf.ConfigurationBuilder;

import configuration.ConfigurationHandler;

/**
 *
 * @author zeigel
 */
public class DBConection {

    static Connection connection;
    
    static String readTen = "select raw_tweet from json_cache limit 10";
    static String insertJsonCache = " insert into json_cache (tweet_id, raw_tweet)"
                    + " values (?, ?)";
    static String insertJsonCacheExtended = " insert into json_cache_extended "
                    +"(tweet_id, raw_tweet, followers_count, location_enabled, tweet_text, user_id)"
                    + " values (?, ?, ?, ?,?,?)";
    
    
    public static void openConnection() throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            SQLException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        
        String database = ConfigurationHandler.database;
        String databaseName = ConfigurationHandler.databaseName;
        String databaseUsername = ConfigurationHandler.databaseUsername;
        String databasePassword = ConfigurationHandler.databasePassword;
        

        connection = DriverManager.getConnection("jdbc:mysql://"+database
                +"/"+databaseName+"?"
                + "user="+ databaseUsername
                + "&password="+ databasePassword
                + "&useSSL=false");
        connection.createStatement();

    }

    public static void closeConnection() throws ClassNotFoundException,
            InstantiationException,
            IllegalAccessException,
            SQLException {
        if (connection != null) {
            connection.close();
        }

    }
    
    
    public static boolean saveRawTweet(Status tweet) {

        Statement stmt = null;
        ResultSet rs = null;
        boolean response = false;

        try {
            // create the mysql insert preparedstatement
            stmt = connection.createStatement();
            PreparedStatement preparedStmt = connection.prepareStatement(DBConection.insertJsonCache);
            //set variables
            preparedStmt.setLong(1, tweet.getId());
            String json = TwitterObjectFactory.getRawJSON(tweet);
            //System.out.println(json);
            preparedStmt.setString(2, json);

            // execute the preparedstatement
            preparedStmt.execute();

            response = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }

        return response;

    }
    
    
    
    public static boolean saveRawTweetExtended(Status tweet) {

        Statement stmt = null;
        ResultSet rs = null;
        boolean response = false;

        try {
            
            //" insert into json_cache (tweet_id, raw_tweet, followers_count, location_enabled)" " values (?, ?, ?, ?)";
            // create the mysql insert preparedstatement
            stmt = connection.createStatement();
            PreparedStatement preparedStmt = connection.prepareStatement(DBConection.insertJsonCacheExtended);
            //set variables
            preparedStmt.setLong(1, tweet.getId());
            
            String json = TwitterObjectFactory.getRawJSON(tweet);
            System.out.println(json);
            preparedStmt.setString(2, json);
            preparedStmt.setLong(3, tweet.getUser().getFollowersCount());
            preparedStmt.setBoolean(4, tweet.getGeoLocation()!=null);
            preparedStmt.setString(5, tweet.getText());
            preparedStmt.setLong(6, tweet.getUser().getId());

            // execute the preparedstatement
            preparedStmt.execute();

            response = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }

        return response;

    }
    

    
    public static boolean testSQL(){
        
        Statement stmt = null;
        ResultSet rs = null;
        boolean response = false;

        try {
            stmt = connection.createStatement();

            // the mysql insert statement
            String query = DBConection.readTen;

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = connection.prepareStatement(query);
            
            // execute the preparedstatement
            preparedStmt.execute();
            
            rs = preparedStmt.getResultSet();
            while (rs.next()) {              
                //arrayList.add(rs.getString(i++));
                //System.out.println(rs.getString("raw_tweet"));
               
                Status st = TwitterObjectFactory.createStatus(rs.getString("raw_tweet"));
                System.out.println(st.getText());
            }
            
            response = true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore

                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore

                stmt = null;
            }
        }

        return response;
        
    }

    public static void main(String[] args) {
        
        try{
            ConfigurationHandler.initialize();
            ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setJSONStoreEnabled(true);
            DBConection.openConnection();
            System.out.println(DBConection.testSQL());
            DBConection.closeConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
