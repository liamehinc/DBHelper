package inc.meh.DBHelper;

import java.util.List;
import android.app.Activity;
import android.content.Context;
//import android.database.Cursor;
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
    		
		final Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
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
            	Location mLocation = mLocationManager.getLastKnownLocation(locationprovider);
 			    

       	     // Register the listener with the Location Manager to receive location updates
       	   //  mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50, 1, mLocationListener);
       	     	
       	     	String InsertStringInsertype = "Start";
	  		    Double InsertStringLat = mLocation.getLatitude();
	  		    Double InsertStringLon = mLocation.getLongitude();
	  		    String InsertStringCat = "pulse";
	  		    int InsertStringIsDeductible = 1;
	  		      
	  		    String InsertString = InsertStringInsertype + ",'" + InsertStringLat + "','" + InsertStringLon + "','" + InsertStringCat + "'," + InsertStringIsDeductible;
	  	    	// InsertString = "Auto: " + mlocation.getLatitude() + ", " + mlocation.getLongitude();
	  	    	Toast.makeText(Main.this, InsertString, Toast.LENGTH_LONG).show();
	  			dh.insert(InsertStringInsertype,InsertStringLat,InsertStringLon,InsertStringCat,InsertStringIsDeductible);
            }
            
        }); 
        // Close of Start BUtton Activity
        
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
  			    dh.insert(InsertStringInsertype,InsertStringLat,InsertStringLon,InsertStringCat,InsertStringIsDeductible);
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

 				mLocationManager.removeUpdates(mLocationListener); 
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
    	    	 dh.insert(InsertString, InsertStringLat, InsertStringLon, InsertStringCat, InsertStringIsDeductible);
    			 
    	     }
    	     
    	
    	     public void onStatusChanged(String provider, int status, Bundle extras) {}
    	
    	     public void onProviderEnabled(String provider) {}
    	
    	     public void onProviderDisabled(String provider) {}
    	     
        };
	     // Register the listener with the Location Manager to receive location updates
	     mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50, 1, mLocationListener);
        
	}
	// Close onCreate
}


   

