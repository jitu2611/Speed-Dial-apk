package com.example.speeddial;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class help extends Activity {
	TextView t, et;
	String l;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.help);
		t = (TextView)findViewById(R.id.ehelp) ;
		et = (TextView)findViewById(R.id.emailhelp) ;
		
		 l ="jk26111996@gmail.com" ;
		t.setText("For any help or Suggestion Plz Contact : - " ) ;
		et.setText(l) ;
		et.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_SEND) ;
				i.setData(Uri.parse("mailto:")) ;
				i.setType("text/plain") ;
				i.putExtra(Intent.EXTRA_EMAIL, new String[] { l}) ;
				startActivity(i) ;
			}
		}) ;
	}
}
