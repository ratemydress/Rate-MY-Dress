package com.innovators.ratemydress;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ViewNotificationActivity extends Activity implements OnClickListener {

	protected Button mRatingButton;
	protected Button mDiscardButton;
	
	protected String mPostId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_notification);
		
		ImageView imageView = (ImageView)findViewById(R.id.imageView);
		
		Uri imageUri = getIntent().getData();
		mPostId = getIntent().getExtras().getString(ParseConstants.KEY_POST_ID);
		
		Picasso.with(this).load(imageUri.toString()).into(imageView);
		
		mRatingButton = (Button) findViewById(R.id.ratingButton);
		mRatingButton.setOnClickListener(this);
		
		mDiscardButton = (Button) findViewById(R.id.discardButton);
		mDiscardButton.setOnClickListener(this);
		
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
	public void onClick(View view) {
		if(view.getId() == R.id.ratingButton){
			//Display the screen to add rating and comments
//			Intent ratingIntent = new Intent(this, RatingActivity.class);
//			startActivity(ratingIntent);
			navigateToRatingDialog();
			
		}else if(view.getId() == R.id.discardButton){
			//Discard the entry from notification list
			
			finish();
		}
	}

	private void navigateToRatingDialog() {
		//Create a Dialog to Show the Rating Screen
        Dialog ratingDialog = new Dialog(this, R.style.FullHeightDialog);
        ratingDialog.setContentView(R.layout.activity_rating);
        ratingDialog.setCancelable(true);
        final RatingBar ratingBar = (RatingBar)ratingDialog.findViewById(R.id.ratingbar);
        ratingBar.setRating(2);

        final TextView comment = (TextView) ratingDialog.findViewById(R.id.comment);
        comment.setHint("Add Comments Here..");

        Button updateButton = (Button) ratingDialog.findViewById(R.id.ratingButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	//update the rating in the database
            	//Create an object of type ParseRating to store comment, rating and postID
            	ParseObject rating = createRating();
            	if(rating == null){
            		AlertDialog.Builder builder = new AlertDialog.Builder(ViewNotificationActivity.this);
        				builder.setMessage(R.string.error_update_rating)
        				.setTitle(R.string.error_update_rating_title)
        				.setPositiveButton(android.R.string.ok, null);
        			AlertDialog dialog = builder.create();
        			dialog.show();
        		}else{
        			//save the created Parse Rating Object
        			saveRating(rating);
        			finish();
        		}
            	finish();
            }

			private ParseObject createRating() { 
				ParseObject rating = new ParseObject(ParseConstants.CLASS_RATINGS);
				rating.put(ParseConstants.KEY_POST_ID, mPostId);
				rating.put(ParseConstants.KEY_RATING, ratingBar.getRating());
				rating.put(ParseConstants.KEY_COMMENT, comment.getText().toString());
				rating.put(ParseConstants.KEY_USER_ID, ParseUser.getCurrentUser());
				return rating;
			}
			
			private void saveRating(ParseObject rating) {
				rating.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						if(e == null){
							//Success: Rating is saved
							Toast.makeText(ViewNotificationActivity.this, R.string.success_rating_update, Toast.LENGTH_LONG);
							//Remove the user from the list of recipients
							
						}else{
							//Some Exception while saving the image
							AlertDialog.Builder builder = new AlertDialog.Builder(ViewNotificationActivity.this);
							builder.setMessage(R.string.error_save_rating)
								   .setTitle(R.string.general_error)
								   .setPositiveButton(android.R.string.ok, null);
							AlertDialog dialog = builder.create();
							dialog.show();
						}
					}
				});
			}
			
        });
        //show the prepared dialog
        ratingDialog.show();                
    }

}
