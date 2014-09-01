
package com.google.wolff.androidhunt;

import android.annotation.SuppressLint;
import android.os.Bundle;

public class VictoryActivity extends BaseActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_victory);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Hunt hunt = Hunt.getHunt(getResources(), getApplicationContext());

	}
}
