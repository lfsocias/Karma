package edu.nps.karma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {
    /** Called when the activity is first created. */
	
	private Button mLoginButton;
	private TextView mUsernameText;
	private TextView mPasswordText;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        mUsernameText = (TextView)findViewById(R.id.username_text);
        mPasswordText = (TextView)findViewById(R.id.password_text);
        mLoginButton = (Button)findViewById(R.id.login_button);
        
        mLoginButton.setOnClickListener(mLoginButtonListener);
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
        	}
        }
    };
}