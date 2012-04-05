package inc.meh.MileageTracker;

import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Util {

	public static double Meters2Miles(double dInput)
	{
		double dOutput=0;

		if (dInput!=0){
			dOutput= dInput / 1609.344;
			//dOutput = Util.roundDecimals(dOutput, 5);
			
			
		}
		
		return dOutput;
	}
	
	public static double Meters2Miles(String sInput) {
		
		double dInput = Double.valueOf(sInput).doubleValue();
		
		return Meters2Miles(dInput);
		
	}
	
	public static double TwoDecimalPrecission(double fInput)
	{
		Float f = (float) (Math.round(fInput*100.0f)/100.0f);
		double dOutput=f.doubleValue();
		return dOutput;
	}
	
	public static double roundDecimals(double d, int iSignificantDigits) {
        
		String sFormat="#.##";
		
		switch (iSignificantDigits) {
		case 0:
			sFormat="#.##";
		case 1:
			sFormat="#.#";
		case 2:
			sFormat="#.##";
		case 3:
			sFormat="#.###";
		case 4:
			sFormat="#.####";
		case 5:
			sFormat="#.#####";
		default:
			sFormat="#.####";
		}
        
		DecimalFormat df = new DecimalFormat(sFormat);
       	return Double.valueOf(df.format(d));
	}
	
public static String UTC2Local(String sDateIn) {
        
		String sDateOut="";

        try {
            //String dateStr = "2010-06-14 02:21:49-0400";
            //sDateIn=dateStr;
            
            SimpleDateFormat sdf =  new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
            TimeZone tz = TimeZone.getDefault();
            sdf.setTimeZone(tz);
            Date date = sdf.parse(sDateIn);

            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sDateOut = sdf.format(date);

            
            //System.out.println(newDateStr);
        
        } catch (java.text.ParseException e) {
			
			e.printStackTrace();
		}
        
        return sDateOut;
    }	

	
	
}
