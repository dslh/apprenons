package nz.co.lakehammond.apprenons;

import java.util.Arrays;
import java.util.List;

import nz.co.lakehammond.apprenons.model.Language;
import nz.co.lakehammond.apprenons.model.Traduction;
import nz.co.lakehammond.apprenons.model.question.FrenchAndEnglishQuestionFactory;
import nz.co.lakehammond.apprenons.model.question.Question;
import nz.co.lakehammond.apprenons.model.question.QuestionQueue;
import nz.co.lakehammond.apprenons.model.question.ShufflingQuestionQueue;
import nz.co.lakehammond.apprenons.sql.TraductionDatabase;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class QuizActivity extends Activity {

	private TraductionDatabase db;
	private List<Traduction> translations;
	private int questionCount;
	private int questionIndex = 0;
	private QuestionQueue questions;
	private Question question;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		// Show the Up button in the action bar.
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		db = new TraductionDatabase(this);
		String[] tags = getIntent().getStringArrayExtra(Extras.TAG_LIST);
		if (tags == null || tags.length == 0) {
			translations = db.getAllTraductions();
		} else {
			translations = db.getTraductionsByTags(Arrays.asList(tags));
		}
		
		questionCount = getIntent().getIntExtra(Extras.QUESTION_COUNT, Extras.DEFAULT_QUESTION_COUNT);
		progressBar().setMax(questionCount);
		
		questions = new ShufflingQuestionQueue(translations, new FrenchAndEnglishQuestionFactory(translations));
		
		showNextQuestion();
	}

	private void showNextQuestion() {
		question = questions.nextQuestion();
		
		TextView textCorrectAnswer = (TextView) findViewById(R.id.textCorrectAnswer);
		textCorrectAnswer.setText(question.getAnswerText());
		textCorrectAnswer.setVisibility(View.INVISIBLE);
		EditText editAnswer = (EditText) findViewById(R.id.editAnswer);
		editAnswer.setText(null);
		
		TextView textQuestion = (TextView) findViewById(R.id.textQuestion);
		textQuestion.setText(question.getQuestionText());
		ImageView questionLanguage = (ImageView) findViewById(R.id.imageQuestionLanguage);
		ImageView answerLanguage = (ImageView) findViewById(R.id.imageAnswerLanguage);
		if (question.getQuestionLanguage() == Language.ENGLISH) {
			questionLanguage.setImageResource(R.drawable.uk_flag);
			answerLanguage.setImageResource(R.drawable.fr_flag);
		} else {
			questionLanguage.setImageResource(R.drawable.fr_flag);
			questionLanguage.setImageResource(R.drawable.uk_flag);
		}
		showCurrentProgress();
	}

	private void showCurrentProgress() {
		TextView textQuestionCount = (TextView) findViewById(R.id.textQuestionCount);
		textQuestionCount.setText((questionIndex + 1) + " / " + questionCount);
		progressBar().setProgress(questionIndex);
	}

	private ProgressBar progressBar() {
		return (ProgressBar) findViewById(R.id.progressQuiz);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
