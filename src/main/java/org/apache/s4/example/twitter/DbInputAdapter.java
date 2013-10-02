/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.s4.example.twitter;

import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.s4.base.Event;
import org.apache.s4.core.adapter.AdapterApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbInputAdapter extends AdapterApp {

    private static Logger logger = LoggerFactory.getLogger(
    		DbInputAdapter.class);

    public DbInputAdapter() {
    }

    private LinkedBlockingQueue<Tweet> messageQueue = 
    		new LinkedBlockingQueue<Tweet>();

    protected ServerSocket serverSocket;

    private Thread t;

    @Override
    protected void onClose() {
    }

    @Override
    protected void onInit() {
        super.onInit();
        t = new Thread(new Dequeuer());
    }

    public void connectAndRead() throws Exception {
        /*Properties twitterProperties = new Properties();
        File twitter4jPropsFile = new File(System.getProperty("user.home") 
        		+ "/twitter4j.properties");*/
    	Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:/home/pablo/data/twitter/fsd_edinburgh_corpus/fsd.db");
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        logger.info("Connecting to SQL");
        ResultSet rs = statement.executeQuery("select id from tweets where user_id <> -1");
        logger.info("Get first data");
        List<Long> idTweets = new ArrayList<Long>(); 
        while(rs.next()) {
        	idTweets.add(rs.getLong("id"));
        }
        logger.info("get next data, total tweets = " + idTweets.size());
        for (long k : idTweets) {
        	ResultSet res = statement.executeQuery("select * from tweets where id == " + k);
        	while(res.next()) {
        		String text = res.getString("text");
        		String stringDate = res.getString("created_at");
        		Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH).parse(stringDate);
        		Tweet t = new Tweet.Builder(k, text, date).build();
        		logger.info("New tweet = " + t.toString());
        		messageQueue.add(t);
        	}
        }
        logger.info("Finished");
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

    class Dequeuer implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Tweet status = messageQueue.take();
                    // Tweet text
                    Event event = new Event();
                    
                    //Get ID
                    event.put("statusId", String.class, String.valueOf(status.getId()));
                    
                    //Get In reply to screen name
                    event.put("statusInReplyScreenName", String.class, status.getInReplyToScreenName());
                    
                    //Get in reply to status Id
                    event.put("statusInReplyStatusId", String.class, String.valueOf(status.getInReplyToStatusId()));
                    
                    //Get in reply to user Id
                    event.put("statusInRepluUserId", String.class, String.valueOf(status.getInReplyToUserId()));
                    
                    //Get text
                    event.put("statusText", String.class, status.getText());
                    
                    //Get time
                    logger.info("date dir = " + status.getCreatedAt().toString());
                    event.put("statusDate", String.class, status.getCreatedAt().toString());
                    
                    //Get place location
                    event.put("statusPlace", String.class, status.getPlace());
                    
                    //Get re tweet count
                    event.put("statusRetweetCount", String.class, String.valueOf(status.getRetweetCount()));
                    
                    //Get source
                    event.put("statusSource", String.class, status.getSource());
                    
                    //Get geographical location
                    event.put("statusGeo", String.class, status.getGeoLocation());
                    logger.info("Puting = "  + status.getId());
                    getRemoteStream().put(event);
                } catch (Exception e) {
                	logger.error("Error while putting tweets in ouputstream", e);
                }
            }

        }
    }
}
