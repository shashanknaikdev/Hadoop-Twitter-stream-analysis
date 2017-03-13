import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;
 
public class TwitterStreamMain {
 
    private TwitterStream tweetStream;
    private String[] keywords;
    private final String outputFile="D:\\Eclipse\\twitterfeed\\twitterstream.json";
    Properties propertiesFile = new Properties();
    FileOutputStream outputStream;
 
    public TwitterStreamMain() {
         try {
            propertiesFile.load(new FileInputStream("twitterProp.properties")); //get properties from the twitterProp file
 
            ConfigurationBuilder conBuild = new ConfigurationBuilder();
            conBuild.setOAuthConsumerKey(propertiesFile.getProperty("CONSUMER_KEY"));
            conBuild.setOAuthConsumerSecret(propertiesFile.getProperty("CONSUMER_SECRET"));
            conBuild.setOAuthAccessToken(propertiesFile.getProperty("ACCESS_TOKEN"));
            conBuild.setOAuthAccessTokenSecret(propertiesFile.getProperty("ACCESS_TOKEN_SECRET"));
            conBuild.setJSONStoreEnabled(true);
            conBuild.setIncludeEntitiesEnabled(true);
 
            tweetStream = new TwitterStreamFactory(conBuild.build()).getInstance(); //create TwitterStream obj
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 
    }
    
    StatusListener listener = new StatusListener() {  				//new StatusListener obj inline def
    	
       public void onStatus(Status status) {
            System.out.println(status.getUser().getScreenName() + ": " + status.getText());
 
            System.out.println("Creation time : "+ String.valueOf(status.getCreatedAt().getTime()));
            try {
            	outputStream.write(DataObjectFactory.getRawJSON(status).getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
         }

		@Override
		public void onException(Exception arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDeletionNotice(StatusDeletionNotice arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScrubGeo(long arg0, long arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStallWarning(StallWarning arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTrackLimitationNotice(int arg0) {
			// TODO Auto-generated method stub
			
		}
    };
    public void startTwitterFeed() {
 
        try {
        	outputStream = new FileOutputStream(new File(outputFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        tweetStream.addListener(listener);
        System.out.println("Starting Twitter feed:");
        tweetStream.sample();
    }
 
    public void stopTwitterFeed() {
        System.out.println("Stopping Twitter feed:");
        tweetStream.shutdown();
         try {
        	outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static void main(String[] args) throws InterruptedException {
 
        TwitterStreamMain twitter = new TwitterStreamMain();
        twitter.startTwitterFeed();
        Thread.sleep(10000);		//Get Twitter feed for 10 seconds then stop
        twitter.stopTwitterFeed();
 
    }
 
}