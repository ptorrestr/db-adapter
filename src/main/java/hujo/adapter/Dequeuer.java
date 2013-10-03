package hujo.adapter;

import java.text.SimpleDateFormat;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Dequeuer implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(Dequeuer.class);
	private LinkedBlockingQueue<Tweet> messageQueue;
	private AdapterApp myAdapter;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
	
	public Dequeuer(LinkedBlockingQueue<Tweet> messageQueue, AdapterApp adapter) {
		this.messageQueue = messageQueue;
		this.myAdapter = adapter;
	}

    @Override
    public void run() {
        while (true) {
            try {
                Tweet status = messageQueue.take();
                // Tweet text
                Event event = new Event();
                
                //Get ID
                event.put("statusId", Long.class, status.getId());
                
                //Get text
                event.put("statusText", String.class, status.getText());
                
                //Get time
                String sCertDate = dateFormat.format(status.getCreatedAt());
                event.put("statusCreatedAt", String.class, sCertDate);
                
                //Get user id
                event.put("statusUserId", Long.class, status.getUserId());

                myAdapter.getRemoteStream().put(event);
            } catch (Exception e) {
            	logger.error("Error while putting tweets in ouputstream", e);
            }
        }

    }
}