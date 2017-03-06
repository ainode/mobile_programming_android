package net.cs76.lectures.Threads02;

import net.cs76.lectures.Threads02.R;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/*
 * Code2
 * Dan Armendariz
 * Computer Science E-76
 * 
 * Fixing ANR issues by creating a background task.
 */

public class Code2 extends Activity implements OnClickListener {

	// UI objects
	private Button async;
	private ImageView img;
	
	// animation object
	private AnimationDrawable anim;
	
	// the amount of time (in seconds) we should sleep when asked
	private static final int S = 10;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// connect to UI objects
		async = (Button)    findViewById(R.id.button_async);
		img   = (ImageView) findViewById(R.id.anim);
		
		// set click listeners
		async.setOnClickListener(this);
		img.setOnClickListener(this);
		
	}

	/**
	 * Display a message to a user via a toast
	 */
	public void showMessage(CharSequence txt) {
		(Toast.makeText(this, txt, Toast.LENGTH_SHORT)).show();
	}
	
	
	/**
	 * Handle onClick events to UI elements.
	 */
	@Override
	public void onClick(View v) {
		switch(v.getId()) {

		case R.id.anim:
			// start animating when the image is clicked

			// set the image resource to use our animation drawable
			img.setImageResource(R.drawable.smiley);
			
			// cast the drawable to an animation and start it
			anim = (AnimationDrawable) img.getDrawable();
			anim.start();

			break;
			
		case R.id.button_async:

			// do our task in the background!
			new DoSomeTask().execute();

			break;
		
		}
	}


	// a class that will spawn a background thread to perform a task
	// see: http://developer.android.com/reference/android/os/AsyncTask.html
	private class DoSomeTask extends AsyncTask<Void, Void, Void> {

		// this is the method with the task that will be run in 
		// the background thread
		@Override
		protected Void doInBackground(Void... params) {
			SystemClock.sleep(S*1000);
			return null;
		}

		// once the background thread completes, this method 
		// will be called in the UI thread
		protected void onPostExecute(Void params) {
			showMessage("Waited " + S + " seconds!");
		}
	}


}