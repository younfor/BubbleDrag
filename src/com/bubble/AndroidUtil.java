package com.bubble;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;

public class AndroidUtil {
	public static int getDeviceHeight(Context context) 
	{
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }
	public static int getTopBarHeight(Activity activity)
	{
		Class<?> c = null;

		Object obj = null;

		Field field = null;

		int x = 0, sbar = 0;

		try {

			c = Class.forName("com.android.internal.R$dimen");

			obj = c.newInstance();

			field = c.getField("status_bar_height");

			x = Integer.parseInt(field.get(obj).toString());

			sbar = activity.getResources().getDimensionPixelSize(x);

		} catch (Exception e1) {

			e1.printStackTrace();

		}

		return sbar;
	}
}
