package com.innovators.ratemydress;
import com.parse.Parse;

import android.app.Application;

public class RateMyDressApplication extends Application{

	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, applicationId, clientKey);
	}
	
}
