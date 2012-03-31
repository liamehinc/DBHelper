package inc.meh.MileageTracker;

import inc.meh.MileageTracker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	private TextView tv;
	private DAO dh;
	protected LocationManager mLocationManager;
	protected LocationListener mLocationListener;
	Button buttonStop;
	protected Button buttonStart;
	Button buttonDelete;
	Button buttonManualInsert;
	Button buttonRetrieve;
	Button buttonRetrieve1;
	Button buttonExport;
	private boolean debug=false;
	public boolean isTracking=false;
	int InsertStringTripId;
	int iMinTime=3000;
	int iMinDist=1;
	
	//initialize trip and provide context
	private Trip t;
	//private History h = new History();

	final Criteria criteria = new Criteria();
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.export_data:
	        	ExportData();
	        	Toast.makeText(this, "Exporting trips...", Toast.LENGTH_SHORT).show();
	        	return true;
	        
	        case R.id.truncate_data:
	        	
	        	if (isTracking){
	        		
	        		Toast.makeText(this, getResources().getString(R.string.TruncateWhileTripRunning), Toast.LENGTH_LONG).show();
	        		
	        	}
	        	else
	        	{
	        		
	        		//confirm before delete
	        		//Toast.makeText(this, "confirm", Toast.LENGTH_SHORT).show();
	        		
	    	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        		builder.setMessage("Are you sure you want to remove ALL trips?\nThis cannot be undone.")
	        		       .setCancelable(false)
	        		       .setTitle("Confirm Delete")
	        		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        		         
	        		    	   public void onClick(DialogInterface dialog, int id) {
	        		                //MyActivity.this.finish();
	        		    		   
	        		    		   TruncateData();
	        		    		  Toast.makeText(Main.this, "Erasing ALL Trips...", Toast.LENGTH_SHORT).show();
	        		           
	        		    	   }
	        		       })
	        		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        		           public void onClick(DialogInterface dialog, int id) {
	        		                dialog.cancel();
	        		           }
	        		       });
	        		
	        		AlertDialog alert = builder.create();
	        		
	        		alert.show();

	        	}

	        	return true;
	        
	        case R.id.show_all_data:
	        	ShowAllData();
	            return true;
	        
	        case R.id.exit:
	        	finishConfirm();
	            return true;
	            
	        default:
	            return super.onOptionsItemSelected(item);
	    }

		//return true;
	}
	
	
	@Override
	public void onBackPressed() {
	   finishConfirm();
	}
	
	//@Override
	public void finishConfirm()
	{

		if (isTracking)
		{

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("Are you sure you want to exit while a trip is running?\n\nTo use other applications, please use the Home button.  You can still track but the accuracy will be reduced when running in the background.")
			       .setCancelable(false)
			       .setTitle("Confirm Exit")
			       .setPositiveButton("Exit and Stop Trip", new DialogInterface.OnClickListener() {
			         
			    	   public void onClick(DialogInterface dialog, int id) {
			    		   Main.this.finish();
			    	   }
			       })
			       /*
			       .setNeutralButton("Exit and Keep Trip Running", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                
			        	   //Intent homeIntent = new Intent(Intent.ACTION_MAIN);
			        	   //homeIntent.setAction(Intent.CATEGORY_HOME);
			        	   //Main.this.startActivity(homeIntent);
			        	   
			        	   onPause();
			        	   onStop();
			           }
			           })
			           */
			       .setNegativeButton("Don't Exit", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       }
			       
			       );
			
			AlertDialog alert = builder.create();
			
			alert.show();
			
			
		}
		else
		{
			//exit without prompt
			Main.this.finish();
		}
		
}
	
	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		//save button state
		outState.putString("ButtonState", buttonStart.getText().toString());
		outState.putBoolean("isTracking", isTracking);
		//outState.putString(tv., value)
		
		super.onSaveInstanceState(outState);
		
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		//---retrieve the information persisted earlier---
		//String sButtonState = savedInstanceState.getString("ButtonState");
		isTracking = savedInstanceState.getBoolean("isTracking");
		
		//if (sButtonState == getResources().getString(R.string.Stop))
		if (isTracking)
		{
			//buttonStart.setText(sButtonState);
			buttonStart.setText(getResources().getString(R.string.Stop));
			buttonStart.setBackgroundResource(R.drawable.pressed_button);
		}
	}
	
	private void ExportData()
	{
		
		String columnString ="Trip Number, Date Created, Distance Travelled (miles) ";
    	String combinedString = columnString + "\n"; //+ dataString;

    	//Call DB via DAO
    	List<String> names = dh.getTripInfo("tripid");

		StringBuilder sb = new StringBuilder();
		
		int i = 0;
		
		//iterate through results and build csv
		for (String name : names) {
			 
			sb.append(name + ", ");
			i++;
			
			//end of line
			if (i==3) {
				sb.append("\n");
				i=0;
			}
		}

		combinedString += sb.toString();
		//Log.d("EXAMPLE", "names size - " + names.size());
		
    	File file   = null;
    	File root   = Environment.getExternalStorageDirectory();
    	if (root.canWrite()){
    	    File dir    =   new File (root.getAbsolutePath() + "/PersonData");
    	     dir.mkdirs();
    	     file   =   new File(dir, getResources().getString(R.string.ExportFileName));
    	     FileOutputStream out   =   null;
    	    try {
    	        out = new FileOutputStream(file);
    	        } catch (FileNotFoundException e) {
    	            e.printStackTrace();
    	        }
    	        try {
    	            out.write(combinedString.getBytes());
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	        try {
    	            out.close();
    	        } catch (IOException e) {
    	            e.printStackTrace();
    	        }
    	    }

    	//get local .csv file
    	Uri u1  =   null;
    	
    	u1  =   Uri.fromFile(file);

    	//build email
    	Intent sendIntent = new Intent(Intent.ACTION_SEND);
    	sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MileageTracker Export");
    	
    	Calendar currentDate = Calendar.getInstance();
    	  SimpleDateFormat formatter= 
    	  new SimpleDateFormat("EEE, MMM d yyyy HH:mm:ss");
    	  String dateNow = formatter.format(currentDate.getTime());

    	sendIntent.putExtra(Intent.EXTRA_TEXT, "Mileage Exported on: " + dateNow);
    	sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
    	sendIntent.setType("text/html");

    	this.startActivity(Intent.createChooser(sendIntent, "Send mail..."));

	}

	//Show Table of Trips on Screen
	private void ShowAllData()
	{

		//Column names
		String[] columns ={"Trip ", "Date Created ", "Distance Travelled (miles) "};

		//build TableLayout
		TableLayout tl=(TableLayout) findViewById(R.id.ShowDataTable);

		//empty table to make sure data is not appended for each click of menu
		tl.removeAllViews();
		
		//add the column headers
		TableRow trc=new TableRow(this);
		trc.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT ));
        
		TableLayout.LayoutParams tableRowParams=
		  new TableLayout.LayoutParams
		  (TableLayout.LayoutParams.FILL_PARENT,TableLayout.LayoutParams.FILL_PARENT);

		int leftMargin=1;
		int topMargin=0;
		int rightMargin=1;
		int bottomMargin=0;

		tableRowParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);

		trc.setLayoutParams(tableRowParams);
		
		//Build Columns
		for (String col : columns) {
			TextView tvc = new TextView(this);
			tvc.setText(col);
			trc.addView(tvc);
		}
		
		tl.addView(trc);
		
		int i = 0;
		double dTotalDistance=0;

		//Get Trips from DAO
    	List<String> names = dh.getTripInfo("tripid");
		
    	//iterate through trips and display in 3 column format
		for (int row=0; row < (names.size()/3); row++) {

			TableRow tr=new TableRow(this);
			tr.setLayoutParams(tableRowParams);
			
			for (int col=0; col<3; col++) {
				TextView tv = new TextView(this);
				tv.setText(names.get(i)+ " ");
				tr.addView(tv);
				
				if (col==2)
				{
					dTotalDistance+= Double.parseDouble(names.get(i));
				}
				
				i++;
			}
		

		tl.addView(tr);
		}
		
		//Bottom Row for Total Mileage
		TableRow trBottom=new TableRow(this);
		
		int leftMarginB=0;
		int topMarginB=10;
		int rightMarginB=0;
		int bottomMarginB=0;

		TableLayout.LayoutParams tableRowParamsB=
			  new TableLayout.LayoutParams
			  (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.MATCH_PARENT);

		
		tableRowParamsB.setMargins(leftMarginB, topMarginB, rightMarginB, bottomMarginB);

		trBottom.setLayoutParams(tableRowParamsB);

		
		TextView tvBottom0 = new TextView(this);
		
		tvBottom0.setText("");
		trBottom.addView(tvBottom0);
		
		
		
		TextView tvBottom1 = new TextView(this);
		
		tvBottom1.setText("Total (miles): ");
		tvBottom1.setGravity(Gravity.RIGHT);
		trBottom.setGravity(Gravity.RIGHT);
		trBottom.addView(tvBottom1);
		
		
		TextView tvBottom2 = new TextView(this);
		tvBottom2.setText(Double.toString(dTotalDistance) );
		//tvBottom1.setGravity(Gravity.LEFT);
		trBottom.addView(tvBottom2);
		
		
		tl.addView(trBottom);
			
		
		Log.d("EXAMPLE", "names size - " + names.size());
	}
	
	//method for removing all data from DB and clearing table from screen
	private void TruncateData()
	{
		dh.deleteAll();
		ShowAllData();
	}
	
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// initialize the database
		this.dh = new DAO(Main.this);
		
		//initialize Trip object and pass Context
		this.t = new Trip(Main.this);
		
		//initialize LocationManager
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (!mLocationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
			startActivity(myIntent);
		}


		//criteria settings
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);

		// debug button to delete all rows
		buttonDelete = (Button) findViewById(R.id.button3);
		
		if (!debug)
			buttonDelete.setVisibility(Button.GONE);
		
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv = (TextView) findViewById(R.id.TextView1);

				dh.deleteAll();
			}
		});
		// Close Delete all rows

		// ****************Start Button activity
		buttonStart = (Button) findViewById(R.id.btnStart);
		//buttonStart.setBackgroundColor(Color.GREEN);
		//buttonStart.setWidth(560);
		//buttonStart.setHeight(90);
		
		buttonStart.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// Perform action on click
				tv = (TextView) findViewById(R.id.TextView1);

				String locationprovider = mLocationManager.getBestProvider(
						criteria, true);

				mLocationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, iMinTime, iMinDist, mLocationListener);
				Location mLocation = mLocationManager
						.getLastKnownLocation(locationprovider);

				//turn on your GPS, Lame-o
				if (mLocation != null)
				{
					
					//toggle based on tracking
					if (!isTracking) 
					{
						t.StartTrack(mLocation);
						
						//change button text
						buttonStart.setText(getResources().getString(R.string.Stop));

						buttonStart.setBackgroundResource(R.drawable.pressed_button);
						isTracking=true;
					}
					else
					{

							t.StopTrack(mLocationManager, criteria, mLocationListener);
						
							// convert the distance numbers to miles rather then the default meters.
							// display to screen (i.e. odometer)

							tv.setText("Trip # " + t.getTripId() + "\nTrip Distance: " + Util.Meters2Miles(t.getCumulativeTripDistance()));

							
						//change button text
						buttonStart.setText(getResources().getString(R.string.Insert));
						
						buttonStart.setBackgroundResource(R.drawable.nice_button);
						isTracking=false;
					}
					
					//empty table to make sure data is not appended for each click of menu
					TableLayout tl=(TableLayout) findViewById(R.id.ShowDataTable);
					tl.removeAllViews();
					
				}
				else  //prompt to enable GPS 
				{
					Toast.makeText(Main.this, getResources().getString(R.string.NoGPS),	Toast.LENGTH_SHORT).show();
				}
			}

		});
		// Close of Start Button Activity

		// Debug button for Manual Insert
		buttonManualInsert = (Button) findViewById(R.id.buttonManualInsert);

		if (!debug)
			buttonManualInsert.setVisibility(Button.GONE);
		
		buttonManualInsert.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				//String InsertString;

				// String coordinates = dh.SelectRow("In");
				String locationprovider = mLocationManager.getBestProvider(criteria, true);
				Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
				if (mLocation != null) {
					String InsertStringInsertype = "Manual";
					Double InsertStringLat = mLocation.getLatitude();
					Double InsertStringLon = mLocation.getLongitude();
					
					double[] calcedDist = t.CalculateDistance(InsertStringLat, InsertStringLon);
					
					// String InsertString = InsertStringInsertype + ",'" +
					// InsertStringLat + "','" + InsertStringLon + "','" +
					// dist2Prev + "'," + cumDist;
					double dist2Prev = calcedDist[0];
					double dcumDist = calcedDist[1];
					
					//InsertString = InsertStringInsertype + "," + InsertStringLat + "," + InsertStringLon + "," + dist2Prev + "," + dcumDist;
					// Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
					//  dh.insert(InsertString, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);  - causing duplicate string insertion into 1 row
					
					dh.insert(InsertStringTripId,InsertStringInsertype, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);

					//Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
					
				} else {
					Toast.makeText(
							Main.this,
							"I can't insert this location because I don't know where you are.",
							Toast.LENGTH_SHORT).show();
				}

			}

		});
		// Close Manual Insert

		// Debug button for Retrieve1 button
		buttonRetrieve1 = (Button) findViewById(R.id.button4);
		
		if (!debug)
			buttonRetrieve1.setVisibility(Button.GONE);
		
		buttonRetrieve1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> OneRow = dh.selectOneRow("");
				if (OneRow.isEmpty()) {
					tv.setText("There are no coordinate entries");
				}
				else {
					tv.setText(OneRow.toString());
				}
			}
		});

		//Debug -- Export button
		buttonExport = (Button) findViewById(R.id.btnExport);
		
		if (!debug)
			buttonExport.setVisibility(Button.GONE);
		
		buttonExport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				Toast.makeText(Main.this, "Export", Toast.LENGTH_SHORT).show();
				
				Intent myIntent = new Intent(v.getContext(), Email.class);
                startActivityForResult(myIntent, 0);

			}
		});
		

		
		// Stop location updates
		// buttonStop = (Button) findViewById(R.id.buttonStop1);
