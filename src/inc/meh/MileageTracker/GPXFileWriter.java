
package inc.meh.MileageTracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import java.util.Locale;

//import me.guillaumin.android.osmtracker.db.DataHelper;
import android.content.Context;
import android.database.Cursor;

/**
 * Writes a GPX file.
 * 
 * @author Nicolas Guillaumin
 *
 */
public class GPXFileWriter {
	
		public Main Main;
		//private DAO dh;

        /**
         * XML header.
         */
        private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
        
        /**
         * GPX opening tag
         */
        private static final String TAG_GPX = "<gpx"
                + " xmlns=\"http://www.topografix.com/GPX/1/1\""
                + " version=\"1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd \">";
        
        /**
         * Date format for a point timestamp.
         */
        private static final SimpleDateFormat POINT_DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        
        /**
         * Writes the GPX file
         * @param trackName Name of the GPX track (metadata)
         * @param cTrackPoints Cursor to track points.
         * @param cWayPoints Cursor to way points.
         * @param target Target GPX file
         * @throws IOException 
         */
        
        
    	public GPXFileWriter(Context context) {
    		
    		// initialize the database
    		//this.dh = new DAO(context);
    		//this.Main = new Main();
    		
    	}
        
        
        public static void writeGpxFile(String trackName, Cursor cTrackPoints, Cursor cWayPoints, File target) throws IOException {
                FileWriter fw = new FileWriter(target);
                
                fw.write(XML_HEADER + "\n");
                fw.write(TAG_GPX + "\n");
                
                writeTrackPoints(trackName, fw, cTrackPoints);
                writeWayPoints(fw, cWayPoints);
                
                fw.write("</gpx>");
                
                fw.close();
        }

        public static void writeGpxFileTrackPoints(String trackName, Cursor cTrackPoints, File target) throws IOException {
            FileWriter fw = new FileWriter(target);
            
            fw.write(XML_HEADER + "\n");
            fw.write(TAG_GPX + "\n");
            
            writeTrackPoints(trackName, fw, cTrackPoints);
            fw.write("</gpx>");
            
            fw.close();
    }
        
        /**
         * Iterates on track points and write them.
         * @param trackName Name of the track (metadata).
         * @param fw Writer to the target file.
         * @param c Cursor to track points.
         * @throws IOException
         */
        public static void writeTrackPoints(String trackName, FileWriter fw, Cursor c) throws IOException {
        		
        	Cursor cursor = c;
                
        		if (cursor.moveToFirst()) {
        			do {

        				StringBuffer out = new StringBuffer();
        				
        				fw.write("\n\t" + "<trk>\n");
        	            fw.write("\t\t" + "<name>Trip " + c.getString(c.getColumnIndex("tripid")) + "</name>" + "\n");
        	            
        	            fw.write("\t\t" + "<trkseg>" + "\n");

        	            out.append("\t\t\t" + "<trkpt lat=\"" 
                                        + c.getDouble(c.getColumnIndex("lat"))
//                        				+ c.getDouble(0)
                        				+ "\" "
                                        + "lon=\"" + c.getDouble(c.getColumnIndex("lon")) 
                                        + "\">\n");

                        out.append("<time>" + Util.UTC2Local(c.getString(c.getColumnIndex("created_date"))) + "</time>\n");

                         out.append("<ele>" + c.getDouble(c.getColumnIndex("elevation")) + "</ele>\n");
                         
                         out.append("<speed>" + c.getDouble(c.getColumnIndex("speed")) + "</speed>\n");
                         
                         out.append("<bearing>" + c.getDouble(c.getColumnIndex("bearing")) + "</bearing>\n");
                
                        
                        //out.append("<time>" + POINT_DATE_FORMATTER.format(new Date(c.getLong(c.getColumnIndex("4")))) + "</time>");
                
               
                out.append("</trkpt>" + "\n");
                fw.write(out.toString());

        		
                fw.write("\t\t" + "</trkseg>" + "\n");
                fw.write("\t" + "</trk>" + "\n");
        			
        			
        			
        			}
        			while (cursor.moveToNext());
        			}
        		if (cursor != null && !cursor.isClosed()) {
        			cursor.close();    
        			}   
        		
            	/*
                fw.write("\t" + "<trk>\n");
                fw.write("\t\t" + "<name>" + trackName + "</name>" + "\n");
                
                fw.write("\t\t" + "<trkseg>" + "\n");

                    
                    while (!c.isAfterLast() ) {
                            StringBuffer out = new StringBuffer();
                            out.append("\t\t\t" + "<trkpt lat=\"" 
                                            //+ c.getDouble(c.getColumnIndex("lat"))
                            				+ c.getDouble(0)
                            				+ "\" "
                                            + "lon=\"" //+ c.getDouble(c.getColumnIndex("lon")) 
                                            + "\">");

                            // out.append("<ele>" + c.getDouble(c.getColumnIndex(DataHelper.Schema.COL_ELEVATION)) + "</ele>");
                    
                            //out.append("<time>" + POINT_DATE_FORMATTER.format(new Date(c.getLong(c.getColumnIndex("4")))) + "</time>");
                    
                   
                    out.append("</trkpt>" + "\n");
                    fw.write(out.toString());
                    
                    c.moveToNext();
                    }
                    */
                    
                        
                
        }
        
        /**
         * Iterates on way points and write them.
         * @param fw Writer to the target file.
         * @param c Cursor to way points.
         * @throws IOException
         */
        public static void writeWayPoints(FileWriter fw, Cursor c) throws IOException {
                while(! c.isAfterLast() ) {
                        StringBuffer out = new StringBuffer();
                        out.append("\t" + "<wpt lat=\""
                                        + c.getDouble(c.getColumnIndex("1")) + "\" "
                                        + "lon=\"" + c.getDouble(c.getColumnIndex("2")) + "\">" + "\n");
                        //out.append("\t\t" + "<ele>" + c.getDouble(c.getColumnIndex(DataHelper.Schema.COL_ELEVATION)) + "</ele>" + "\n");
                    out.append("\t\t" + "<time>" + POINT_DATE_FORMATTER.format(new Date(c.getLong(c.getColumnIndex("5")))) + "</time>" + "\n");
                        out.append("\t\t" + "<name>" + c.getString(c.getColumnIndex("6")) + "</name>" + "\n");
                        //String link = c.getString(c.getColumnIndex(DataHelper.Schema.COL_LINK));
                    //if (link != null) {
                      //  out.append("\t\t" + "<link>" + link + "</link>" + "\n");
                    //}
                    out.append("\t" + "</wpt>" + "\n");
                    
                    fw.write(out.toString());
                    
                    c.moveToNext();
                }
        }
}
