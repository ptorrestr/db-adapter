package hujo.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.s4.core.adapter.AdapterApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterInputAdapter extends AdapterApp {
    private static Logger logger = LoggerFactory.getLogger(TwitterInputAdapter.class);
    private LinkedBlockingQueue<Tweet> messageQueue = new LinkedBlockingQueue<Tweet>();
    protected ServerSocket serverSocket;
    private Thread t;
    
    public TwitterInputAdapter() {
    }

    @Override
    protected void onClose() {
    }

    @Override
    protected void onInit() {
        super.onInit();
        t = new Thread(new Dequeuer(messageQueue, this));
    }

    public void connectAndRead() throws Exception {

    	// Load properties file
        Properties adapterProperties = new Properties();
        File adapterPropsFile = new File(System.getProperty("user.home") 
        		+ "/data/adapter.properties");
        if (!adapterPropsFile.exists()) {
            logger.error( "Cannot find twitter4j.properties file in this "
            		+ "location :[{}]. Make sure it is available at this "
            		+ "place and includes neccesary data",
            		adapterPropsFile.getAbsolutePath());
            return;
        }
        // Load properties into configuration object
        adapterProperties.load(new FileInputStream(adapterPropsFile));
        Configuration config = new Configuration.Builder()
        	.twitterUser(adapterProperties.getProperty("twitterUser"))
        	.twitterPass(adapterProperties.getProperty("twitterPass"))
        	.twitterDebug(adapterProperties.getProperty("twitterDebug"))
        	.twitterConsumerKey(adapterProperties.getProperty("twitterConsumerKey"))
        	.twitterConsumerSecret(adapterProperties.getProperty("twitterConsumerSecret"))
        	.twitterAccessKey(adapterProperties.getProperty("twitterAccessKey"))
        	.twitterAccessSecret(adapterProperties.getProperty("twitterAccessSecret"))
        	.build();
        // Initialize objects
        ConfigurationBuilder cb = new ConfigurationBuilder()
        	.setDebugEnabled(Boolean.valueOf(config.getTwitterDebug()))
            .setUser(config.getTwitterUser())
            .setPassword(config.getTwitterPass())
            .setOAuthConsumerKey(config.getTwitterConsumerKey())
            .setOAuthConsumerSecret(config.getTwitterConsumerSecret())
            .setOAuthAccessToken(config.getTwitterAccessKey())
            .setOAuthAccessTokenSecret(config.getTwitterAccessSecret());
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        StatusListener statusListener = new StatusListener() {

            @Override
            public void onException(Exception ex) {
                logger.error("error", ex);
            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                logger.error("error");
            }

            @Override
            public void onStatus(Status status) {
            	Tweet tweet = new Tweet.Builder(status.getId(), status.getText()
            			, status.getCreatedAt(), status.getUser().getId()).build();
                messageQueue.add(tweet);

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
                logger.error("error");
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                logger.error("error");
            }
        };
        twitterStream.addListener(statusListener);
        twitterStream.sample();

    }

    @Override
    protected void onStart() {
        try {
            t.start();
            connectAndRead();
        } catch (Exception e) {
        	logger.error("Failed" , e);
            throw new RuntimeException(e);
        }
    }
}
