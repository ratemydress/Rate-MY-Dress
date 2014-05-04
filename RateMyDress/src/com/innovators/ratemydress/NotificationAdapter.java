package com.innovators.ratemydress;

import java.util.List;

import com.parse.ParseObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<ParseObject>{

	protected Context mContext;
	protected List<ParseObject> mNotifications;
	
	public NotificationAdapter(Context context, List<ParseObject> notifications) {
		super(context, R.layout.notification_item, notifications);
		mContext = context;
		mNotifications = notifications;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null){
			//inflate view is used to convert a layout xml to a view
			//This will get added to the list view
			convertView = LayoutInflater.from(mContext).inflate(R.layout.notification_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.notificationIcon);
			holder.nameLabel = (TextView)convertView.findViewById(R.id.senderLabel);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		ParseObject post = mNotifications.get(position);
		if(post.getString(ParseConstants.KEY_FILE_TYPE).equals(ParseConstants.TYPE_IMAGE)){
			holder.iconImageView.setImageResource(R.drawable.ic_action_picture);
		}
		holder.nameLabel.setText(post.getString(ParseConstants.KEY_SENDER_FB_NAME));
		
		return convertView;
	}
	
	private static class ViewHolder {
		ImageView iconImageView;
		TextView nameLabel;
	}

}
