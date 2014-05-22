package com.innovators.ratemydress;

import com.parse.Parse;

import com.parse.ParseFacebookUtils;

import android.app.Application;



public class RateMyDressApplication extends Application{

	public void onCreate() {
		super.onCreate();
		
		Parse.initialize(this, "y6m79FAh0jqAhVEroK1ssnH892kkH8Sml8I8wwAZ", 
				"eZzxvluDFDOvQ4rOS0fHktSpQ0UnkIzrFZ0LJqWl");
		
		ParseFacebookUtils.initialize(getString(R.string.fb_app_id));
	}
		
}
