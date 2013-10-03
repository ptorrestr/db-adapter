package hujo.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateUtils;
import org.apache.s4.base.Event;
import org.apache.s4.core.App;
import org.apache.s4.core.ProcessingElement;
import org.apache.s4.core.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TweetExtractorPE extends ProcessingElement {
	static Logger logger = LoggerFactory.getLogger(TweetExtractorPE.class);
    Stream<TweetSlideEvent> downStream;

    public TweetExtractorPE(App app) {
        super(app);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate() {

    }

    public void setDownStream(Stream<TweetSlideEvent> stream) {
        this.downStream = stream;
    }
    
    // Round the time to the slide time (30 mins)
    public Date roundSlide(Date date) {
        //Date aproxDate = DateUtils.round(date, Calendar.MINUTE);
    	Date aproxDate = DateUtils.truncate(date, Calendar.MINUTE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(aproxDate);
        int unRoundedMinutes = calendar.get(Calendar.MINUTE);
        int mod = unRoundedMinutes % 30;
        calendar.set(Calendar.MINUTE, unRoundedMinutes - mod);
        return calendar.getTime();
    }

    public void onEvent(Event event) {
    	long id = event.get("statusId", Long.class);
    	String text = event.get("statusText", String.class);
    	String createdAtStr = event.get("statusCreatedAt", String.class);
    	Date createdAt;
        try {
        	createdAt = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(createdAtStr);
        } catch (ParseException e) {
            logger.error("Could not parse date", e);
            return;
        }
    	long userId = event.get("statusUserId", Long.class);
        Tweet tweet = new Tweet.Builder(id, text, createdAt, userId).build();
        
    	String slideId = roundSlide(createdAt).toString();
    	downStream.put(new TweetSlideEvent(slideId, tweet));	
    }

    @Override
    protected void onRemove() {
        // TODO Auto-generated method stub

    }

}