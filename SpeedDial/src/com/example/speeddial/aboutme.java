package com.example.speeddial;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class aboutme extends Activity {
	TextView t;
  String Tag = "ABOUT" ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutme);
		t = (TextView) findViewById(R.id.about);
		Log.i(Tag, "About is called") ;
		t.setText(" JITESH  , Student of MNNIT , COMPUTER SCIENCE 2014 batch");
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

}
