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
	
    /** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
				
		setContentView(R.layout.main);
		
		// initialize the database
		// worthless change
		this.dh = new DBHelper(Main.this); 
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
//		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
		
		if(!mLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER ))
		{
		    Intent myIntent = new Intent( Settings.ACTION_SECURITY_SETTINGS );
		    startActivity(myIntent);
		}
        
    		
		final Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		//criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
    	
		// Delete all rows
		buttonDelete = (Button) findViewById(R.id.button3);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
		         tv=(TextView) findViewById(R.id.TextView1); 
		        
		      	dh.deleteAll();
			}
		});
		//Close Delete all rows
		
		// Start Button activity
		buttonStart = (Button) findViewById(R.id.button1);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
                // Perform action on click
            	tv=(TextView) findViewById(R.id.TextView1); 
            	 	
            	buttonStart.setClickable(false);
            	buttonStop.setClickable(true);
            	String locationprovider = mLocationManager.getBestProvider(criteria, true);
            	
            	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
            	
            	
            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
 			    

       	     // Register the listener with the Location Manager to receive location updates
       	   //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50, 1, mLocationListener);
       	     
            	if (mLocation!=null)
            	{
       	     	String InsertStringInsertype = "Start";
	  		    Double InsertStringLat = mLocation.getLatitude();
	  		    Double InsertStringLon = mLocation.getLongitude();
	  		    String InsertStringCat = "pulse";
	  		    int InsertStringIsDeductible = 1;
	  		      
	  		    String InsertString = InsertStringInsertype + ",'" + InsertStringLat + "','" + InsertStringLon + "','" + InsertStringCat + "'," + InsertStringIsDeductible;
	  	    	// InsertString = "Auto: " + mlocation.getLatitude() + ", " + mlocation.getLongitude();
	  	    	Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
	  			dh.insert(InsertStringInsertype,InsertStringLat,InsertStringLon);
            	
            	}
            	else
            	{
            		Toast.makeText(Main.this, "turn on your GPS lame-o", Toast.LENGTH_LONG).show();
    	  			
            	}
            	
            }
            
        }); 
        // Close of Start Button Activity
        
        // Retrieve 1 row from database
        buttonManualInsert = (Button) findViewById(R.id.buttonManualInsert);
        buttonManualInsert.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		//Perform action on click
        		String InsertString; 
            	
        		//String coordinates = dh.SelectRow("In");
        		String locationprovider = mLocationManager.getBestProvider(criteria, true);
            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);

            	
            	String InsertStringInsertype = "Manual";
  		      	Double InsertStringLat = mLocation.getLatitude();
  		      	Double InsertStringLon = mLocation.getLongitude();
  		      	String InsertStringCat = "pulse";
  		      	int InsertStringIsDeductible = 1;
  		      
  		      	InsertString = InsertStringInsertype + ",'" + InsertStringLat + "','" + InsertStringLon + "','" + InsertStringCat + "','" + InsertStringIsDeductible;

  		      	Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
  			    dh.insert(InsertStringInsertype,InsertStringLat,InsertStringLon);
        	}
        	
        });
 		// Close Retrieve 1 row from database
        
        // Stop location updates
 		buttonStop = (Button) findViewById(R.id.buttonStop1);
 		buttonStop.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				buttonStop.setClickable(false);
 				buttonStart.setClickable(true);
 				
 				String sStartLat="";
 				String sStartLong="";
 				
 				String sStopLat="";
 				String sStopLong="";

 				mLocationManager.removeUpdates(mLocationListener); 
 				
 				//calculate distance
 				tv.setText("calc this");

 				//get Stop session
 		      	 
// 		      	tv.setText("sFromDB: " + sFromDB);

 //		      	String[] sFromDBArray=sFromDB.split(",");
 		      	
            	List<String> names = dh.selectOneRow("Start"); 
        		StringBuilder sb = new StringBuilder(); 
        		sb.append("Coordinates in database:\n"); 
        		for (String name : names) { 
        			sb.append(name + ", "); 
        			//sb.append("\n");
        		} 
 		      	 
 		      	 //tv.setText("from array: " + sFromDBArray[0]);
 		      	
// 		      	sStartLat=sFromDBArray[1];
 //		      	sStartLong=sFromDBArray[2];
 		      	tv.setText("start Lat: " + sStartLat + " start Long: " + sStartLong);
 		      	
 		      	sStartLat=sStartLat.replace("'", "");
 		      	sStartLong=sStartLong.replace("'", "");
 		      	
 		      	//Location startLocation = new Location(sStartLong);
 		      	sStartLat="32.3987659";
 		      	sStartLong="-117.435876";

 		      	tv.setText("start Lat: " + sStartLat + " start Long: " + sStartLong);
 		      	

 		      	double dStartLat = Double.parseDouble(sStartLat);
		      	double dStartLong = Double.parseDouble(sStartLong);
 		     		      	
		      	float[] results = {999f};
	  		    
	  		    //android.location.Location.distanceBetween(dStartLat, dStartLong, dStartLat+1, dStartLat, results);
		      	
		      	//android.location.Location.distanceBetween(32, 117, 33, 117, results);
		      	
 		      	//tv.setText("distanceBetween: " + results[0] );
 		      	
 		      	String locationprovider = mLocationManager.getBestProvider(criteria, true);
            	
            	mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);

            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
 		      	
 		      	if (mLocation!=null)
            	{
       	     	String InsertStringInsertype = "Stop";
	  		    Double InsertStringLat = mLocation.getLatitude();
	  		    Double InsertStringLon = mLocation.getLongitude();
	  		    String InsertStringCat = "pulse";
	  		    int InsertStringIsDeductible = 1;
	  		      
	  		    //float[] results = {0};
	  		
	  		  android.location.Location.distanceBetween(dStartLat, dStartLong, InsertStringLat, InsertStringLon, results);
		      	
	  		    tv.setText("distanceBetween: " + results[0] );
		      	
	  		    sStopLat=InsertStringLat.toString();
	  		    sStopLong=InsertStringLon.toString();
	  		    
	  		    tv.setText("start Lat: " + sStartLat + " start Long: " + sStartLong + "stop Lat: "+ sStopLat + "stop Long: "+ sStopLong);
	  		    
	  		    String InsertString = InsertStringInsertype + ",'" + InsertStringLat + "','" + InsertStringLon + "','" + InsertStringCat + "'," + InsertStringIsDeductible;
	  	    	// InsertString = "Auto: " + mlocation.getLatitude() + ", " + mlocation.getLongitude();
	  	    	Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
	  			dh.insert(InsertStringInsertype,InsertStringLat,InsertStringLon);
            	
            	}
            	else
            	{
            		Toast.makeText(Main.this, "turn on your GPS lame-o", Toast.LENGTH_LONG).show();
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
            	tv=(TextView) findViewById(R.id.TextView1); 
          
            	List<String> names = dh.selectAll("id"); 
        		StringBuilder sb = new StringBuilder(); 
        		sb.append("Coordinates in database:\n"); 
        		for (String name : names) { 
        			sb.append(name + ", "); 
        			//sb.append("\n");
        		} 
        		//Toast.makeText(Main.this, sb.toString(), Toast.LENGTH_LONG).show();
				
        		Log.d("EXAMPLE", "names size - " + names.size()); 
        		tv.setText(sb.toString()); 

            }
        });
        // Close Retrieve button activity

    	
    	// Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
    	     public void onLocationChanged(Location mlocation) {
    		     // Called when a new location is found by the network location provider.
    	    	
    		      String InsertStringInsertype = "Auto";
    		      Double InsertStringLat = mlocation.getLatitude();
    		      Double InsertStringLon = mlocation.getLongitude();
    		      String InsertStringCat = "pulse";
    		      int InsertStringIsDeductible = 1;
    		      
    	    	 String InsertString = InsertStringInsertype + ",'" + InsertStringLat + "','" + InsertStringLon + "','" + InsertStringCat + "'," + InsertStringIsDeductible;
    	    	 Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
    	    	 dh.insert(InsertString, InsertStringLat, InsertStringLon);
    			 
    	     }
    	     
    	
    	     public void onStatusChanged(String provider, int status, Bundle extras) {}
    	
    	     public void onProviderEnabled(String provider) {}
    	
    	     public void onProviderDisabled(String provider) {}
    	     
        };
	     // Register the listener with the Location Manager to receive location updates
	     mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,mLocationListener);
        
	}
	// Close onCreate
}


   

