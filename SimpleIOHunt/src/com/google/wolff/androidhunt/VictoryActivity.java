
package com.google.wolff.androidhunt;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.artisan.incodeapi.ArtisanTrackingManager;

public class VictoryActivity extends BaseActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_victory);
	}

	@Override
	protected void onResume() {
		ArtisanTrackingManager.trackEvent("saw victory!");
		super.onResume();
	}
}
