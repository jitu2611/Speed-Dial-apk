package com.example.speeddial;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

class ImageAdapter extends BaseAdapter {

	ImageView p;
	String smg;
	private Context context;
	Cursor imgc;
	int w ;
	private LruCache<String, Bitmap> mMemoryCache;

	public ImageAdapter(Context localcontext, Cursor c,int Width) {
		// TODO Auto-generated constructor stub
		context = localcontext;
		imgc = c;
		w=Width ;
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in bytes rather than number
				// of items.
				return bitmap.getByteCount();
			}
		};
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgc.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
               		if (arg1 == null) {
			p = new ImageView(context);
			imgc.moveToPosition(arg0);

			smg = imgc.getString(imgc.getColumnIndex(Database.contact.IMAGE));
			Bitmap b = null;
			try {

				Bitmap l  = decodefromdatabase(smg, w/3, 250);
				b = getRoundedShape(l) ;
				p.setImageBitmap(b);
			} catch (Exception e) {
				
				Toast.makeText(context,
						"SOME IMAGES SOURCE ARE DELETED BY YOU",
						Toast.LENGTH_LONG).show();
				 
			}
			if (b == null) {
				p.setImageBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.drawable.missing));
				 ContentValues cv= new ContentValues() ;
				 String imageUri = "drawable://" + R.drawable.missing;
				 cv.put(Database.contact.IMAGE, imageUri) ;
				 int i = imgc.getInt(imgc.getColumnIndex(Database.contact._ID))  ;
				context.getContentResolver().update(Database.Contenturi,cv,"_id = "+ i,null) ;
			}

			p.setScaleType(ImageView.ScaleType.FIT_XY);
			p.setPadding(2, 2,2,2) ;
			p.setLayoutParams(new GridView.LayoutParams(w/3, 250));
		} else {
			p = (ImageView) arg1;
		}

		/*
		 * Bitmap b = BitmapFactory.decodeFile(smg);
		 */

		return p;
	}
	public Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
	    int targetWidth = 200	;
	    int targetHeight = 200 ;
	    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
	                        targetHeight,Bitmap.Config.ARGB_8888);

	    Canvas canvas = new Canvas(targetBitmap);
	    Path path = new Path();
	    path.addCircle(((float) targetWidth - 5) / 2,
	        ((float) targetHeight - 5) / 2,
	        (Math.min(((float) targetWidth), 
	        ((float) targetHeight)) / 2),
	        Path.Direction.CCW);

	    canvas.clipPath(path);
	    Bitmap sourceBitmap = scaleBitmapImage;
	    canvas.drawBitmap(sourceBitmap, 
	        new Rect(0, 0, sourceBitmap.getWidth(),
	        sourceBitmap.getHeight()), 
	        new Rect(0, 0, targetWidth, targetHeight), null);
	    return targetBitmap;
	}
	public Bitmap decodefromdatabase(String path, int reqWidth, int reqHeight) {
		// TODO Auto-generated method stub
		Bitmap bm = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inDither = true;
		options.inPreferredConfig = Config.RGB_565;
		options.inPreferQualityOverSpeed = false;
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);
		addBitmapToMemoryCache(path, bm);

		return bm;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return (Bitmap) mMemoryCache.get(key);
	}

	public int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}
}