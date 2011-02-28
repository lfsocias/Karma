package edu.nps.karma;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import edu.nps.karma.RestClient.RequestMethod;

public class AddEventActivity extends Activity {
	
	public final static String DEBUG_TAG = "Add Event Activity";
	
	SharedPreferences mPrefs;
	String mUsername, mPassword, mURL;
	
	List<EventDetails> eventsList;
	EventListAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        mUsername = mPrefs.getString("username", null);
        mPassword = mPrefs.getString("password", null);
        
        // TODO: If either are null, start the logout activity to force a re-login
        
        mURL = getResources().getString(R.string.server_url)+"events.json";
        
        ListView list = (ListView)findViewById(R.id.all_events_list);
        
        // Get the events
        RestClient client = new RestClient(mURL);
		client.AddHeader("Authorization", "Basic " + RestClient.getCredentials(
				mUsername, mPassword));
		

		try {
			client.Execute(RequestMethod.GET);
			
			String response = client.getResponse();
			Log.i(DEBUG_TAG, response);
			
			eventsList = processJSONEvents(response);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		adapter = new EventListAdapter();
		list.setAdapter(adapter);
		//list.setOnItemClickListener(eventSelectedListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.app_options, menu);
		menu.findItem(R.id.add_event_menu_item).setIntent(
				new Intent(this, AddEventActivity.class));
		menu.findItem(R.id.logout_menu_item).setIntent(
				new Intent(this, LogoutActivity.class));
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
	
	private List<EventDetails> processJSONEvents(String response) throws JSONException {
		
		List<EventDetails> eventsList = new ArrayList<EventDetails>();
		JSONArray allEventsArray = new JSONArray(response);
		
		for (int i = 0;i<allEventsArray.length();i++) {
			
			JSONObject jsonEvent = allEventsArray.getJSONObject(i).getJSONObject("event");
			
			
			String eventName = jsonEvent.getString("name");
			String timeString = jsonEvent.getString("created_at");
			
			// From: http://www.coderanch.com/t/403639/java/java/convert-data-type-string-timestamp
			//SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-ddThh:mm:ssZ");
			//java.util.Date parsedDate = dateFormat.parse(timeString);
			//java.sql.Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());

            eventsList.add(new EventDetails(eventName, timeString));
	         
		
		}

		 return eventsList;
	}
	
	class EventListAdapter extends ArrayAdapter<EventDetails> {
		EventListAdapter() {
			super(getApplicationContext(), android.R.layout.simple_list_item_1, eventsList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			EventViewHolder holder;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.event_item, null);
				holder = new EventViewHolder(row);
				row.setTag(holder);
			} else {
				holder = (EventViewHolder)row.getTag();
			}
			
			holder.populateFrom(eventsList.get(position));
			
			return row;
		}
		
		
	}

	class EventDetails {
		private String mName;
		private String mTimestamp;
		
		public EventDetails(String name, String timestamp) {
			mName = name;
			mTimestamp = timestamp;
		}
		
		public String getEventName() {
			return mName;
		}
		
		public String getTimestamp() {
			return mTimestamp;
		}
	}
	
	static class EventViewHolder {
		private TextView mEventName;
		private TextView mTimestamp;
		private View row = null;
		
		EventViewHolder(View row) {
			this.row = row;
			mEventName = (TextView)this.row.findViewById(R.id.event_name);
			mTimestamp = (TextView)this.row.findViewById(R.id.event_timestamp);
		}
		
		void populateFrom(EventDetails d) {
			mEventName.setText(d.getEventName());
			mTimestamp.setText("Timestamp: " + d.getTimestamp());	
		}
	}

}
