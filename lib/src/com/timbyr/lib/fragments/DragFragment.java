package com.timbyr.lib.fragments;

import com.actionbarsherlock.app.SherlockFragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ClipData;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class DragFragment extends SherlockFragment implements OnTouchListener {

	private View selected_item = null;
	private int mDownX = 0;
	private int mDownY = 0;
	private int offset_x = 0;
	private int offset_y = 0;
	private int mContainer;

	public final static int DRAG = 0;
	public final static int DROP = 1;


	@SuppressLint("NewApi")
	public void setOnDragListener(View v, View root, final int flags, Object listener){
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
			//						ViewGroup container = (ViewGroup)getView();
			final ViewGroup container = (ViewGroup)root;
			//			ViewGroup container = (ViewGroup)getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
			if(container!=null){
				Log.i("DRAG",container.getClass().toString());
				Log.i("DRAG",container.getId()+"");
//				container.setOnTouchListener(new View.OnTouchListener() {
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						switch(event.getActionMasked())
//						{
//						case MotionEvent.ACTION_MOVE:
//							int x = (int)event.getX() - offset_x;
//							int y = (int)event.getY() - offset_y;
//							int w = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 100;
//							int h = getActivity().getWindowManager().getDefaultDisplay().getHeight() - 100;
//							if(x > w)
//								x = w;
//							if(y > h)
//								y = h;
//							RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//									new ViewGroup.MarginLayoutParams(
//											RelativeLayout.LayoutParams.WRAP_CONTENT,
//											RelativeLayout.LayoutParams.WRAP_CONTENT));
//							lp.setMargins(x, y, 0, 0);
//							Log.i("MOVE", x+" "+y);
////							selected_item.setLayoutParams(lp);
//							selected_item.setDrawingCacheEnabled(true);
//							selected_item.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//							selected_item.layout(0, 0, selected_item.getMeasuredWidth(), selected_item.getMeasuredHeight());
//							selected_item.buildDrawingCache();
//							Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
//							ImageView image = new ImageView(getActivity());
//							image.setLayoutParams(lp);
//							container.addView(image);
//							break;
//						default:
//							break;
//						}
//						return false;
//					}
//				});
			}
		} else {
			v.setOnDragListener((OnDragListener) listener);
		}
		if(flags == DRAG){
			v.setOnTouchListener(this);
		}
	}

	@SuppressLint("NewApi")
	public void startDrag(View v){
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			ClipData dragData = ClipData.newPlainText((CharSequence)v.getTag(), (CharSequence)v.getTag());
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(dragData, shadowBuilder, v, 0);
		}
		else{
//			ViewGroup container = (ViewGroup) getView().findViewById(mContainer);
//			int x = (int)event.getX() - offset_x;
//			int y = (int)event.getY() - offset_y;
//			int w = getActivity().getWindowManager().getDefaultDisplay().getWidth() - 100;
//			int h = getActivity().getWindowManager().getDefaultDisplay().getHeight() - 100;
//			if(x > w)
//				x = w;
//			if(y > h)
//				y = h;
//			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//			new ViewGroup.MarginLayoutParams(
//					RelativeLayout.LayoutParams.WRAP_CONTENT,
//					RelativeLayout.LayoutParams.WRAP_CONTENT));
//			lp.setMargins(x, y, 0, 0);
//			selected_item.setDrawingCacheEnabled(true);
//			selected_item.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//			selected_item.layout(0, 0, selected_item.getMeasuredWidth(), selected_item.getMeasuredHeight());
//			selected_item.buildDrawingCache();
//			Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
//			ImageView image = new ImageView(getActivity());
//			image.setLayoutParams(lp);
//			container.addView(image);
		}
	}

	public void setContainer(int id){
		mContainer = id;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = 0;
		int y = 0;
		switch(event.getActionMasked()){
		case MotionEvent.ACTION_DOWN:
			offset_x = (int)event.getX();
			offset_y = (int)event.getY();
			selected_item = v;
			mDownX = (int) event.getX();
			mDownY = (int) event.getY();
			Log.i("ACTION_DOWN", event.getX()+" "+event.getY());
			return true;
		case MotionEvent.ACTION_MOVE:
			Log.i("ACTION_MOVE", event.getX()+" "+event.getY());
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
				x = (int) event.getX();
				y = (int) event.getY();
				if(Math.sqrt(((x - mDownX)*(x-mDownX))+((y-mDownY)*(y-mDownY)))>30){
					startDrag(v);
				}

				return true;
			}
		default:
			return false;
		}
	}

}