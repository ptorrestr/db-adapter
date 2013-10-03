package hujo.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.s4.base.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetSlideEvent extends Event implements Comparable<TweetSlideEvent>{
	static Logger logger = LoggerFactory.getLogger(TweetSlideEvent.class);
	private String slide;
	private String createdAt; // S4 cannot parse Date datatype
	private long id;
	private long userId;
	private String text;
	
	public TweetSlideEvent() {
	}
	
	public TweetSlideEvent(String slide, Tweet tweet) {
		this.slide = slide.replace(" ", "_");
		this.createdAt = tweet.getCreatedAt().toString();
		this.id = tweet.getId();
		this.userId = tweet.getUserId();
		this.text = tweet.getText().replaceAll("\\s+", " ");
	}
	
	public String getSlide() { return slide; }
	public String getCreatedAt() { return createdAt; }
	public long getId() { return id; }
	public long getUserId() { return userId; }
	public String getText() { return text; }
	
	public String toString() {
		return "TSE: { sl = " + slide 
				+ " , cr = " + createdAt
				+ " , id = " + id
				+ " , us = " + userId
				+ " , te = " + text
				+ " }";
	}
	
	@Override
	public int compareTo(TweetSlideEvent lEntry) {
		try {
			Date dateLEntry = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(lEntry.createdAt);
			Date dateREntry = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(createdAt);
		    if ( dateLEntry.before(dateREntry)) {
		        return -1;
		    } else if (dateLEntry.after(dateREntry)) {
		        return 1;
		    } else {
		    	if ( lEntry.id < id ) {
		    		return -1;
		    	} else if ( lEntry.id > id ) {
		    		return 1;
		    	} else {
		    		if ( lEntry.userId < userId ) {
		    			return -1;
		    		} else if ( lEntry.userId > userId ) {
		    			return 1;
		    		}
		    	}
		    }
		} catch (ParseException e) {
			logger.error("Error when comparing",e);
        }
        return 0;
    }
}
