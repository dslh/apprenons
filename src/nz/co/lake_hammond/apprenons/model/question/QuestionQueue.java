package nz.co.lake_hammond.apprenons.model.question;

/**
 * Interface for things which produce an
 * endless list of questions to be asked
 * to the user. A <code>QuestionQueue</code>
 * requires that each question is answered
 * before another one is produced.
 * 
 * @author doug
 * @see Question
 *
 */
public interface QuestionQueue {
	/**
	 * Return the next question to be answered.
	 * Will never return null.
	 * If a question has already been retrieved
	 * from this queue, one of {@link Question#answerQuestion(Object)},
	 * {@link Question#onCorrect()}, or {@link Question#onIncorrect()}
	 * must have been called before this method is invoked.
	 * 
	 * @return the next question to be asked
	 * @throws IllegalStateException if the previous question has not yet been answered
	 */
	Question nextQuestion();
}
