package inc.meh.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
 
public class Email extends Activity {
        Button send;
        EditText address, subject, emailtext;
    	private DBHelper dh;


        @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //setContentView(R.layout.main);
        
        setContentView(R.layout.email);
        
        this.dh = new DBHelper(Email.this);
       
        send=(Button) findViewById(R.id.emailsendbutton);
        address=(EditText) findViewById(R.id.emailaddress);
        subject=(EditText) findViewById(R.id.emailsubject);
        emailtext=(EditText) findViewById(R.id.emailtext);
       
        
        send.setOnClickListener(new OnClickListener() {
                       
                        @Override
                        public void onClick(View v) {

                        	//String columnString =   "\"PersonName\",\"Gender\",\"Street1\",\"postOffice\",\"Age\"";
                        	//String dataString   =   "currentUser,userName,gender,currentUser.street1,currentUser.postOFfice,currentUser.age";
                        	
                        	String columnString ="Insertype, Latitude, Longitude, Distance To Previous, Cumulative Distance, Date Created";  
                        	
                        	String combinedString = columnString + "\n"; //+ dataString;

                        	List<String> names = dh.selectAll("coordinateid");
            				StringBuilder sb = new StringBuilder();
            				
            				int i = 0;
            				
            				for (String name : names) {
            					 
            					sb.append(name + ", ");
            					i++;
            					if (i==6) {
            						sb.append("\n");
            						i=0;
            					}
            				}

            				combinedString += sb.toString();
            				//Log.d("EXAMPLE", "names size - " + names.size());
            				
                        	File file   = null;
                        	File root   = Environment.getExternalStorageDirectory();
                        	if (root.canWrite()){
                        	    File dir    =   new File (root.getAbsolutePath() + "/PersonData");
                        	     dir.mkdirs();
                        	     file   =   new File(dir, "Data.csv");
                        	     FileOutputStream out   =   null;
                        	    try {
                        	        out = new FileOutputStream(file);
                        	        } catch (FileNotFoundException e) {
                        	            e.printStackTrace();
                        	        }
                        	        try {
                        	            out.write(combinedString.getBytes());
                        	        } catch (IOException e) {
                        	            e.printStackTrace();
                        	        }
                        	        try {
                        	            out.close();
                        	        } catch (IOException e) {
                        	            e.printStackTrace();
                        	        }
                        	    }
                        	
                        
                        	Uri u1  =   null;
                        	
                        	u1  =   Uri.fromFile(file);

                        	Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        	sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MT Export");
                        	sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
                        	sendIntent.setType("text/html");

                        	//startActivity(sendIntent);
                        	
                        	
                        	
                                 
                                      final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                               
                              //        emailIntent.setType("plain/text");
                                 
                                      emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ address.getText().toString()});
                               
                                      emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject.getText());
                               
                                      //emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailtext.getText());
                                      
                                      //emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, emailtext.getText());
                       
                            //        Email.this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                                      Email.this.startActivity(Intent.createChooser(sendIntent, "Send mail..."));
 
                        }
                });
    }
}