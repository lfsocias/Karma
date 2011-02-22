package edu.nps.karma;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	SharedPreferences mPrefs;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String username = mPrefs.getString("username", "User");
        TextView userNameTextView = (TextView)findViewById(R.id.home_username_text);
        userNameTextView.setText(username);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.app_options, menu);
		menu.findItem(R.id.add_event_menu_item).setIntent(
				new Intent(this, AddEventActivity.class));
		menu.findItem(R.id.help_menu_item).setIntent(
				new Intent(this, HelpActivity.class));
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		startActivity(item.getIntent());
		return true;
	}

}
