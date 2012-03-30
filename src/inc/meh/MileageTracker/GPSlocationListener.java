package inc.meh.MileageTracker;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class GPSlocationListener implements LocationListener 
{
    //member variables
    private Handler mParentHandler;//points to Handler of parent
    private long mTimeBetweenLocationEvents;
    private long mTimeOfLastLocationEvent;
    private boolean mAccuracyOverride;
    private float mLastAccuracy;
    private boolean mOverrideLocation;

    //constants
    private static final float INVALID_ACCURACY = 999999.0f;
    private static final String TAG = "GPSlocationListener";


    //constructor
    public GPSlocationListener(Handler parentMsgHandler, long timeBetweenLocationEvents, boolean accuracyOverride)
    {
        mParentHandler = parentMsgHandler;
        mTimeOfLastLocationEvent = 0;
        mAccuracyOverride = accuracyOverride;
        mLastAccuracy = INVALID_ACCURACY;
        mOverrideLocation = false;
        mTimeBetweenLocationEvents = timeBetweenLocationEvents;
    }

    //EVENT: onLocationChanged()
    // send GPS coordinates to CommService
    public void onLocationChanged(Location loc)
    {
        Log.d(TAG, "onLocationChanged() triggered. Accuracy = "+Float.toString(loc.getAccuracy()));
        mOverrideLocation = false;

        if (loc != null)
        {
            //if a more accurate coordinate is available within a set of events, then use it (if enabled by programmer)
            if (mAccuracyOverride == true)
            {
                //whenever the expected time period is reached invalidate the last known accuracy
                // so that we don't just receive better and better accuracy and eventually risk receiving
                // only minimal locations
                if (loc.getTime() - mTimeOfLastLocationEvent >= mTimeBetweenLocationEvents)
                {
                    mLastAccuracy = INVALID_ACCURACY;
                }


                if (loc.hasAccuracy())
                {
                    final float fCurrentAccuracy = loc.getAccuracy();

                    //the '<' is important here to filter out equal accuracies !
                    if ((fCurrentAccuracy != 0.0f) && (fCurrentAccuracy < mLastAccuracy))
                    {
                        mOverrideLocation = true;
                        mLastAccuracy = fCurrentAccuracy;
                    }
                }
            }

            //ensure that we don't get a lot of events
            // or if enabled, only get more accurate events within mTimeBetweenLocationEvents
            if (  (loc.getTime() - mTimeOfLastLocationEvent >= mTimeBetweenLocationEvents)
                ||(mOverrideLocation == true) )
            {
                //be sure to store the time of receiving this event !
                mTimeOfLastLocationEvent = loc.getTime();

                //send message to parent containing the location object
                Message msgToMain = mParentHandler.obtainMessage();
                msgToMain.what = 1;
                msgToMain.obj = loc;
                mParentHandler.sendMessage(msgToMain);
            }
        }
    }

    public void onProviderDisabled(String provider)
    {
        // TODO Auto-generated method stub
    }

    public void onProviderEnabled(String provider)
    {
        // TODO Auto-generated method stub
    }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

} 
