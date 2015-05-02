package com.bubble;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

public class BubbleStable extends RelativeLayout {
    private BubbleMove bubble;
    private boolean isInit=false;
    public BubbleStable(Context context) {
        super(context);
    }

    public BubbleStable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if(!isInit)
		{
			//画原始圆
			isInit=true;
			//创建浮动气泡
	        bubble=new BubbleMove(getContext());
	        int []location = new int[2];
	        getLocationOnScreen(location);
	        bubble.setBasePoint(location[0],location[1]-AndroidUtil.getTopBarHeight((Activity) getContext()));
	        bubble.setRadius(w/2);
	        bubble.setMaxDistance(150);
	        bubble.setText("8");
	        //全屏绑定
	    	attachToWindow(getContext());

		}
		
	}
 
    private void attachToWindow(Context context) {
		WindowManager mWindowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.height = WindowManager.LayoutParams.MATCH_PARENT;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.format = PixelFormat.RGBA_8888;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		mWindowManager.addView(bubble, params);
	}
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	bubble.onTouchEvent(event);
        return true;
    }

}
