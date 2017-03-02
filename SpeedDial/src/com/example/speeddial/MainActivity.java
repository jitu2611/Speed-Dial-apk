package com.example.speeddial;

import java.util.Locale;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.AdRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {

	GridView gv;

	boolean doublebackexit = false;
	int width ;
	int height ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String tag = "create";
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay(); 
		 width = display.getWidth();
		height = display.getHeight();
		Log.i(tag, "Create");
		LinearLayout r;
		AdView adView = (AdView) findViewById(R.id.adView);

		AdRequest adRequest = new AdRequest.Builder().build() ;

	adView.loadAd(adRequest);
		r = (LinearLayout) findViewById(R.id.rl);
		//r.setBackgroundResource(R.drawable.parchmentbase);
		gv = (GridView) findViewById(R.id.gridview);
       
		LayoutTransition l = new LayoutTransition();
		l.enableTransitionType(LayoutTransition.CHANGING);
		
	
		gv.setLayoutTransition(l);
		
		registerForContextMenu(gv);

		try {
			@SuppressWarnings("deprecation")
			Cursor c = managedQuery(Database.Contenturi, null, null, null, null);
			ImageAdapter a = new ImageAdapter(this, c,width);

			gv.setAdapter(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
		gv.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				Cursor c = managedQuery(Database.Contenturi, null, null, null,
						null);

				c.moveToPosition(arg2);
				String name = c.getString(c
						.getColumnIndex(Database.contact.NAME));
				
				final String phoneno = "tel:"
						+ c.getString(c.getColumnIndex(Database.contact.PHONE));
				
						// TODO Auto-generated method stub
						Intent j = new Intent(
								android.content.Intent.ACTION_CALL, Uri
										.parse(phoneno));
						startActivity(j);
					

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub

		switch (item.getItemId()) {
		case R.id.add:
			int l = -1;
			Bundle a = new Bundle();
			a.putInt("position", l);
			try {
				Intent i = new Intent(MainActivity.this, Info.class);
				i.putExtras(a);
				ActivityOptions opt = ActivityOptions.makeCustomAnimation(
						getBaseContext(), android.R.anim.slide_in_left,
						android.R.anim.fade_out);

				startActivity(i, opt.toBundle());
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		case R.id.aboutme:
			try {
				Intent about = new Intent(this, aboutme.class);
				startActivity(about);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.help:
			try {
				Intent help = new Intent(this, help.class);
				startActivity(help);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}

		return super.onOptionsItemSelected(item);

	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

		try {
			@SuppressWarnings("deprecation")
			Cursor c = managedQuery(Database.Contenturi, null, null, null, null);
			ImageAdapter a = new ImageAdapter(this, c,width);

			gv.setAdapter(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			@SuppressWarnings("deprecation")
			Cursor c = managedQuery(Database.Contenturi, null, null, null, null);
			ImageAdapter a = new ImageAdapter(this, c,width);

			gv.setAdapter(a);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		// return super.onContextItemSelected(item);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		int q = info.position;
		Cursor c = managedQuery(Database.Contenturi, null, null, null, null);
		c.moveToPosition(q);
		int qw = c.getInt(c.getColumnIndex(Database.contact._ID));

		switch (item.getItemId()) {

		case R.id.edit:

			int l = q;
			Bundle bun = new Bundle();
			bun.putInt("position", l);
			try {
				Intent i = new Intent(MainActivity.this, Info.class);
				i.putExtras(bun);
				startActivity(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.del:
			// String[] whereArgs = new String[] {Long.toString(l)};
			@SuppressWarnings("unused")
			int d = getContentResolver().delete(Database.Contenturi,
					"_id = " + qw, null);

			try {
				Cursor ci = managedQuery(Database.Contenturi, null, null, null,
						null);
				ImageAdapter a = new ImageAdapter(this, ci,width);

				gv.setAdapter(a);
			} catch (Exception e) {
				e.printStackTrace();
			}

			Toast.makeText(getBaseContext(), "Deleted Successfully ",
					Toast.LENGTH_SHORT).show();
			break;
		}
		return super.onContextItemSelected(item);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.gridview) {

			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.menu_list, menu);

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (doublebackexit) {
			super.onBackPressed();
			finish();
		} else {
			this.doublebackexit = true;
			Toast.makeText(this, "Please Click BACK Again To Exit",
					Toast.LENGTH_SHORT).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					doublebackexit = false;
				}
			}, 2000);
		}
	}

}
