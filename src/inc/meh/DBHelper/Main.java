package inc.meh.DBHelper;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
//import android.database.Cursor;
import android.location.*;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private TextView tv;
	private DBHelper dh;
	private LocationManager mLocationManager;
	LocationListener mLocationListener;
	Button buttonStop;
	Button buttonStart;
	Button buttonDelete;
	Button buttonManualInsert;
	Button buttonRetrieve;
	Button buttonRetrieve1;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// initialize the database
		this.dh = new DBHelper(Main.this);
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!mLocationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
			startActivity(myIntent);
		}

		final Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// Delete all rows
		buttonDelete = (Button) findViewById(R.id.button3);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv = (TextView) findViewById(R.id.TextView1);

				dh.deleteAll();
			}
		});
		// Close Delete all rows

		// Start Button activity
		buttonStart = (Button) findViewById(R.id.button1);
		buttonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Perform action on click
				tv = (TextView) findViewById(R.id.TextView1);

				String locationprovider = mLocationManager.getBestProvider(
						criteria, true);

				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 60000, 10, mLocationListener);
				Location mLocation = mLocationManager
						.getLastKnownLocation(locationprovider);
				// Register the listener with the Location Manager to receive
				// location updates
				// mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50,
				// 1, mLocationListener);

				if (mLocation != null) {
					buttonStart.setClickable(false);
					buttonStop.setClickable(true);
					String InsertStringInsertype = "Start";
					Double InsertStringLat = mLocation.getLatitude();
					Double InsertStringLon = mLocation.getLongitude();

					String InsertString = InsertStringInsertype + ",'"
							+ InsertStringLat + "','" + InsertStringLon + "','";

					Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT)
							.show();
					dh.insert(InsertStringInsertype, InsertStringLat,
							InsertStringLon, 0.0, 0.0);

					Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(Main.this, "turn on your GPS lame-o",
							Toast.LENGTH_SHORT).show();
				}
			}

		});
		// Close of Start Button Activity

		// Manual Insert
		buttonManualInsert = (Button) findViewById(R.id.buttonManualInsert);
		buttonManualInsert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				String InsertString;

				// String coordinates = dh.SelectRow("In");
				String locationprovider = mLocationManager.getBestProvider(criteria, true);
				Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
				if (mLocation != null) {
					String InsertStringInsertype = "Manual";
					Double InsertStringLat = mLocation.getLatitude();
					Double InsertStringLon = mLocation.getLongitude();
					
					double[] calcedDist = CalculateDistance(InsertStringLat, InsertStringLon);
					
					// String InsertString = InsertStringInsertype + ",'" +
					// InsertStringLat + "','" + InsertStringLon + "','" +
					// dist2Prev + "'," + cumDist;
					double dist2Prev = calcedDist[0];
					double dcumDist = calcedDist[1];
					
					InsertString = InsertStringInsertype + "," + InsertStringLat + "," + InsertStringLon + "," + dist2Prev + "," + dcumDist;
					// Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
					//  dh.insert(InsertString, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);  - causing duplicate string insertion into 1 row
					dh.insert(InsertStringInsertype, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);

					Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
							Main.this,
							"I can't insert this location because I don't know where you are.",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		// Close Manual Insert

		// Retrieve1 button
		buttonRetrieve1 = (Button) findViewById(R.id.button4);
		buttonRetrieve1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> OneRow = dh.selectOneRow("");
				if (OneRow.isEmpty()) {
					tv.setText("there are no coordinate entries");
				}
				else {
					tv.setText(OneRow.toString());
				}
			}
		});

		// Stop location updates
		buttonStop = (Button) findViewById(R.id.buttonStop1);
		buttonStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonStop.setClickable(false);
				buttonStart.setClickable(true);

				String sStartLat = "";
				String sStartLong = "";

				String sStopLat = "";
				String sStopLong = "";
				//mLocationManager.removeUpdates(mLocationListener);  // try moving this further down in code

				// calculate distance
				tv.setText("calc this");
				// get last location in database
				double[] dLastLocation = getLastLocation();

				// break out lat and long
				double dStartLat = dLastLocation[0];
				double dStartLong = dLastLocation[1];
				float[] results = { 999f };

				String locationprovider = mLocationManager.getBestProvider(criteria, true);
				Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);

				if (mLocation != null) {
					String InsertStringInsertype = "Stop";
					Double InsertStringLat = mLocation.getLatitude();
					Double InsertStringLon = mLocation.getLongitude();

					Double dist2Prev = 0.0;
					Double cumDist = 0.0;

					// is the db empty?
					if (dLastLocation != null) {
						// double dDist2Prev = dLastLocation[2];
						double dCumDist = dLastLocation[3];

						if (dCumDist != 0) {
							cumDist = dCumDist;
						}
						
						// float[] results = {999f};
						// get distance between here and last record in database
						android.location.Location.distanceBetween(dStartLat,
								dStartLong, InsertStringLat, InsertStringLon,
								results);

						double dDist2Prev = results[0];

						dist2Prev = dDist2Prev;
						cumDist += dDist2Prev;
						mLocationManager.removeUpdates(mLocationListener);  // moved later in routine to allow for removal of requestLocationupdates
					}
					// show me the money!!!
					android.location.Location.distanceBetween(dStartLat,
							dStartLong, InsertStringLat, InsertStringLon,
							results);

					tv.setText("distanceBetween: " + results[0]);

					sStopLat = InsertStringLat.toString();
					sStopLong = InsertStringLon.toString();

					sStartLat = Double.toString(dStartLat);
					sStartLong = Double.toString(dStartLong);

					tv.setText("start Lat: " + sStartLat + " start Long: "
							+ sStartLong + "stop Lat: " + sStopLat
							+ "stop Long: " + sStopLong);
					tv.setText("distanceBetween: " + results[0]
							+ "\n\nstart Lat: " + sStartLat + " start Long: "
							+ sStartLong + "\nstop Lat: " + sStopLat
							+ "stop Long: " + sStopLong);

					String InsertString = InsertStringInsertype + ",'"
							+ InsertStringLat + "','" + InsertStringLon + "','";

					Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT)
							.show();
					dh.insert(InsertStringInsertype, InsertStringLat,
							InsertStringLon, dist2Prev, cumDist);

				} else {
					Toast.makeText(Main.this, "turn on your GPS lame-o",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		// Close of buttonStop.setOnClickListener
		buttonStop.setClickable(false);

		// Retrieve button activity
		buttonRetrieve = (Button) findViewById(R.id.button2);
		buttonRetrieve.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				tv = (TextView) findViewById(R.id.TextView1);

				List<String> names = dh.selectAll("id");
				StringBuilder sb = new StringBuilder();
				sb.append("Coordinates in database:\n");
				for (String name : names) {
					sb.append(name + ", ");
					// sb.append("\n");
				}

				Log.d("EXAMPLE", "names size - " + names.size());
				tv.setText(sb.toString());
			}
		});
		// Close Retrieve button activity

		// Define a listener that responds to location updates
		// i.e. auto update
		mLocationListener = new LocationListener() {
			public void onLocationChanged(Location mlocation) {
				// Called when a new location is found by the network location
				// provider.

				// make sure there is an active trip b4 logging to db

				//if (isTripActive()) {

					String InsertStringInsertype = "Auto";
					Double InsertStringLat = mlocation.getLatitude();
					Double InsertStringLon = mlocation.getLongitude();

					double[] calcedDist = CalculateDistance(InsertStringLat, InsertStringLon);
					
					// String InsertString = InsertStringInsertype + ",'" +
					// InsertStringLat + "','" + InsertStringLon + "','" +
					// dist2Prev + "'," + cumDist;
					double dist2Prev = calcedDist[0];
					double dcumDist = calcedDist[1];
					
					String InsertString = InsertStringInsertype + ","
							+ InsertStringLat + "," + InsertStringLon + ","
							+ dist2Prev + "," + dcumDist;
					// Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
					//  dh.insert(InsertString, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);  - causing duplicate string insertion into 1 row
					dh.insert(InsertStringInsertype, InsertStringLat, InsertStringLon,
							dist2Prev, dcumDist);

				//}// end isTripActive()

			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

		};
		// Register the listener with the Location Manager to receive location
		// updates
//		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	//			0, 0, mLocationListener);

	}

	// Close onCreate
	// helper method to get last location in db for breadcrumbs
	private boolean isTripActive() {
		if (buttonStart.isClickable() == false && buttonStop.isClickable()) {
			return true;
		} else {
			return false;
		}

	}

	
	// helper method to calculate distances
	private double[] CalculateDistance(double InsertStringLat, double InsertStringLon) {
		// get last location in database
		double[] dLastLocation = getLastLocation();

		Double dist2Prev = 0.0;
		Double dcumDist = 0.0;

		// is the db empty?
		if (dLastLocation != null) {
			// break out lat and long
			double dStartLat = dLastLocation[0];
			double dStartLong = dLastLocation[1];

			// double dDist2Prev = dLastLocation[3];
			dcumDist = dLastLocation[3];

			float[] results = { 999f };
			// get distance between here and last record in database
			android.location.Location.distanceBetween(dStartLat,
					dStartLong, InsertStringLat, InsertStringLon,
					results);

			// float fDistanceBeween= results[0];

			double dDist2Prev = results[0];

			dist2Prev = dDist2Prev;
			dcumDist += dDist2Prev;
			
		}  // end if (dLastLocation != null) 

		double[] dReturn = { dist2Prev, dcumDist };
		return dReturn;

	}
	
	
	// helper method to get last location in db for breadcrumbs
	private double[] getLastLocation() {
		String sStartLat = "";
		String sStartLong = "";
		List<String> dbRow = dh.selectOneRow("");

		/*
		 * StringBuilder sb = new StringBuilder(); for (String name : names) {
		 * sb.append(name + ", "); }
		 * 
		 * String sFromDB=sb.toString();
		 * 
		 * String[] sFromDBArray=sFromDB.split(",");
		 */

		// String[] sFromDBArray=(String[]) dbRow.toArray();
		
		sStartLat = dbRow.get(1);// sFromDBArray[1];
		sStartLong = dbRow.get(2);// sFromDBArray[2];

		String sDist2Prev = dbRow.get(3);// sFromDBArray[3];
		String sCumDist = dbRow.get(4);// sFromDBArray[4];

		sStartLat = sStartLat.replace("'", "");
		sStartLong = sStartLong.replace("'", "");
		double dStartLat = Double.parseDouble(sStartLat);
		double dStartLong = Double.parseDouble(sStartLong);

		double dDist2Prev = Double.parseDouble(sDist2Prev);
		double dCumDist = Double.parseDouble(sCumDist);

		double[] dReturn = { dStartLat, dStartLong, dDist2Prev, dCumDist };

		return dReturn;

	}
}
