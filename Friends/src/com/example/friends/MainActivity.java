package com.example.friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

import com.facebook.*;
import com.facebook.model.*;

import android.content.Intent;

public class MainActivity extends Activity {

	private static final List<String> PERMISSIONS = Arrays.asList("user_friends", "manage_pages");
	private ArrayList<FacebookFriend> friends = new ArrayList<FacebookFriend>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	
	    //start Facebook Login
	    Session.openActiveSession(this, true, new Session.StatusCallback() {
	
	      // callback when session changes state
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
		        	List<String> permissions = session.getPermissions(); 
		            if (!permissions.contains("user_friends")) { 
		              Session.NewPermissionsRequest   newPermissionsRequest = new Session.NewPermissionsRequest(MainActivity.this,PERMISSIONS);
		              session.requestNewPublishPermissions(newPermissionsRequest);
		            }
		            Request request = Request.newMyFriendsRequest(session,new Request.GraphUserListCallback() {
		            
		    		  @Override
		    		  public void onCompleted(List<GraphUser> users, Response response) {
		    			  
		                  if (users != null && response.getError() == null) {
	                        for (GraphUser user : users) {
	                            FacebookFriend fb = new FacebookFriend(user.getId(), user.getFirstName(), user.getLastName());
								friends.add(fb);
	                        }
		                  }
		                final ListView listview = (ListView) findViewById(R.id.listview);
		      		    Adapter adapter = new Adapter(MainActivity.this, friends);
		      		    listview.setAdapter(adapter);
		    		  }
		            });
		        	Bundle bundle = request.getParameters();
		        	bundle.putString("fields", "id, first_name, last_name, picture");
		        	request.setParameters(bundle);
		        	request.executeAsync();
					}
				}
	    	});
		    
		}
    
	 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
