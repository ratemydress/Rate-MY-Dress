package com.innovators.ratemydress;

public class ParseConstants {

	//Class Names
		public static final String CLASS_POSTS = "Posts";
		protected static final String CLASS_RATINGS = "Ratings";
		
	//Field Names
		public static final String KEY_USERNAME = "username";
		public static final String KEY_FRIENDS_RELATION = "friendsRelation";
		
		public static final String KEY_RECIPIENT_IDS = "recipientIds";
		public static final String KEY_SENDER_ID = "senderID";
		public static final String KEY_SENDER_NAME = "senderName";
		public static final String KEY_FILE = "file";
		public static final String KEY_FILE_TYPE = "fileType";
		public static final String KEY_SENDER_FB_NAME = "senderFbName";
		public static final String KEY_CREATED_AT = "createdAt";
		
		protected static final String KEY_POST_ID = "postID";
		protected static final String KEY_RATING = "rating";
		protected static final String KEY_COMMENT = "comment";
		//User who is Rating
		protected static final String KEY_USER_ID = "raterID";
		//Have to include Sender ID as well in this table to reduce queries
		//Think about it.
		
	//Miscellaneous Values
		public static final String TYPE_IMAGE = "image";
		public static final String TYPE_VIDEO = "video";
				

}
