package xjon.hearu.alarm;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import xjon.hearu.core.Alarm;
import xjon.hearu.database.AlarmDbAdapter;
import xjon.hearu.utility.Constants;
import xjon.hearu.utility.Tools;

/**
 * This class is used to start up relevant services when phone is started.
 * 
 */
public class RebootCompletedReceiver extends BroadcastReceiver {

	AlarmManager am;
	SharedPreferences settings;

	@Override
	public void onReceive(final Context context, final Intent intent) {

	
		Tools.setAllAlarms(context);
		
	}
}
