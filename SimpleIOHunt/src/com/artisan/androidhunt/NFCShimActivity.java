package com.artisan.androidhunt;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;

import com.artisan.activity.ArtisanActivity;


/**
 * This activity reads incoming NFC messages and redirects them to the right place.
 *
 * @author wolff
 *
 */
public class NFCShimActivity extends ArtisanActivity {
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w("AndroidHunt", "NFCShimActivity: OnCreate");

		super.onCreate(savedInstanceState);

		Intent intent = getIntent();

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			List<String> data = intent.getData().getQueryParameters("c");
			Intent newintent = new Intent(this, ClueActivity.class);
			if (data.size() == 0) {
				Uri uri = intent.getData();
				String note = uri.toString().substring(
						uri.toString().lastIndexOf(':') + 1);
				newintent.putExtra(NavActivity.EXTRA_MESSAGE, note);
				startActivity(newintent);
				return;
			}
			String message = data.get(0); // This should be the tag you found.
			newintent.putExtra(NavActivity.EXTRA_MESSAGE, message);
			startActivity(newintent);
		} else {
			Log.w("AndroidHunt", "Shim: Nothing to intent");
		}

		finish();
	}
}
