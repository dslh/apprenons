package nz.co.lakehammond.apprenons.model.question;

import nz.co.lakehammond.apprenons.model.Traduction;

/**
 * Base implementation of the {@link Question}
 * interface which takes care of everything except
 * for what the question is and what the answer
 * should be.
 * 
 * @author doug
 *
 */
public abstract class QuestionAdapter implements Question {
	
	private final Traduction translation;
	private boolean answered = false;
	private OnAnsweredListener listener;

	public QuestionAdapter(Traduction translation) {
		this.translation = translation;
	}
	
	@Override
	public final Traduction getTranslation() {
		return translation;
	}

	@Override
	public boolean answerQuestion(Object answer) {
		if (isCorrect(answer)) {
			onCorrect();
			return true;
		} else {
			onIncorrect();
			return false;
		}
	}
	
	private void answerQuestion(boolean correct) {
		if (answered)
			throw new IllegalStateException("question already answered");
		answered = true;
		
		if (listener != null)
			listener.onAnswered(new Answer(correct));
	}

	@Override
	public void onCorrect() {
		answerQuestion(true);
	}

	@Override
	public void onIncorrect() {
		answerQuestion(false);
	}

	@Override
	public final boolean isAnswered() {
		return answered;
	}
	
	@Override
	public final void setOnAnsweredListener(OnAnsweredListener listener) {
		this.listener = listener;
	}
}