/*		buttonStop.setOnClickListener(new View.OnClickListener() {
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
	//	buttonStop.setClickable(false);
*/
		// Debug -- Retrieve button activity
		buttonRetrieve = (Button) findViewById(R.id.button2);
		
		if (!debug)
			buttonRetrieve.setVisibility(Button.GONE);
		
		buttonRetrieve.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				tv = (TextView) findViewById(R.id.TextView1);

				List<String> names = dh.selectAll("coordinateid");
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

				if (isTracking) {

					String InsertStringInsertype = "Auto";
					Double InsertStringLat = mlocation.getLatitude();
					Double InsertStringLon = mlocation.getLongitude();

					double[] calcedDist = t.CalculateDistance(InsertStringLat, InsertStringLon);
					
					// String InsertString = InsertStringInsertype + ",'" +
					// InsertStringLat + "','" + InsertStringLon + "','" +
					// dist2Prev + "'," + cumDist;
					double dist2Prev = calcedDist[0];
					double dcumDist = calcedDist[1];
					
					//String InsertString = InsertStringInsertype + ","
					//		+ InsertStringLat + "," + InsertStringLon + ","
					//		+ dist2Prev + "," + dcumDist;
					
					// Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
					//  dh.insert(InsertString, InsertStringLat, InsertStringLon,dist2Prev, dcumDist);  - causing duplicate string insertion into 1 row
					dh.insert(t.getTripId(),InsertStringInsertype, InsertStringLat, InsertStringLon,
							dist2Prev, dcumDist);
					
					//String sCumDist= dcumDist.toString();
					
					String sMessage ="Total Trip Mileage:\n\n" + Util.Meters2Miles(dcumDist)+"\n" ;
					
					if (mlocation.hasSpeed())
						sMessage+= "\n\nSpeed: " + Util.roundDecimals(mlocation.getSpeed()*2.23693629,2) +" mph";
					
					if (mlocation.hasAltitude())
						sMessage+= "\n\nAltitude: " + Util.roundDecimals(mlocation.getAltitude()*3.2808399,2) +" feet " ;
					
					if (mlocation.hasBearing())
						sMessage+="\n\nBearing: " + Util.roundDecimals(mlocation.getBearing(),2) + " degrees ";
					
					
					
					tv.setTextSize(24);
					tv.setText(sMessage);

				}// end isTripActive()

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

	}   // Close onCreate






}