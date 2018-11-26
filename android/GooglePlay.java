
package org.godotengine.godot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.IntentSender.SendIntentException;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.net.ConnectivityManager;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONException;

public class GooglePlay extends Godot.SingletonBase {

	static public Godot.SingletonBase initialize (Activity p_activity) {
		return new GooglePlay(p_activity);
	}

	public GooglePlay(Activity p_activity) {
		registerClass ("GooglePlay", new String[] {
			"init", "login", "logout", "unlock_achievement",
			"increase_achievement", "show_achievements", "request_highscore",
			"submit_leaderboard", "show_leaderboard", "show_leaderboards",
			"get_version_code", "has_internet", "save_game", "load_game"
		});

		activity = p_activity;
	}

	public boolean has_internet()
	{
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo() != null;
	}
	
	public int get_version_code(final int instanceID) {
		try {
			final PackageInfo pInfo =
			activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);

			return pInfo.versionCode;
		} catch (NameNotFoundException e) { }

		return 0;
	}

	public void init(final int instanceID) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).init(instanceID);
			}
		});
	}

	public void initWithDict(final Dictionary dict, final int instanceID) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).initAdvanced(dict, instanceID);
			}
		});
	}

	public void login() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).connect();
			}
		});
	}

	public void logout() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).disconnect();
			}
		});
	}

	public boolean isConnected() {
		return PlayService.getInstance(activity).isConnected();
	}

	public void save_game(final String game, final String data)
	{
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).save_game(game, data.getBytes());
			}
		});
	}

	public void load_game(final String game)
	{
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).load_game(game);
			}
		});
	}

	public void unlock_achievement(final String id) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).achievement_unlock(id);
			}
		});
	}

	public void increase_achievement(final String id, final int steps) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).achievement_increment(id, steps);
			}
		});
	}

	public void show_achievements() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).achievement_show_list();
			}
		});
	}

	public void request_highscore(final String l_id) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).leaderboard_get(l_id);
			}
		});
	}

	public void submit_leaderboard(final int score, final String l_id) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).leaderboard_submit(l_id, score);
			}
		});
	}

	public void show_leaderboard(final String l_id) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).leaderboard_show(l_id);
			}
		});
	}

	public void show_leaderboards() {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				PlayService.getInstance(activity).leaderboard_show_list();
			}
		});
	}

	protected void onMainActivityResult (int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult: reqCode=" + requestCode + ", resCode=" + resultCode);

		PlayService.getInstance(activity).onActivityResult(requestCode, resultCode, data);
	}

	protected void onMainPause () {
		PlayService.getInstance(activity).onPause();
	}

	protected void onMainResume () {
//		mFirebaseAnalytics.setCurrentScreen(activity, "Main", currentScreen);
		PlayService.getInstance(activity).onResume();
	}

	protected void onMainDestroy () {
		PlayService.getInstance(activity).onStop();
	}

	private static Context context;
	private static Activity activity;

    static final String TAG = "godot";
}
