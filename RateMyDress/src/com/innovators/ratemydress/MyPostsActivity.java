package com.innovators.ratemydress;

import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class MyPostsActivity extends ListActivity {

	public static final String TAG = MyPostsActivity.class.getSimpleName();
	
	protected List<ParseObject> mPosts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			setContentView(R.layout.activity_my_posts);

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseObject> queryPosts = new ParseQuery<ParseObject>(ParseConstants.CLASS_POSTS);
		queryPosts.whereEqualTo(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		queryPosts.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
		queryPosts.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> posts, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if(e==null){
					//You have shared pics
					mPosts = posts;
				
					MyPostsAdapter adapter = new MyPostsAdapter(MyPostsActivity.this, mPosts);
					setListAdapter(adapter);
				}
			}
		});
		
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_posts, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		ParseObject post = mPosts.get(position);
		String fileType = post.getString(ParseConstants.KEY_FILE_TYPE);
		ParseFile file = post.getParseFile(ParseConstants.KEY_FILE);
		Uri fileUri = Uri.parse(file.getUrl());
		
		if(fileType.equals(ParseConstants.TYPE_IMAGE)){
			Intent intent = new Intent(this, ViewPostActivity.class);
			intent.setData(fileUri);
			intent.putExtra(ParseConstants.KEY_POST_ID, post.getObjectId());
			startActivity(intent);
		}
		
	}
	
}
