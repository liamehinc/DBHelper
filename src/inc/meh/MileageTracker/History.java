package inc.meh.MileageTracker;

import java.util.List;

import android.content.Context;


public class History {
	
	public Main Main;
	private DAO dh;
	
	//private Trip t;

	public History(Context context) {
		
		// initialize the database
		this.dh = new DAO(context);
		//this.Main = new Main();
		
	}
	
	protected void eraseAll() {
		dh.deleteAll();
		
	}
	
	//Show Table of Trips on Screen

	protected List<String> getCompleteHistory() {

		//List<String> names = dh.getTripInfo("tripid");
		
		return dh.getTripInfo("tripid");
	}

}
