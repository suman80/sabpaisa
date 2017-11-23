package in.sabpaisa.droid.sabpaisa.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
	public static SharedPreferences prefs;

	public static void putBoolean(Context ctx, String key, boolean val) {
		prefs = ctx.getSharedPreferences(AppConfiguration.APP_NAME, Context.MODE_PRIVATE);
		prefs.edit().putBoolean(key, val).commit();

	}

	public static boolean getBoolean(Context ctx, String key) {
		prefs = ctx.getSharedPreferences(AppConfiguration.APP_NAME, Context.MODE_PRIVATE);
		return prefs.getBoolean(key, false);
	}

	public static void putInt(Context ctx, String key, int score) {
		prefs = ctx.getSharedPreferences(AppConfiguration.APP_NAME, Context.MODE_PRIVATE);
		prefs.edit().putInt(key, score).commit();

	}

	public static int getInt(Context ctx, String key) {
		prefs = ctx.getSharedPreferences(AppConfiguration.APP_NAME, Context.MODE_PRIVATE);
		
		return prefs.getInt(key, 0);
	}
	
	
}
