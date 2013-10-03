package hujo.adapter;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.Charsets;
import org.apache.s4.core.App;
import org.apache.s4.core.ProcessingElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

public class ReporterTweetSlidePE extends ProcessingElement{
	static Logger logger = LoggerFactory.getLogger(ReporterTweetSlidePE.class);
	Map<Long, TweetSlideEvent> tweets;
	boolean first = true;
	public ReporterTweetSlidePE(App app) {
		super(app);
	}
	
	public void onEvent(TweetSlideEvent event) {
		if ( first ) {
			logger.info("New ReporterTweetSlidePE key = " + getId());
			tweets = Maps.newConcurrentMap();
			first = false;
		}
		tweets.put(event.getId(), event);
	}
	
	public void onTime() {
		// Create copy
		Map<Long, TweetSlideEvent> copyTweets = Maps.newHashMap(tweets);
		logger.info("Size map = " + copyTweets.size());
		List<TweetSlideEvent> sortedTweets = Lists.newArrayList();
		for ( Map.Entry<Long, TweetSlideEvent> tweet : copyTweets.entrySet()) {
			sortedTweets.add(tweet.getValue());
		}
		logger.info("Size list = " + sortedTweets.size());
		// Sort
		Collections.sort(sortedTweets);
		// Write to outputfile
		writeOutputFile(getId() + ".tsv", sortedTweets, "\t");
		
	}
	
	public void writeOutputFile(String name, List<TweetSlideEvent> list, String separator) {
		StringBuilder sb = new StringBuilder();
		Iterator<TweetSlideEvent> iter = list.iterator();
		while ( iter.hasNext() ) {
			TweetSlideEvent entry = iter.next();
			sb.append(entry.getCreatedAt() + separator 
					+ entry.getId() + separator
					+ entry.getUserId() + separator 
					+ entry.getText() + "\n");
		}
		File file = new File(name);
		try {
			Files.write(sb.toString(), file, Charsets.UTF_8);
			logger.info("Output Wrote at:" + file.getAbsolutePath() );
		}
		catch( IOException e) {
			logger.error("Cannot write documento to file [{}]"
					, file.getAbsolutePath(), e);
		}
	}
	
	@Override
	protected void onCreate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onRemove() {
		// TODO Auto-generated method stub
		
	}
}
