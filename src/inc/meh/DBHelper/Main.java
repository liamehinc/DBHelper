package inc.meh.DBHelper;

//import MT.Package.R;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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
	Button buttonAlto;
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
		this.dh = new DBHelper(Main.this); 
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    		
		final Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		//String locationprovider = mLocationManager.getBestProvider(criteria,true);		
    	
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
            	buttonAlto.setClickable(true);
            	String locationprovider = mLocationManager.getBestProvider(criteria, true);
            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
        	     // Register the listener with the Location Manager to receive location updates
        	     mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50, 1, mLocationListener);
 			    dh.insert("Start Insert: " +mLocation.getLatitude() + ", " + mLocation.getLongitude() );
            }
            
        }); 
        // Close of Start BUtton Activity
        
        // Retrieve 1 row from database
        buttonManualInsert = (Button) findViewById(R.id.buttonManualInsert);
        buttonManualInsert.setOnClickListener(new View.OnClickListener(){
        	@Override
        	public void onClick(View v) {
        		//Perform action on click
            	//tv=(TextView) findViewById(R.id.TextView1); 
        		String InsertString; 
            	
        		//String coordinates = dh.SelectRow("In");
        		String locationprovider = mLocationManager.getBestProvider(criteria, true);
            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);

            	InsertString = "Manual Insert: " +mLocation.getLatitude() + ", " + mLocation.getLongitude(); 
            	dh.insert( InsertString );
     
        		Toast.makeText(Main.this, InsertString, Toast.LENGTH_SHORT).show();
        		
        	}
        	
        });
 		// Close Retrieve 1 row from database
        
        // Stop location updates
 		buttonAlto = (Button) findViewById(R.id.buttonStop1);
 		buttonAlto.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				buttonAlto.setClickable(false);
 				buttonStart.setClickable(true);

 				mLocationManager.removeUpdates(mLocationListener); 
 				String coord = dh.SelectRow("In");
 			//	mLocation.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results)
 				dh.insert("Stopped inserting: " + coord );
 			}
 		});
 		// Close of buttonAlto.setOnClickListener
 		buttonAlto.setClickable(false);
 		
        // Retrieve button activity
		buttonRetrieve = (Button) findViewById(R.id.button2);
        buttonRetrieve.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	tv=(TextView) findViewById(R.id.TextView1); 
          
            	List<String> names = dh.selectAll(); 
        		StringBuilder sb = new StringBuilder(); 
        		sb.append("Coordinates in database:\n"); 
        		for (String name : names) { 
        			sb.append(name + "\n"); 
        		} 
				
		Log.d("EXAMPLE", "names size - " + names.size()); 
		tv.setText(sb.toString()); 
		        		
        //    	Toast.makeText(Main.this, "Retrieved lat/long: 32.9638148,-117.0929607", Toast.LENGTH_LONG).show();
        		//Toast.make
            }
        });
        // Close Retrieve button activity

    	
    	// Define a listener that responds to location updates
        mLocationListener = new LocationListener() {
    	     public void onLocationChanged(Location mlocation) {
    		     // Called when a new location is found by the network location provider.
    	    	 
    	    	 String InsertString;
    	    	 
    	    	 InsertString = "Auto Insert: " + mlocation.getLatitude() + ", " + mlocation.getLongitude();
    	    	 Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
    			    dh.insert(InsertString);
    			 
    	     }
    	     
    	
    	     public void onStatusChanged(String provider, int status, Bundle extras) {}
    	
    	     public void onProviderEnabled(String provider) {}
    	
    	     public void onProviderDisabled(String provider) {}
        };

        
	}
	// Close onCreate
}
   

