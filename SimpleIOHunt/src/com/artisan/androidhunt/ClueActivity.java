/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.artisan.androidhunt;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artisan.incodeapi.ArtisanTrackingManager;

public class ClueActivity extends BaseActivity {

	String lastClueFound;
	String result;

	private ImageView mImgView;
	private TextView mHeading;
	private LinearLayout mTagConainer1;
	private LinearLayout mTagConainer2;
	private LinearLayout mTagConainer3;
	private TextView mTag1NotFound;
	private ImageView mTag1Found;
	private TextView mTag2NotFound;
	private ImageView mTag2Found;
	private TextView mTag3NotFound;
	private ImageView mTag3Found;

	private Handler mHandler = new Handler();

	public Hunt getHunt() {
		return Hunt.getHunt(getResources(), getApplicationContext());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_clue);

		mImgView = (ImageView) findViewById(R.id.imageView1);
		mHeading = (TextView) findViewById(R.id.heading);

		mTagConainer1 = (LinearLayout) findViewById(R.id.tag_container_1);
		mTagConainer2 = (LinearLayout) findViewById(R.id.tag_container_2);
		mTagConainer3 = (LinearLayout) findViewById(R.id.tag_container_3);

		mTag1NotFound = (TextView) findViewById(R.id.tag_1_notfound_marker);
		mTag1Found = (ImageView) findViewById(R.id.tag_1_found_marker);
		mTag2NotFound = (TextView) findViewById(R.id.tag_2_notfound_marker);
		mTag2Found = (ImageView) findViewById(R.id.tag_2_found_marker);
		mTag3NotFound = (TextView) findViewById(R.id.tag_3_notfound_marker);
		mTag3Found = (ImageView) findViewById(R.id.tag_3_found_marker);

		mImgView.setVisibility(View.INVISIBLE);

