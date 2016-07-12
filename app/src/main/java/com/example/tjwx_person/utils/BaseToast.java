package com.example.tjwx_person.utils;



import android.content.Context;
import android.widget.Toast;

public class BaseToast {

	public static void makeShortToast(Context context, String text) {
		if (context != null && text != null && !"".equals(text)) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();

		}

	};

	public static void makeLongToast(Context context, String text) {
		if (context != null && text != null && !"".equals(text)) {
			Toast.makeText(context, text, Toast.LENGTH_LONG).show();
		}
	};

}
