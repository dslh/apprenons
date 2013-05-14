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
import android.content.res.ColorStateList;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
	private int correctCount = 0;
	private QuestionQueue questions;
	private Question question;

	OnClickListener answerQuestionWhenClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			answerQuestion(editAnswer().getText().toString());
		}
	};
	OnClickListener showNextQuestionWhenClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			showNextQuestion();
		}
	};
	OnClickListener returnToParentWhenClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			NavUtils.navigateUpFromSameTask(QuizActivity.this);
		}
	};
	
	private ColorStateList initialAnswerColors;

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

		
		EditText editAnswer = editAnswer();
		initialAnswerColors = editAnswer.getTextColors();
		
		showNextQuestion();
		
		editAnswer.setOnEditorActionListener(new OnDoneEditingListener() {
			@Override
			protected void onDone(TextView v) {
				if (question.isAnswered())
					showNextQuestion();
				else
					answerQuestion(v.getText().toString());
			}
		});
	}

	private void showNextQuestion() {
		if (questionIndex == questionCount) {
			showQuizResult();
			return;
		}
		
		question = questions.nextQuestion();
		
		resetStyle();
		
		TextView textQuestion = (TextView) findViewById(R.id.textQuestion);
		textQuestion.setText(question.getQuestionText());
		
		ImageView questionLanguage = (ImageView) findViewById(R.id.imageQuestionLanguage);
		ImageView answerLanguage = (ImageView) findViewById(R.id.imageAnswerLanguage);
		if (question.getQuestionLanguage() == Language.ENGLISH) {
			questionLanguage.setImageResource(R.drawable.uk_flag);
			answerLanguage.setImageResource(R.drawable.fr_flag);
			editAnswer().setHint(R.string.text_answer_french);
		} else {
			questionLanguage.setImageResource(R.drawable.fr_flag);
			answerLanguage.setImageResource(R.drawable.uk_flag);
			editAnswer().setHint(R.string.text_answer_english);
		}
		
		showCurrentProgress();
		
		Button answerQuestion = answerQuestionButton();
		answerQuestion.setText(getString(R.string.answer_question));
		answerQuestion.setOnClickListener(answerQuestionWhenClicked);
	}

	private void resetStyle() {
		TextView textCorrectAnswer = textCorrectAnswer();
		textCorrectAnswer.setText(question.getAnswerText());
		textCorrectAnswer.setVisibility(View.INVISIBLE);

		EditText editAnswer = editAnswer();	
		editAnswer.setText(null);
		editAnswer.setEnabled(true);
		editAnswer.setTextColor(initialAnswerColors);
		
		findViewById(R.id.imageStrikeThrough).setVisibility(View.INVISIBLE);;
	}
	
	private void answerQuestion(String answer) {
		if (question.isAnswered())
			return;
		
		questionIndex++;
		EditText editAnswer = editAnswer();
		editAnswer.setEnabled(false);
		TextView correctAnswer = textCorrectAnswer();
		
		if (question.answerQuestion(answer)) {
			correctCount++;
			editAnswer.setTextColor(getResources().getColor(R.color.correct_answer));
			
			correctAnswer.setTextColor(getResources().getColor(R.color.correct_answer));
			correctAnswer.setText(R.string.correct_answer_given);
		} else {
			editAnswer.setTextColor(getResources().getColor(R.color.incorrect_answer));
			correctAnswer.setTextColor(getResources().getColor(R.color.incorrect_answer));
			findViewById(R.id.imageStrikeThrough).setVisibility(View.VISIBLE);
		}
		
		correctAnswer.setVisibility(View.VISIBLE);
		
		Button answerQuestion = answerQuestionButton();
		answerQuestion.setText(getString(R.string.next_question));
		answerQuestion.setOnClickListener(showNextQuestionWhenClicked);
	}
	
	private void showQuizResult() {
		resetStyle();
		progressBar().setProgress(questionCount);

		TextView correctAnswer = textCorrectAnswer();
		correctAnswer.setVisibility(View.VISIBLE);
		correctAnswer.setTextColor(getResources().getColor(R.color.correct_answer));
		correctAnswer.setText(getString(R.string.quiz_final_score, correctCount, questionCount));
		
		EditText editAnswer = editAnswer();
		editAnswer.setText(null);
		editAnswer.setEnabled(false);
		
		Button button = answerQuestionButton();
		button.setText(getString(R.string.exit_quiz));
		button.setOnClickListener(returnToParentWhenClicked);
	}

	private Button answerQuestionButton() {
		return (Button) findViewById(R.id.buttonAnswerQuestion);
	}

	private TextView textCorrectAnswer() {
		return (TextView) findViewById(R.id.textCorrectAnswer);
	}

	private EditText editAnswer() {
		return (EditText) findViewById(R.id.editAnswer);
	}

	private void showCurrentProgress() {
		TextView textQuestionCount = (TextView) findViewById(R.id.textQuizProgress);
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
