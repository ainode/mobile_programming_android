package net.cs76.lectures.Storage03;

import net.cs76.lectures.Storage03.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Code3
 * Dan Armendariz
 * Computer Science E-76
 * 
 * Demonstrates saving and querying arbitrary data from a
 * SQLite database.
 */

public class Code3 extends Activity implements OnClickListener {

	DBAdapter db;
	EditText user, pass;

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // instantiate a DBAdapter
        db = new DBAdapter(this);
        
        // open a connection to the DB, and create
        // a table if one does not yet exist.
        db.open();

        // connect to UI elements
        Button auth = (Button)findViewById(R.id.auth);
        Button save = (Button)findViewById(R.id.save);
        user = (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);

        // allow our buttons to do something
        auth.setOnClickListener(this);
        save.setOnClickListener(this);
	
	}


	/** Explicitly close our database connection when our
	 * application is done with it and we're about to quit.
	 */
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}


	/** Perform the requested button action: Save will add a
	 * username and password pair to the SQLite table, and
	 * Authenticate will query the database to see if the 
	 * current user/pass combination are present.
	 */
	public void onClick(View v) {

		// find the username and password that were entered
		String username = user.getText().toString();
		String password = pass.getText().toString();

		// if the "Save" button was pushed, we'll save the user/pass into the DB.
		// otherwise we'll try to 'authenticate' by verifying the user/pass exist in the db
		switch(v.getId()) {
			case R.id.save:

				// insertUser() method will insert a user and return a row ID
				long id = db.insertUser(username, password);
				
				// if the row ID is -1 there was some error, otherwise it was successful
				if (id != -1)
					 displayMessage(username + " inserted!");
				else
					displayMessage(username + " wasn't inserted?"); 

				break;
			case R.id.auth: default:

				// attempt to authenticate a user. It will return true
				// if authenticated or false otherwise.
				if(db.authenticateUser(username, password)) {
					displayMessage(username + " authenticated!");
				} else {
					displayMessage("Authentication failed for "+username + "!");
				}
		}
	}


	/** Display a long Toast as feedback for this Activity.
	 * 
	 * @param msg is the string to display
	 */
	private void displayMessage(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
}