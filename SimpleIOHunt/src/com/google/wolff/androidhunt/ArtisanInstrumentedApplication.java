package com.google.wolff.androidhunt;

import com.artisan.application.ArtisanApplication;
import com.artisan.manager.ArtisanManager;
import com.artisan.utils.ArtisanDebug;

public class ArtisanInstrumentedApplication extends ArtisanApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		// GCM_SENDER_ID is your Project Number from the Google Developer Console.
		// More information at http://developer.android.com/google/gcm/gs.html
		ArtisanManager.setPushSenderId("185682788346");

		ArtisanDebug.enableArtisanLog();

		ArtisanManager.startArtisan(this, "540470532b22209451000016");
	}

	/**
	 * Register your Artisan Power Hook variables and Power Hook blocks here
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/power-hooks/
	 * 
	 */
	@Override
	public void registerPowerhooks() {

	}

	/**
	 * Register your Artisan In-code Experiments here
	 * 
	 * For example:
	 * 
	 * ArtisanExperimentManager.registerExperiment("my first experiment");
	 * ArtisanExperimentManager.addVariantForExperiment("blue variation", "my first experiment");
	 * ArtisanExperimentManager.addVariantForExperiment("green variation", "my first experiment");
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/incode-experiments/
	 */
	@Override
	public void registerInCodeExperiments() {

	}

	/**
	 * Register your Artisan User Profile Variables here
	 * 
	 * Examples at http://docs.useartisan.com/dev/android/user-profiles/
	 */
	@Override
	public void registerUserProfileVariables() {

	}
}
