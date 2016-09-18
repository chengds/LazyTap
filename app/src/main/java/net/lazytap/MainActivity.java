package net.lazytap;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

	// Variables
	private int counts[] = {0, 0, 0, 0};
	private static String captions[] = {
			"Chores / Work\n%d",
			"Study / Edu\n%d",
			"Fun / Play\n%d",
			"Wasted\n%d"
	};
	private static String datafile = "simpledata";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	protected void onPause() {
		// write updated counts
		SharedPreferences prefs = getApplicationContext()
				.getSharedPreferences(datafile, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt("chores", counts[0]);
		editor.putInt("study", counts[1]);
		editor.putInt("fun", counts[2]);
		editor.putInt("wasted", counts[3]);
		editor.commit();

		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// read counts from preferences
		SharedPreferences prefs = getApplicationContext()
				.getSharedPreferences(datafile, Context.MODE_PRIVATE);
		counts[0] = prefs.getInt("chores", 0);
		counts[1] = prefs.getInt("study", 0);
		counts[2] = prefs.getInt("fun", 0);
		counts[3] = prefs.getInt("wasted", 0);

		updateAllCounts();
	}

	/**
	 * Called when the user clicks on one of the four buttons
	 */
	public void modifyCount(View view) {
		Button b = (Button) view;
		int ident = -1;

		// identify the button
		switch (b.getId()) {
			case R.id.chores:
				ident = 0;
				break;
			case R.id.study:
				ident = 1;
				break;
			case R.id.fun:
				ident = 2;
				break;
			case R.id.wasted:
				ident = 3;
				break;
		}

		// modify the requested count
		Switch s = (Switch) findViewById(R.id.decrease);
		counts[ident] += s.isChecked() ? -1 : 1;
		if (counts[ident] < 0) counts[ident] = 0;

		// update the button text
		b.setText(String.format(captions[ident], counts[ident]));
	}

	/**
	 * Called when the user wants to reset the counters.
	 * It would be better with a widget different than a button, maybe a
	 * swipe-thing, to avoid pushing it by mistake.
	 */
	public void clearCounts(View view) {
		for (int i = 0; i < 4; i++) counts[i] = 0;
		updateAllCounts();

	}

	public void updateAllCounts() {
		// update screen buttons
		Button b = (Button) findViewById(R.id.chores);
		b.setText(String.format(captions[0], counts[0]));
		b = (Button) findViewById(R.id.study);
		b.setText(String.format(captions[1], counts[1]));
		b = (Button) findViewById(R.id.fun);
		b.setText(String.format(captions[2], counts[2]));
		b = (Button) findViewById(R.id.wasted);
		b.setText(String.format(captions[3], counts[3]));
	}
}