		mImgView.setAdjustViewBounds(true);
		mImgView.setMaxHeight(250);
	}

	public void onToVictory() {
		Intent intent = new Intent(this, VictoryActivity.class);

		startActivity(intent);
	}

	public void refresh() {
		Hunt hunt = Hunt.getHunt(getResources(), this.getApplicationContext());

		Clue clue = hunt.getCurrentClue();

		if (clue == null) {
			onToVictory();
			finish();
			return;
		}

		if (hunt.getQuestionState() != Hunt.QUESTION_STATE_NONE && clue.question != null) {
			Log.e("WTF", "YOU NEED TO ANSWER A QUESTION");
			Intent newIntent = new Intent(this, TriviaQuestionActivity.class);
			startActivity(newIntent);

		} else if (hunt.getQuestionState() != Hunt.QUESTION_STATE_NONE) {
			// This is also a rare case, usually triggered by swapping the
			// hunt data out
			Log.e("WTF", "Your question hunt state is bad.  I'm fixin' it.");
			hunt.setQuestionState(Hunt.QUESTION_STATE_NONE);
		}

		mImgView.setAdjustViewBounds(true);
		hunt.setClueImage(getResources(), mImgView);

		// Set visibilities
		mImgView.setVisibility(View.VISIBLE);
		View view = findViewById(R.id.clue_progress);
		view.setVisibility(View.INVISIBLE);

		findViewById(R.id.clue_progress).setVisibility(View.INVISIBLE);

		TextView displayText = (TextView) findViewById(R.id.displayText);
		displayText.setText(clue.displayText);

		int clueNumber = hunt.getClueDisplayNumber(clue);
		int totalClues = hunt.getTotalClues();
		String clueTitle = clue.displayName;
		getActionBar().setTitle("Philly Startup Challenge: " + clueNumber + "/" + totalClues);
		mHeading.setText(clueTitle);

		// Display the correct number of circles on the clue
		int tagCount = clue.tags.size();

		switch (tagCount) {
		case 0:
			mTagConainer1.setVisibility(View.GONE);
			mTagConainer2.setVisibility(View.GONE);
			mTagConainer3.setVisibility(View.GONE);
			break;
		case 1:
			mTagConainer1.setVisibility(View.VISIBLE);
			mTagConainer2.setVisibility(View.GONE);
			mTagConainer3.setVisibility(View.GONE);
			break;
		case 2:
			mTagConainer1.setVisibility(View.VISIBLE);
			mTagConainer2.setVisibility(View.VISIBLE);
			mTagConainer3.setVisibility(View.GONE);
			break;
		case 3:
			mTagConainer1.setVisibility(View.VISIBLE);
			mTagConainer2.setVisibility(View.VISIBLE);
			mTagConainer3.setVisibility(View.VISIBLE);
			break;
		default:
			throw new IllegalStateException("Clue tag max of 3 exceeded");
		}

		updateTagDisplay(hunt, clue);
	}

	@Override
	public void onNewIntent(Intent intent) {
		setIntent(intent);
	}

	@Override
	public void onResume() {
		super.onResume();

		Intent intent = getIntent();

		if (intent == null) {
			refresh();
			return;
		}

		lastClueFound = intent.getStringExtra(NavActivity.EXTRA_MESSAGE);
		setIntent(null);

		Hunt hunt = Hunt.getHunt(getResources(), this.getApplicationContext());

		final Clue clue = hunt.getCurrentClue();

		if (clue == null) {
			onToVictory();
			finish();

			return;
		}

		if (lastClueFound != null) {
			refresh();

			result = hunt.findTag(lastClueFound);
			hunt.save(getResources(), getApplicationContext());

			if (result.equals(Hunt.DECOY)) {
				ArtisanTrackingManager.trackEvent("found a decoy");
				hunt.soundManager.play(hunt.soundManager.rejected, this);
				return;
			}

			if (result.equals(Hunt.ACK)) {
				ArtisanTrackingManager.trackEvent("found a clue!");
				updateTagDisplay(hunt, clue);
				hunt.soundManager.play(hunt.soundManager.foundIt, this);
				return;
			}
			if (result.equals(Hunt.ALREADY_FOUND)) {
				ArtisanTrackingManager.trackEvent("found same clue again");
				Toast.makeText(this, "You already found " + lastClueFound + ".  " + clue.getStatus(hunt), Toast.LENGTH_SHORT).show();
				hunt.soundManager.play(hunt.soundManager.repeat, this);
				return;
			}
			if (result.equals(Hunt.WRONG_CLUE)) {
				ArtisanTrackingManager.trackEvent("found the wrong clue");
				Toast.makeText(this, "Tag " + lastClueFound + " is not part of this clue.  " + clue.getStatus(hunt), Toast.LENGTH_SHORT).show();
				hunt.soundManager.play(hunt.soundManager.rejected, this);
				return;
			}
			if (result.equals(Hunt.CLUE_COMPLETE)) {
				ArtisanTrackingManager.trackEvent("found all clues!");
				updateTagDisplay(hunt, clue);

				hunt.soundManager.play(hunt.soundManager.foundItAll, this);
				Toast.makeText(this, "Found it! Hang on for next clue...", Toast.LENGTH_SHORT).show();

				findViewById(R.id.imageView1).setVisibility(View.INVISIBLE);
				findViewById(R.id.clue_progress).setVisibility(View.VISIBLE);

				final Hunt _theHunt = hunt;
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

						if (clue.question != null) {
							Intent newIntent = new Intent(ClueActivity.this, TriviaQuestionActivity.class);
							_theHunt.setQuestionState(Hunt.QUESTION_STATE_QUESTIONING);
							_theHunt.save(getResources(), getApplicationContext());
							startActivity(newIntent);
						} else {
							refresh();
						}
					}
				}, 3000);

				return;
			}
		} else {
			refresh();
		}
	}

	AlertDialog mDialog;

	private void updateTagDisplay(Hunt hunt, Clue clue) {
		int tagsFound = clue.getCluesFound(hunt);
		switch (tagsFound) {
		case 0:
			mTag1NotFound.setVisibility(View.VISIBLE);
			mTag2NotFound.setVisibility(View.VISIBLE);
			mTag3NotFound.setVisibility(View.VISIBLE);
			mTag1Found.setVisibility(View.GONE);
			mTag2Found.setVisibility(View.GONE);
			mTag3Found.setVisibility(View.GONE);
			break;
		case 1:
			mTag1NotFound.setVisibility(View.GONE);
			mTag2NotFound.setVisibility(View.VISIBLE);
			mTag3NotFound.setVisibility(View.VISIBLE);
			mTag1Found.setVisibility(View.VISIBLE);
			mTag2Found.setVisibility(View.GONE);
			mTag3Found.setVisibility(View.GONE);
			break;
		case 2:
			mTag1NotFound.setVisibility(View.GONE);
			mTag2NotFound.setVisibility(View.GONE);
			mTag3NotFound.setVisibility(View.VISIBLE);
			mTag1Found.setVisibility(View.VISIBLE);
			mTag2Found.setVisibility(View.VISIBLE);
			mTag3Found.setVisibility(View.GONE);
			break;
		case 3:
			mTag1NotFound.setVisibility(View.GONE);
			mTag2NotFound.setVisibility(View.GONE);
			mTag3NotFound.setVisibility(View.GONE);
			mTag1Found.setVisibility(View.VISIBLE);
			mTag2Found.setVisibility(View.VISIBLE);
			mTag3Found.setVisibility(View.VISIBLE);
			break;
		default:
			throw new IllegalStateException("Clue tag max of 3 exceeded");

		}
	}
}
