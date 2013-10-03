package hujo.adapter;

import java.util.Date;

public class Tweet {
	private final long id;
	private final String text;
	private final Date createdAt;
	private final long userId;
	private final String inReplyToScreenName;
	private final String inReplyToStatusId;
	private final String inReplyToUserId;
	private final String place;
	private final long retweetCount;
	private final String source;
	private final String geoLocation;
	
	public static class Builder {
		private final long id;
		private final String text;
		private final Date createdAt;
		private final long userId;
		private String inReplyToScreenName;
		private String inReplyToStatusId;
		private String inReplyToUserId;
		private String place;
		private long retweetCount;
		private String source;
		private String geoLocation;
		
		public Builder(long id, String text, Date createdAt, long userId) {
			this.id = id;
			this.text = text;
			this.createdAt = createdAt;
			this.userId = userId;
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
		userId = builder.userId;
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
	public long getUserId() { return userId; }
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
