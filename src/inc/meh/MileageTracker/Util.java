package inc.meh.MileageTracker;

public class Util {

	public static double Meters2Miles(double dInput)
	{
		double dOutput=0;
		
		if (dInput!=0)
			dOutput= dInput / 1609.344;
		
		return dOutput;
	}
	
	public static void Meters2Miles(String sInput) {
		
		double dInput = Double.valueOf(sInput).doubleValue();
		
		Meters2Miles(dInput);
		
	}
}
