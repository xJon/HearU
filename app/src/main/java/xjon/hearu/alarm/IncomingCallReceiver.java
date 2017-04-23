package xjon.hearu.alarm;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.ArrayList;

import xjon.hearu.core.Contact;
import xjon.hearu.database.AlarmDbAdapter;
import xjon.hearu.utility.Constants;

public class IncomingCallReceiver extends BroadcastReceiver {

	private AlarmDbAdapter dbAdapter;

	@Override
	public void onReceive(final Context context, final Intent intent) {

		SharedPreferences settings = context.getSharedPreferences(Constants.SHARED_PREFS_NAME, Context.MODE_PRIVATE);

		// Needed to see in what way the phone state changed (New call, hang up
		// etc)
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

		// Needed to change the volume
		AudioManager audioMgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

		// The callstate (Ringing, hung up etc)
		int callState = tm.getCallState();
		int alarmId = -1;
		if (callState == TelephonyManager.CALL_STATE_RINGING) {
			Log.i("hearu-data", "incoming number: " + incomingNumber);
			alarmId = settings.getInt(Constants.SCHEDULER_ALARM_ID, -1);
        }
		boolean unmuteOnCall = settings.getBoolean(Constants.SCHEDULER_UNMUTE_ON_CALL, false);
		boolean isMuted = settings.getBoolean(Constants.SCHEDULER_SOUND_MUTED, false);
		boolean enabled = settings.getBoolean(Constants.SCHEDULER_ENABLED, false);
		boolean unmutedByReceiver = settings.getBoolean(Constants.SCHEDULER_UNMUTED_BY_RECEIVER, false);

		if (enabled) {

			// If the sound is not muted, do nothing
			if (isMuted || unmutedByReceiver) {

					Editor editor = settings.edit();

					// Incoming call
					if (callState == TelephonyManager.CALL_STATE_RINGING) {

						dbAdapter = new AlarmDbAdapter(context);
						boolean isWhitelistContactCalling = false;
						dbAdapter.open();
						ArrayList<Contact> contactsArrayList = dbAdapter.getAllContacts();
						Contact[] contactsArray = new Contact[contactsArrayList.size()];
						contactsArray = contactsArrayList.toArray(contactsArray);
						for (Contact c : contactsArray) {
							if (c.getNumber().equals(incomingNumber) && c.getContactAlarmId() == alarmId) {
								isWhitelistContactCalling = true;
								break;
							}
						}
						if (isWhitelistContactCalling || unmuteOnCall)
						{
							// Cache the previous volume lock setting
							editor.putBoolean(Constants.SCHEDULER_CACHED_VOLUME_LOCK, settings.getBoolean(Constants.SCHEDULER_VOLUME_LOCK, false));

							// Disable volume lock
							editor.putBoolean(Constants.SCHEDULER_VOLUME_LOCK, false);
							editor.commit();
							editor.putInt(Constants.SCHEDULER_CACHED_RING_MODE, audioMgr.getRingerMode());
							editor.commit();
							editor.putBoolean(Constants.SCHEDULER_UNMUTED_BY_RECEIVER, true);
							editor.commit();

							Log.d("Ringer mode saved", "" + audioMgr.getRingerMode());
							// Unmute
							audioMgr.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
						}

					} else if (callState == TelephonyManager.CALL_STATE_IDLE) {

						Log.d("Ringer mode loaded", "" + settings.getInt(Constants.SCHEDULER_CACHED_RING_MODE, 0));

						audioMgr.setRingerMode(settings.getInt(Constants.SCHEDULER_CACHED_RING_MODE, 0));

						// Restore volume lock to former state

						editor.putBoolean(Constants.SCHEDULER_VOLUME_LOCK, settings.getBoolean(Constants.SCHEDULER_CACHED_VOLUME_LOCK, false));
						editor.putBoolean(Constants.SCHEDULER_UNMUTED_BY_RECEIVER, false);
						editor.putBoolean(Constants.SCHEDULER_SOUND_MUTED, true);
						editor.commit();

					}
			}
		}

	}
}