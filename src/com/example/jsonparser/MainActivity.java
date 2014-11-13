package com.example.jsonparser;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	private EditText locationEditText;
	private TextView countryTextView, temperatureTextView, humidityTextView,
			pressureTextView;
	private Button weather;
	private String location, url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		locationEditText = (EditText) findViewById(R.id.locationEdit);

		countryTextView = (TextView) findViewById(R.id.countryEdit);
		temperatureTextView = (TextView) findViewById(R.id.temperatureEdit);
		humidityTextView = (TextView) findViewById(R.id.humidityEdit);
		pressureTextView = (TextView) findViewById(R.id.pressureEdit);
		weather = (Button) findViewById(R.id.weather);

		weather.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new JSONAdder().execute();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public class JSONAdder extends AsyncTask<String, String, JSONObject> {
		ProgressDialog b = new ProgressDialog(MainActivity.this);
		private String country = "country";
		private String temperature = "temperature";
		private String humidity = "humidity";
		private String pressure = "pressure";

		public volatile boolean parsingComplete = true;

		public String getCountry() {
			return country;
		}

		public String getTemperature() {
			return temperature;
		}

		public String getHumidity() {
			return humidity;
		}

		public String getPressure() {
			return pressure;
		}

		@Override
		protected void onPreExecute() {
			b.setTitle("Please Wait");
			b.setMessage("Getting data");
			b.setCancelable(false);
			b.show();
			location = locationEditText.getText().toString();
			url = "http://api.openweathermap.org/data/2.5/weather?q="
					+ location;
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			JSONObject jObj = new JSONParser().getJSONFromUrl(url);
			return jObj;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			b.dismiss();
			try {
				JSONObject sys = result.getJSONObject("sys");
				country = sys.getString("country");

				JSONObject main = result.getJSONObject("main");
				temperature = main.getString("temp");
				pressure = main.getString("pressure");
				humidity = main.getString("humidity");

				countryTextView.setText(getCountry());
				temperatureTextView.setText(getTemperature());
				humidityTextView.setText(getHumidity());
				pressureTextView.setText(getPressure());
				parsingComplete = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.onPostExecute(result);
		}

		public void log() {

			Log.i("country", country);
			Log.i("temperature", temperature);
			Log.i("humidity", humidity);
			Log.i("pressure", pressure);
		}
	}
}
