package inc.meh.MileageTracker;

import java.util.Date;

import android.content.Context;
import android.location.Location;



public class Trip {

	public Main Main;
	private DAO dh;
	private Context context;  

	private double distance;
	private Date startDate;
	private Date endDate;
	private String name;
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Trip(Context context) {
		
		this.context = context;
		
		// initialize the database
		this.dh = new DAO(context);
		
	}

	public Trip() {
		this.dh = new DAO(Main.getApplicationContext());
	}

	//Method to begin tracking mileage
	protected void StartTrack(Location mLocation) {
				
		//get the trip id
		int InsertStringTripId = dh.getTripId();
		
		//this is a "start" activity (not "auto" or "stop")
		String InsertStringInsertype = "Start";
		
		//get lat and long from location object
		Double InsertStringLat = mLocation.getLatitude();
		Double InsertStringLon = mLocation.getLongitude();

		//insert first entry in Database for this trip
		dh.insert(InsertStringTripId,InsertStringInsertype, InsertStringLat,
				InsertStringLon, 0.0, 0.0);

	}
	

	
	

}
