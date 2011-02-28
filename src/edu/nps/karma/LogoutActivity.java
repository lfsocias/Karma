package edu.nps.karma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class LogoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("username", null);
		editor.putString("password", null);
		editor.commit();
		Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(this, LoginActivity.class);
		startActivity(i);
	}
	
	

}
