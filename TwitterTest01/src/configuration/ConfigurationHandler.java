/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package configuration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 *
 * @author zeigel
 */
public class ConfigurationHandler {
    
    public static String database;
    public static String databaseName;
    public static String databaseUsername;
    public static String databasePassword;
    public static String consumerKey;
    public static String consumerSecret;
    public static String accessToken;
    public static String accessTokenSecret;
    
    public static void initialize() {

        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("database"));
            System.out.println(prop.getProperty("dbuser"));
            System.out.println(prop.getProperty("dbpassword"));
            
            ConfigurationHandler.consumerKey = prop.getProperty("consumer-key");
            ConfigurationHandler.consumerSecret = prop.getProperty("consumer-secret");
            ConfigurationHandler.accessToken = prop.getProperty("access-token");
            ConfigurationHandler.accessTokenSecret = prop.getProperty("access-token-secret");
            ConfigurationHandler.database = prop.getProperty("database");
            ConfigurationHandler.databaseName = prop.getProperty("database-name");
            ConfigurationHandler.databaseUsername = prop.getProperty("db-user");
            ConfigurationHandler.databasePassword = prop.getProperty("db-password");
            
            
            

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    

}
