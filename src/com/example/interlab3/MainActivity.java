package com.example.interlab3;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

	private final String USER_AGENT = "Mozilla/5.0";
	int threads = 0;
	TextView textv;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textv = (TextView) findViewById(R.id.textView1);
		//Could not do make request without this if statement
	    if (android.os.Build.VERSION.SDK_INT > 9) {
	        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	        StrictMode.setThreadPolicy(policy);
	      }
		
	    EditText et = (EditText) findViewById(R.id.editText1);
	    
	    et.addTextChangedListener(new TextWatcher(){
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
			@Override
			public void afterTextChanged(Editable s) {
				new Thread(new Task(threads, s.toString())).start();
				threads++;
				
			}
	    }); 
	   
		
	}
	

    class Task implements Runnable {
    	private int _id; 
    	private String _searchString;
    	
    	Task(int id, String searchString){
    		_id = id;
    		_searchString = searchString;
    	}
    	
        @Override
        public void run() {

            try {
            	sendGet(_id,_searchString);
            	System.out.println("tr�d: " + _id + " klar");
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
	 
			System.out.println("json: " + jsonArray.getString(0));
			
			textv.setText(jsonArray.getString(0));
			
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
