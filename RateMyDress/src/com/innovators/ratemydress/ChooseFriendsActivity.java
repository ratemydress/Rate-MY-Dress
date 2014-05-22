package com.innovators.ratemydress;

import java.util.ArrayList;
import java.util.List;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.net.Uri;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class ChooseFriendsActivity extends ListActivity {

	public static final String TAG = ChooseFriendsActivity.class.getSimpleName();
	
	protected List<ParseUser> mFriends;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	
	protected MenuItem mSendMenuItem;
	
	protected Uri mMediaUri;
	protected String mFileType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_choose_friends);
		// Show the Up button in the action bar.
		setupActionBar();
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		mMediaUri = getIntent().getData();
		mFileType = getIntent().getExtras().getString(ParseConstants.KEY_FILE_TYPE); 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
		
		setProgressBarIndeterminateVisibility(true);
		
		ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
		query.addAscendingOrder(ParseConstants.KEY_USERNAME);
		mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if(e == null){
					mFriends = friends;
					String[] usernames = new String[mFriends.size()];
					int i = 0;
					for(ParseUser user : mFriends){
						usernames[i] = user.getString("fbName");
						i++;
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(getListView().getContext(), 
											android.R.layout.simple_list_item_checked,
											usernames);
					setListAdapter(adapter);
				}else{
					Log.e(TAG, e.getMessage());
					AlertDialog.Builder builder = new AlertDialog.Builder(ChooseFriendsActivity.this);
					builder.setMessage(e.getMessage())
						.setTitle(R.string.error_title)
						.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create(); 
					dialog.show();
				}
				
			}
		});

	}
	
	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.choose_friends, menu);
		mSendMenuItem = menu.getItem(0);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		
		case R.id.action_share:
			ParseObject post = createMessage();
			if(post == null){
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(R.string.error_selecting_file)
						.setTitle(R.string.error_selecting_file_title)
						.setPositiveButton(android.R.string.ok, null);
				AlertDialog dialog = builder.create();
				dialog.show();
			}else{
				sendNotification();
				share(post);
				finish();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void sendNotification() {
	    Bundle params = new Bundle();
	    params.putString("message", "Learn how to make your Android apps social");

	    WebDialog requestsDialog = (
	        new WebDialog.RequestsDialogBuilder(this,
	            Session.getActiveSession(),
	            params))
	            .setOnCompleteListener(new OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                    FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(ChooseFriendsActivity.this, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(ChooseFriendsActivity.this, 
	                                "Network Error", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(ChooseFriendsActivity.this, 
	                                "Request sent",  
	                                Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(ChooseFriendsActivity.this, 
	                                "Request cancelled", 
	                                Toast.LENGTH_SHORT).show();
	                        }
	                    }   
	                }

	            })
	            .build();
	    requestsDialog.show();
	}

	protected void share(ParseObject message) {
		message.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if(e == null){
					//success
					Toast.makeText(ChooseFriendsActivity.this, R.string.success_message, Toast.LENGTH_LONG).show();
				}
				else{
					AlertDialog.Builder builder = new AlertDialog.Builder(ChooseFriendsActivity.this);
					builder.setMessage(R.string.error_sending_message)
							.setTitle(R.string.error_selecting_file_title)
							.setPositiveButton(android.R.string.ok, null);
					AlertDialog dialog = builder.create();
					dialog.show();
				}
			}
		});
	}

	protected ParseObject createMessage() {
		ParseObject post = new ParseObject(ParseConstants.CLASS_POSTS);
		post.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
		post.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
		post.put(ParseConstants.KEY_SENDER_FB_NAME, ParseUser.getCurrentUser().get("fbName"));
		post.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
		post.put(ParseConstants.KEY_FILE_TYPE, mFileType);
		
		byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
		if(fileBytes == null){
			return null;
		}else{
			if(mFileType.equals(ParseConstants.TYPE_IMAGE)){
				fileBytes = FileHelper.reduceImageForUpload(fileBytes);
			}
			String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
			ParseFile file = new ParseFile(fileName, fileBytes);
			post.put(ParseConstants.KEY_FILE, file);
			return post;
		}
	}
	
	protected ArrayList<String> getRecipientIds() {
		ArrayList<String> recipientIds = new ArrayList<String>();
		for(int i = 0; i<getListView().getCount();i++){
			if(getListView().isItemChecked(i)){
				recipientIds.add(mFriends.get(i).getObjectId());
			}
		}
		return recipientIds;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		if(l.getCheckedItemCount() > 0){
			mSendMenuItem.setVisible(true);
		}else{
			mSendMenuItem.setVisible(false);
		}
	}
	
}
