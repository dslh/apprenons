package nz.co.lakehammond.apprenons;

import nz.co.lakehammond.apprenons.sql.TraductionDatabase;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;

public class TranslationsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_translations_list);
		
		TextView addTranslation = (TextView) findViewById(R.id.addTranslation);
		addTranslation.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				v.setBackgroundResource(R.drawable.pressed_background);
				return false;
			}
		});
		
		ListView translations = (ListView) findViewById(R.id.listTranslations);
		translations.setAdapter(new TranslationAdapter(this, new TraductionDatabase(this).getAllTraductions()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.translations_list, menu);
		return true;
	}
	
	public void onAddTranslation(View v) {
		v.setBackgroundResource(0);
		startActivity(new Intent(this, TraductionDetailActivity.class));
	}

}
