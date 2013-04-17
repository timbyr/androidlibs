package com.timbyr.lib.widget;

import android.content.Context;
import android.text.style.SuperscriptSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class RelativeDragLayout extends RelativeLayout {
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}
	
	public RelativeDragLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public RelativeDragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public RelativeDragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}



	

}
