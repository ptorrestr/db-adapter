package org.apache.s4.example.twitter;

import java.util.Date;

public class Tweet {
	final private long id;
	final private String text;
	final private Date createdAt;
	final private String inReplyToScreenName;
	final private String inReplyToStatusId;
	final private String inReplyToUserId;
	final private String place;
	final private long retweetCount;
	final private String source;
	final private String geoLocation;
	
	public static class Builder {
		final private long id;
		final private String text;
		final private Date createdAt;
		private String inReplyToScreenName;
		private String inReplyToStatusId;
		private String inReplyToUserId;
		private String place;
		private long retweetCount;
		private String source;
		private String geoLocation;
		
		public Builder(long id, String text, Date createdAt) {
			this.id = id;
			this.text = text;
			this.createdAt = createdAt;
		}
		
		public Builder inReplyScreenName(String value) {
			this.inReplyToScreenName = value;
			return this;
		}
		
		public Builder inReplyStatusId(String value) {
			this.inReplyToStatusId = value;
			return this;
		}
		
		public Builder inReplyUserId(String value) {
			this.inReplyToUserId = value;
			return this;
		}
		
		public Builder place(String value) {
			this.place = value;
			return this;
		}
		
		public Builder retweetCount(long value) {
			this.retweetCount = value;
			return this;
		}
		
		public Builder source(String value) {
			this.source = value;
			return this;
		}
		
		public Builder geoLocation(String value) {
			this.geoLocation = value;
			return this;
		}
		
		public Tweet build() {
			return new Tweet(this);
		}
		
	}
	
	private Tweet(Builder builder) {
		id = builder.id;
		text = builder.text;
		createdAt = builder.createdAt;
		inReplyToScreenName = builder.inReplyToScreenName;
		inReplyToStatusId = builder.inReplyToStatusId;
		inReplyToUserId = builder.inReplyToUserId;
		place = builder.place;
		retweetCount = builder.retweetCount;
		source = builder.source;
		geoLocation = builder.geoLocation;
	}
	
	public long getId() { return id; }
	public String getText() { return text; }
	public Date getCreatedAt() { return createdAt; }
	public String getInReplyToScreenName() { return inReplyToScreenName; }
	public String getInReplyToStatusId() { return inReplyToStatusId; }
	public String getInReplyToUserId() { return inReplyToUserId; }
	public String getPlace() { return place; }
	public long getRetweetCount() { return retweetCount; }
	public String getSource() {return source; }
	public String getGeoLocation() { return geoLocation; }
	
	public String toString() {
		return "Tw = { id : " + id + ", te : " + text + ", cr : " + createdAt + "}";
	}
}
