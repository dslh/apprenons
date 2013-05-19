package nz.co.lake_hammond.apprenons.model.question;

import nz.co.lake_hammond.apprenons.model.Language;
import nz.co.lake_hammond.apprenons.model.Traduction;

/**
 * A question to be asked to the user.
 * This is a {@link Traduction} along with a specific
 * question to be asked.
 * 
 * <p>When the user has answered the question, exactly
 * one of {@link #answerQuestion(Object)}, {@link #onCorrect()},
 * or {@link #onIncorrect()} should be called.
 * 
 * @author doug
 * @see QuestionQueue
 *
 */
public interface Question {

	/**
	 * Interface for a callback to be invoked when
	 * the question has been answered.
	 * 
	 * @author doug
	 *
	 */
	public interface OnAnsweredListener {
		/**
		 * Called when the question has been answered.
		 * 
		 * @param answer details about the answer that was given
		 */
		void onAnswered(Answer answer);
	}
	
	/**
	 * Details about an answer that has been given for a question.
	 * 
	 * @author doug
	 *
	 */
	public static class Answer {
		private final boolean correct;

		Answer(boolean correct) {
			this.correct = correct;
		}

		public boolean isCorrect() {
			return correct;
		}
	}
	
	/**
	 * Get the underlying translation from whicn
	 * this question has been derived.
	 * 
	 * @return the underlying translation
	 */
	Traduction getTranslation();
	
	/**
	 * Get the text that should be presented to
	 * the user.
	 * 
	 * @return the question text
	 */
	String getQuestionText();

	/**
	 * Get the language that the question is being asked in.
	 * 
	 * @return the language of the question
	 */
	Language getQuestionLanguage();
	
	/**
	 * Get the text that should be presented to
	 * the user if they answer incorrectly.
	 * 
	 * @return the answer text
	 */
	String getAnswerText();

	/**
	 * Returns whether the given answer satisfies the
	 * question. Does not mark the question as answered.
	 * 
	 * @param answer the answer that the user has provided
	 * @return <code>true</code> if the answer is correct, <code>false</code> otherwise
	 */
	boolean isCorrect(Object answer);
	
	/**
	 * Returns whether or not the given answer satisfies
	 * the question. Marks the question as answered. Calls
	 * {@link #onCorrect()} or {@link #onIncorrect()}
	 * appropriately.
	 * 
	 * @param answer the answer that the user has provided.
	 * @return <code>true</code> if the answer is correct, <code>false</code> otherwise
	 * @throws IllegalStateException if the question has already been answered
	 */
	boolean answerQuestion(Object answer);
	
	/**
	 * Called when the user has answered the question
	 * correctly. Can either be invoked directly, or via
	 * {@link #answerQuestion(Object)}.
	 * 
	 * @throws IllegalStateException if the question has already been answered.
	 */
	void onCorrect();
	
	/**
	 * Called when the user has answered the question
	 * incorrectly. Can either be invoked directly, or
	 * via {@link #answerQuestion(Object)}.
	 * 
	 * @throws IllegalStateException if the question has already been answered.
	 */
	void onIncorrect();
	
	/**
	 * Returns whether or not the question has already
	 * been answered. The question should be considered
	 * answered if any of {@link #answerQuestion(Object)},
	 * {@link #onCorrect()} or {@link #onIncorrect()}.
	 * 
	 * @return <code>true</code> if the question has been answered
	 */
	boolean isAnswered();

	/**
	 * Registers a callback to be invoked when the question has been answered.
	 * 
	 * @param listener the callback to be called when the question has been answered
	 */
	void setOnAnsweredListener(OnAnsweredListener listener);
}
