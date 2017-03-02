package com.example.speeddial;

import java.io.ByteArrayOutputStream;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class Info extends Activity {

	AlertDialog.Builder aobj;

	private final int gall = 1;
	private final int con = 2;
	private final int pcrop = 3;

	ImageView img;
	String name, phone;
	EditText n, p;

	int pos;

	private String ipath = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data);
		img = (ImageView) findViewById(R.id.imageView);
		n = (EditText) findViewById(R.id.editText1);
		p = (EditText) findViewById(R.id.editText2);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		pos = b.getInt("position");

		// Toast.makeText(getBaseContext(), ""+pos, Toast.LENGTH_SHORT).show() ;
		if (pos != -1) {
			@SuppressWarnings("deprecation")
			Cursor ic = managedQuery(Database.Contenturi, null, null, null,
					null);
			ic.moveToPosition(pos);
			n.setText(ic.getString(ic.getColumnIndex(Database.contact.NAME)));
			p.setText(ic.getString(ic.getColumnIndex(Database.contact.PHONE)));
			String path = ic.getString(ic
					.getColumnIndex(Database.contact.IMAGE));
			ipath = path;
			 
			 Bitmap bmp = null ;
			 if(path.contains("draw")){
				 bmp=BitmapFactory.decodeResource(getResources(), R.drawable.upload) ;
				 ipath = null ;
				
			 }else{
			bmp = BitmapFactory.decodeFile(path);
			 }
			img.setImageBitmap(bmp);
			
			
		}
		Fragment fr = new fragment1();

		android.app.FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment1, fr);
		fragmentTransaction.commit();

		aobj = new AlertDialog.Builder(this);
		aobj.setMessage("EXIT");
		aobj.setPositiveButton("yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				moveTaskToBack(true);
				Info.this.finish();
			}
		});
		aobj.setNegativeButton("no", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub

			}
		});
		img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				fragment2 fr = new fragment2();
				//Toast.makeText(getBaseContext(), "h", Toast.LENGTH_SHORT).show() ;
						android.app.FragmentManager fm = getFragmentManager();
						FragmentTransaction fragmentTransaction = fm.beginTransaction();
						fragmentTransaction.replace(R.id.fragment1, fr);
						fragmentTransaction.commit();
			}
		}) ;
		

	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_sd, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.help:
			Intent help = new Intent(this, help.class);
			startActivity(help);
			break;
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.discard:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.save:
			phone = p.getText().toString();

			name = n.getText().toString();
			if (ipath == null) {
				Toast.makeText(getBaseContext(), "Select Image" ,
						Toast.LENGTH_SHORT).show();

			} else if (phone.isEmpty()) {
				Toast.makeText(getBaseContext(), "Enter the Phone Number",
						Toast.LENGTH_SHORT).show();

			} else {
				String im = ipath;
				if (pos == -1) {
					ContentValues values = new ContentValues();

					values.put(Database.contact.NAME, name);
					values.put(Database.contact.PHONE, phone);
					values.put(Database.contact.IMAGE, im);

					@SuppressWarnings("unused")
					Uri uri = getContentResolver().insert(Database.Contenturi,
							values);
					Toast.makeText(getBaseContext(), "SUSSESFULLY INSERTED",
							Toast.LENGTH_SHORT).show();

					finish();
				} else {
					ContentValues values = new ContentValues();

					values.put(Database.contact.NAME, name);
					values.put(Database.contact.PHONE, phone);
					values.put(Database.contact.IMAGE, im);
					@SuppressWarnings("deprecation")
					Cursor c = managedQuery(Database.Contenturi, null, null,
							null, null);
					c.moveToPosition(pos);
					int qw = c.getInt(c.getColumnIndex(Database.contact._ID));
					@SuppressWarnings("unused")
					int update = getContentResolver().update(
							Database.Contenturi, values, "_id = " + qw, null);
					Toast.makeText(getBaseContext(), "Updated Successfully",
							Toast.LENGTH_SHORT).show();
					finish();
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog a;
		a = aobj.create();
		a.setMessage("SURE u want to exit ");
		a.show();

	}

	public void getgallery(View view) {
		Thread ga = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, gall);
			}
		});
		try {
			ga.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getcamera(View view) {
		Thread ca = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent job = new Intent(
						android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(job, pcrop);
			}

		});
		try {
			ca.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getcontact(View view) {
		Thread co = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,
						ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, con);
			}
		});
		try {
			co.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void back(View view) {
		Fragment fr = new fragment1();

		android.app.FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.fragment1, fr);
		fragmentTransaction.commit();
	}

	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) throws RuntimeException,SecurityException{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case gall:
				final Uri selectedImage;
				selectedImage = data.getData();

				// TODO Auto-generated method stub
				try {

					Intent crop = new Intent("com.android.camera.action.CROP");
					crop.setDataAndType(selectedImage, "image/*");
					crop.putExtra("crop", true);
					crop.putExtra("aspectX", 1);
					crop.putExtra("aspectY", 1);
					crop.putExtra("outputX", 128);
					crop.putExtra("outputY", 128);
					crop.putExtra("return-data", true);
					startActivityForResult(crop, pcrop);
				} catch (Exception  e) {
					Toast.makeText(getBaseContext(), "SOORY>.....",
							Toast.LENGTH_SHORT).show();
				}

				break;
			case con:
				Uri uriContact;
				uriContact = data.getData();

				Cursor c = managedQuery(uriContact, null, null, null, null);
				
				if (c.moveToFirst()) {
					String id = c
							.getString(c
									.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
					name = c.getString(c
							.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					String hasPhone = c
							.getString(c
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					if (hasPhone.equalsIgnoreCase("1")) {
						Cursor phones = getContentResolver()
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = " + id, null, null);
						phones.moveToFirst();
						String cNumber = phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						Toast.makeText(getApplicationContext(),
								"IMPORTED SUCCESSFULLY", Toast.LENGTH_SHORT)
								.show();
						n.setText(name);
						p.setText(cNumber);
					}
				}

				break;
			case pcrop:
				Bundle extra = data.getExtras();
				Bitmap bmp = (Bitmap) extra.get("data");

				img.setScaleType(ScaleType.FIT_XY);
				img.setImageBitmap(bmp);
				Uri tempUri;
				tempUri = getImageUri(getApplicationContext(), bmp);
				ipath = getRealPathFromURI(tempUri);

				break;

			}
		}

	}

	public Uri getImageUri(Context inContext, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = Images.Media.insertImage(inContext.getContentResolver(),
				inImage, "Title", null);
		return Uri.parse(path);
	}

	public String getRealPathFromURI(Uri uri) {
		Cursor cursor = getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);
	}

}
