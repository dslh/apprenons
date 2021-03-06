package nz.co.lake_hammond.apprenons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import nz.co.lake_hammond.apprenons.sql.TraductionDatabase;
import nz.co.lake_hammond.apprenons.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class NewQuizActivity extends Activity {

	private TraductionDatabase db;
	private List<CheckBox> tagBoxes = new ArrayList<CheckBox>();
	
	private static final int[] QUESTION_COUNTS = new int[] {10, 20, 50, 80, 100};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_quiz);
		
		db = new TraductionDatabase(this);
		
		LinearLayout list = quizTagList();
		LayoutParams boxLayout = new ViewGroup.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		for (String tag : db.getAllTags()) {
			CheckBox box = new CheckBox(this);
			
			box.setText(tag);
			box.setTag(tag); // A convenient naming collision...
			
			list.addView(box, boxLayout);
			tagBoxes.add(box);
		}
		
		final CheckBox checkAll = (CheckBox) findViewById(R.id.checkAllTopics);
		checkAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for (CheckBox box : tagBoxes) {
					box.setChecked(checkAll.isChecked());
				}
			}
		});
		
		SeekBar questionCount = seekQuestionCount();
		questionCount.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				updateQuestionCountDisplay();
			}
		});
		updateQuestionCountDisplay();
	}

	protected void updateQuestionCountDisplay() {
		String count = String.valueOf(QUESTION_COUNTS[seekQuestionCount().getProgress()]);
		((TextView) findViewById(R.id.textShowQuestionCount)).setText(count);
	}

	private SeekBar seekQuestionCount() {
		return (SeekBar) findViewById(R.id.seekQuestionCount);
	}

	private LinearLayout quizTagList() {
		return (LinearLayout) findViewById(R.id.quizTagList);
	}
	
	private boolean hasCheckedTags() {
		for (CheckBox box : tagBoxes)
			if (box.isChecked())
				return true;
		
		return false;
	}
	
	private String[] getCheckedTags() {
		Collection<String> tags = new ArrayList<String>();
		for (CheckBox box : tagBoxes) {
			if (box.isChecked()) {
				tags.add((String) box.getTag());
			}
		}
		return tags.toArray(new String[0]);
	}
	
	public void onAddWords(View v) {
		Intent intent = new Intent(this, TraductionDetailActivity.class);
		intent.putExtra(Extras.TAG_LIST, getCheckedTags());
		startActivity(intent);
	}
	
	public void onStartQuiz(View v) {
		if (hasCheckedTags()) {
			Intent intent = new Intent(this, QuizActivity.class);
			intent.putExtra(Extras.TAG_LIST, getCheckedTags());
			intent.putExtra(Extras.QUESTION_COUNT, QUESTION_COUNTS[seekQuestionCount().getProgress()]);
			startActivity(intent);
		} else {
			Toast.makeText(this, R.string.no_tags_checked, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_quiz, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.backup_database) {
			Intent intent = new Intent(this, BackupService.class);
			startService(intent);
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
