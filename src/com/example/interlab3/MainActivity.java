package com.example.interlab3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;





import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private final String USER_AGENT = "Mozilla/5.0";
	int idIndex = 0;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Could not do make request without this if statement
	    if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	      }
		
	    EditText et = (EditText) findViewById(R.id.editText1);
	    
	    
	    et.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				new Thread(new Task(idIndex,s.toString())).start();
				idIndex++;
				System.out.println(s);

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
        });
	 	
	}
	

    class Task implements Runnable {
    	private int _id;
    	private String _searchString;
    	TextView tv = (TextView)findViewById(R.id.textView1);
    	
    	Task(int id, String searchString){
    		_id = id;
    		_searchString = searchString;

    	}
    	
        @Override
        public void run() {

            try {

    			sendGet(_id,_searchString);
				
    			/*if(jsonArray.length() > 0){
					tv.setText("yes");
					for(int i = 1; i<jsonArray.length(); i++){
						tv.append(jsonArray.getString(i));
					}
				}*/
            	
            	System.out.println("tråd: " + _id + " klar");
                //Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        private void sendGet(int id, String searchString) throws Exception {
			
			String url = "http://flask-afteach.rhcloud.com/getnames/"+ Integer.toString(id) +"/"+ searchString;
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			JSONObject json = new JSONObject(response.toString());
			JSONArray jsonArray = new JSONArray(json.getString("result"));
			
			System.out.println("length " + jsonArray.length());
			System.out.println("json: " + jsonArray.getString(0));
			//tv.setText(jsonArray.getString(0).toString());
			
			//print result
			System.out.println(response.toString());
			
	 
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

}
