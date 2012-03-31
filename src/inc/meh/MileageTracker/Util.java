package inc.meh.MileageTracker;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
}
