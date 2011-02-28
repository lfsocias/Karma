package edu.nps.karma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import edu.nps.karma.RestClient.RequestMethod;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	public static final String DEBUG_TAG = "LoginActivity";
	
	private Button mLoginButton;
	private TextView mUsernameText;
	private TextView mPasswordText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Check to see if already logged in
        SharedPreferences prefs = 
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String username = prefs.getString("username", null);
        String password = prefs.getString("password", null);
        if (username != null && password != null) {
        	if (checkCredentials(username, password)) {
        		// If the username and password are saved and correct, go to the homescreen
        		Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        		startActivity(i);
        	}
        }
        
        setContentView(R.layout.login);
        
        mUsernameText = (TextView)findViewById(R.id.username_text);
        mPasswordText = (TextView)findViewById(R.id.password_text);
        mLoginButton = (Button)findViewById(R.id.login_button);
        
        mLoginButton.setOnClickListener(mLoginButtonListener);
    }
    
    private boolean checkCredentials(String username, String password) {
    	RestClient client = new RestClient(getResources().getString(R.string.server_url));
		client.AddHeader("Authorization", "Basic " + RestClient.getCredentials(
				username, password));
		
		try {
			client.Execute(RequestMethod.GET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String response = client.getResponse();
		
		Log.i(DEBUG_TAG, response);
		
		// This is a hack, but the HTML will only contain Logout if the credentials are good
		// otherwise is has Login
		if (response.contains("Logout")) {
			return true;
		}
		
		return false; // only returns false if credentials are bad	
    }
    
    private OnClickListener mLoginButtonListener = new OnClickListener() {
        public void onClick(View v) {
        	/*
        	 * We will check the credentials against the server.  If they are good, store
        	 * them and move on.  For now, I will assume they are good enough
        	 */
        	if (mUsernameText.getText().toString().equals("") || 
        			mPasswordText.getText().toString().equals("")) {
        		Toast.makeText(LoginActivity.this, "Username or password cannot be blank", 
        				Toast.LENGTH_SHORT).show();
        	} else {
				if (checkCredentials(mUsernameText.getText().toString(), 
						mPasswordText.getText().toString())) {
					// If the username and password are good, save them
					SharedPreferences prefs = 
						PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	        		SharedPreferences.Editor editor = prefs.edit();
	        		editor.putString("username", mUsernameText.getText().toString());
	        		editor.putString("password", mPasswordText.getText().toString());
	        		editor.commit();
	        		
	        		// Once the username and password are saved, go to the homescreen
	        		Intent i = new Intent(getApplicationContext(), HomeActivity.class);
	        		startActivity(i);
	        		finish();
				} else {
					// Username+password is bad, try again
					mUsernameText.setText("");
					mPasswordText.setText("");
					mUsernameText.requestFocus();
					Toast.makeText(LoginActivity.this, "Username or password is wrong", Toast.LENGTH_SHORT).show();
				}
        		
        	}
        }
    };
}