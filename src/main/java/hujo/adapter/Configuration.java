package hujo.adapter;

public class Configuration {
	private final String dataBaseFile;
	private final String dataBaseDriver;
	private final String dataBaseName;
	private final String dataBaseClass;
	private final String dataBaseQuerySelectIds;
	private final String dataBaseQuerySelectTweet;
    private final String dataBaseIdField;
	private final String dataBaseTextField;
    private final String dataBaseCreatedAtField;
    private final String dataBaseUserIdField;
    private final String dataBaseDateFormat;
	
	private final String twitterUser;
	private final String twitterPass;
	private final String twitterDebug;
	private final String twitterConsumerKey;
	private final String twitterConsumerSecret;
	private final String twitterAccessKey;
	private final String twitterAccessSecret;
	
	public static class Builder {
		private String dataBaseFile;
		private String dataBaseDriver;
		private String dataBaseName;
		private String dataBaseClass;
		private String dataBaseQuerySelectIds;
		private String dataBaseQuerySelectTweet;
	    private String dataBaseIdField;
		private String dataBaseTextField;
	    private String dataBaseCreatedAtField;
	    private String dataBaseUserIdField;
	    private String dataBaseDateFormat;
	    
		private String twitterUser;
		private String twitterPass;
		private String twitterDebug;
		private String twitterConsumerKey;
		private String twitterConsumerSecret;
		private String twitterAccessKey;
		private String twitterAccessSecret;
		
		public Builder() {
		}
		
		public Builder dataBaseFile(String value) {
			dataBaseFile = value;
			return this;
		}
		
		public Builder dataBaseDriver(String value) {
			dataBaseDriver = value;
			return this;
		}
		
		public Builder dataBaseName(String value) {
			dataBaseName = value;
			return this;
		}
		
		public Builder dataBaseClass(String value) {
			dataBaseClass = value;
			return this;
		}
		
		public Builder dataBaseQuerySelectIds(String value) {
			dataBaseQuerySelectIds = value;
			return this;
		}
		
		public Builder dataBaseQuerySelectTweet(String value) {
			dataBaseQuerySelectTweet = value;
			return this;
		}
		
	    public Builder dataBaseIdField(String value) {
	    	dataBaseIdField = value;
	    	return this;
	    }
	    
		public Builder dataBaseTextField(String value) {
			dataBaseTextField = value;
			return this;
		}
		
	    public Builder dataBaseCreatedAtField(String value) {
	    	dataBaseCreatedAtField = value;
	    	return this;
	    }
	    
	    public Builder dataBaseUserIdField(String value) {
	    	dataBaseUserIdField = value;
	    	return this;
	    }
	    
	    public Builder dataBaseDateFormat(String value) {
	    	dataBaseDateFormat = value;
	    	return this;
	    }
		
		public Builder twitterUser(String value) {
			twitterUser = value;
			return this;
		}
		
		public Builder twitterPass(String value) {
			twitterPass = value;
			return this;
		}
		
		public Builder twitterDebug(String value) {
			twitterDebug = value;
			return this;
		}
		
		public Builder twitterConsumerKey(String value) {
			twitterConsumerKey = value;
			return this;
		}
		
		public Builder twitterConsumerSecret(String value) {
			twitterConsumerSecret = value;
			return this;
		}
		
		public Builder twitterAccessKey(String value) {
			twitterAccessKey = value;
			return this;
		}
		
		public Builder twitterAccessSecret(String value) {
			twitterAccessSecret = value;
			return this;
		}
		
		public Configuration build() {
			return new Configuration(this);
		}
	}
	
	private Configuration(Builder builder) {
		dataBaseFile = builder.dataBaseFile;
		dataBaseDriver = builder.dataBaseDriver;
		dataBaseName = builder.dataBaseName;
		dataBaseClass = builder.dataBaseClass;
	    dataBaseQuerySelectIds = builder.dataBaseQuerySelectIds;
		dataBaseQuerySelectTweet = builder.dataBaseQuerySelectTweet;
	    dataBaseIdField = builder.dataBaseIdField;
		dataBaseTextField = builder.dataBaseTextField;
	    dataBaseCreatedAtField = builder.dataBaseCreatedAtField;
	    dataBaseUserIdField = builder.dataBaseUserIdField;
	    dataBaseDateFormat = builder.dataBaseDateFormat;
	    
		twitterUser = builder.twitterUser;
		twitterPass = builder.twitterPass;
		twitterDebug = builder.twitterDebug;
		twitterConsumerKey = builder.twitterConsumerKey;
		twitterConsumerSecret = builder.twitterConsumerSecret;
		twitterAccessKey = builder.twitterAccessKey;
		twitterAccessSecret = builder.twitterAccessSecret;
	}
	
	public String getDataBaseFile() { return dataBaseFile; }
	public String getDataBaseDriver() { return dataBaseDriver; }
	public String getDataBaseName() { return dataBaseName; }
	public String getDataBaseClass() { return dataBaseClass; }
	public String getDataBaseQuerySelectIds() { return dataBaseQuerySelectIds; }
	public String getDataBaseQuerySelectTweet() { return dataBaseQuerySelectTweet; }
	public String getDataBaseIdField() { return dataBaseIdField; }
    public String getDataBaseTextField() { return dataBaseTextField; }
    public String getDataBaseCreatedAtField() { return dataBaseCreatedAtField; }
    public String getDataBaseUserIdField() { return dataBaseUserIdField; }
    public String getDataBaseDateFormat() { return dataBaseDateFormat; }
    
	public String getTwitterUser() { return twitterUser; }
	public String getTwitterPass() { return twitterPass; }
	public String getTwitterDebug() { return twitterDebug; }
	public String getTwitterConsumerKey() { return twitterConsumerKey; }
	public String getTwitterConsumerSecret() { return twitterConsumerSecret; }
	public String getTwitterAccessKey() { return twitterAccessKey; }
	public String getTwitterAccessSecret() { return twitterAccessSecret; }
}
