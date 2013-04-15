package com.timbyr.lib;

import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public abstract class Util {
	public static Drawable getDrawable(Context context, String name, int height) {
		int res = getResource(context, name);
		Drawable editTextDrawable = context.getResources().getDrawable(res);
		Log.i("DRAWABLE", ""+res);
		Bitmap bitmap = ((BitmapDrawable) editTextDrawable).getBitmap();
		Drawable d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, height, height, true));
		d.setBounds(new Rect(0, 0, height, height));
		return d;
	}
	
	public static int getResource(Context context, String name) {
		int identifier = context.getResources().getIdentifier(name.toLowerCase(Locale.getDefault()), "drawable", context.getPackageName());
		Log.d("RESOURCE",name+" "+identifier);
		return identifier;
	}
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static Point getSize(Context context){
		Point size = new Point();
		WindowManager w = ((Activity)context).getWindowManager();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2){
			w.getDefaultDisplay().getSize(size);
		}else{
			Display d = w.getDefaultDisplay(); 
			size.set(d.getWidth(), d.getHeight());
		}
		size.set(size.x, size.y-getStatusBarHeight(context));
		return size;
	}

	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
