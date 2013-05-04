package nz.co.lakehammond.apprenons.model.question;

import java.util.ArrayList;
import java.util.List;

import nz.co.lakehammond.apprenons.model.Traduction;
import nz.co.lakehammond.apprenons.model.question.Question.Answer;
import nz.co.lakehammond.apprenons.model.question.Question.OnAnsweredListener;

/**
 * A queue that shuffles answered questions back into
 * the queue in a random position based on whether the
 * question was answered correctly or not. Incorrectly
 * answered questions are put somewhere near the front
 * of the queue, while correct answers are put near the
 * back. 
 * 
 * @author doug
 *
 */
public class ShufflingQuestionQueue implements QuestionQueue {
	private final class ReinsertQuestionCallback implements
			OnAnsweredListener {
		@Override
		public void onAnswered(Answer answer) {
			if (answer.isCorrect())
				translations.add((int) (
						Math.random() * translations.size() * 0.1),
						question.getTranslation());
			else
				translations.add(
						translations.size() - 3
						- (int) (translations.size() * 0.05),
						question.getTranslation());
			
			question = null;
		}
	}

	private final List<Traduction> translations;
	private final QuestionFactory factory;
	private Question question = null;

	public ShufflingQuestionQueue(List<Traduction> translations, QuestionFactory factory) {
		this.factory = factory;
		this.translations = new ArrayList<Traduction>(translations);
		
		if (translations == null ||
			translations.isEmpty())
			throw new IllegalArgumentException("No translations given!");
	}

	@Override
	public Question nextQuestion() {
		if (question != null)
			throw new IllegalStateException("The previous question has not yet been answered.");
		
		question = factory.makeQuestion(translations.get(translations.size() - 1));
		question.setOnAnsweredListener(new ReinsertQuestionCallback());
		return question;
	}
}
