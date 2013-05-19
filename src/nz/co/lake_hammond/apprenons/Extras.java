package nz.co.lake_hammond.apprenons;

import android.content.Intent;

/**
 * Names for extra information to be passed between
 * activities within the application.
 * 
 * @author doug
 * @see Intent#putExtras(Intent)
 *
 */
public class Extras {

	/**
	 * The number of questions to be asked in a quiz.
	 */
	public static final String QUESTION_COUNT = "nz.co.lake_hammond.apprenons.QuestionCount";
	
	/**
	 * A list of names of categories of words and phrases.
	 */
	public static final String TAG_LIST = "nz.co.lake_hammond.apprenons.TagList";

	/**
	 * Default number of questions to be asked in a quiz if no
	 * value is given.
	 */
	public static final int DEFAULT_QUESTION_COUNT = 10;
	
}
