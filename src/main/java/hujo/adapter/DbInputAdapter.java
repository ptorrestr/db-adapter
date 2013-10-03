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

package hujo.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.s4.core.adapter.AdapterApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbInputAdapter extends AdapterApp {

    private static Logger logger = LoggerFactory.getLogger(
    		DbInputAdapter.class);
    private LinkedBlockingQueue<Tweet> messageQueue = 
    		new LinkedBlockingQueue<Tweet>();
    private Thread t;
    
    public DbInputAdapter() {
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
        	.dataBaseFile(adapterProperties.getProperty("dataBaseFile"))
        	.dataBaseDriver(adapterProperties.getProperty("dataBaseDriver"))
        	.dataBaseName(adapterProperties.getProperty("dataBaseName"))
        	.dataBaseClass(adapterProperties.getProperty("dataBaseClass"))
        	.dataBaseQuerySelectIds(adapterProperties.getProperty("dataBaseQuerySelectIds"))
    		.dataBaseQuerySelectTweet(adapterProperties.getProperty("dataBaseQuerySelectTweet"))
    	    .dataBaseIdField(adapterProperties.getProperty("dataBaseIdField"))
    		.dataBaseTextField(adapterProperties.getProperty("dataBaseTextField"))
    	    .dataBaseCreatedAtField(adapterProperties.getProperty("dataBaseCreatedAtField"))
    	    .dataBaseUserIdField(adapterProperties.getProperty("dataBaseUserIdField"))
    	    .dataBaseDateFormat(adapterProperties.getProperty("dataBaseDateFormat"))
        	.build();
        // Initialize objects
    	//Class.forName("org.sqlite.JDBC");
    	Class.forName(config.getDataBaseClass());
        Connection connection = DriverManager.getConnection(
        		//"jdbc:sqlite:/home/pablo/data/twitter/fsd_edinburgh_corpus/fsd.db"
        		config.getDataBaseDriver()
        		+ ":" + config.getDataBaseName()
        		+ ":" + config.getDataBaseFile()
        		);
        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        logger.info("Connecting to SQL");
        // Read first all the ids
        ResultSet rs = statement.executeQuery(config.getDataBaseQuerySelectIds());
        List<Long> idTweets = new ArrayList<Long>(); 
        while(rs.next()) {
        	idTweets.add(rs.getLong(config.getDataBaseIdField()));
        }
        logger.info("Total tweets = " + idTweets.size());
        for (long k : idTweets) {
        	ResultSet res = statement.executeQuery(config.getDataBaseQuerySelectTweet() + k);
        	while(res.next()) {
        		String text = res.getString(config.getDataBaseTextField());
        		String stringDate = res.getString(config.getDataBaseCreatedAtField());
        		Date date = new SimpleDateFormat(config.getDataBaseDateFormat(), Locale.ENGLISH).parse(stringDate);
        		long userId = res.getLong(config.getDataBaseUserIdField());
        		Tweet t = new Tweet.Builder(k, text, date, userId).build();
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
}
