package nz.co.lakehammond.apprenons.model.question;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.text.TextUtils;

import nz.co.lakehammond.apprenons.model.Language;
import nz.co.lakehammond.apprenons.model.Traduction;

/**
 * A factory which produces questions in English that
 * must be answered in French.
 * 
 * @author doug
 * @see QuestionFactory
 *
 */
public class EnglishQuestionFactory implements QuestionFactory {

	private final List<Traduction> translations;

	/**
	 * Create a new factory. A full list of translations that
	 * are currently in use must be provided, in case any of them
	 * have the same English version.
	 * 
	 * @param translations a list of translations in use
	 */
	public EnglishQuestionFactory(List<Traduction> translations) {
		this.translations = translations;
	}
	
	@Override
	public Question makeQuestion(Traduction translation) {
		final String question = pickAnEnglishVersion(translation);
		final Set<String> frenchVersions = collectFrenchAnswers(translation, question);
		
		return new QuestionAdapter(translation) {
			
			@Override
			public boolean isCorrect(Object answer) {
				if (!(answer instanceof String))
					return false;
				
				for (String french : frenchVersions)
					if (french.equalsIgnoreCase((String) answer))
						return true;
				
				return false;
			}
			
			@Override
			public String getQuestionText() {
				return question;
			}
			
			@Override
			public Language getQuestionLanguage() {
				return Language.ENGLISH;
			}
			
			@Override
			public String getAnswerText() {
				return TextUtils.join(", ", frenchVersions);
			}
		};
	}

	private Set<String> collectFrenchAnswers(Traduction translation, final String question) {
		final Set<String> frenchVersions = new HashSet<String>();
		frenchVersions.add(translation.getVersionFrançais());
		for (Traduction t : translations)
			if (t != translation && t.hasVersionAnglaise(question))
				frenchVersions.add(t.getVersionFrançais());
		return frenchVersions;
	}

	private String pickAnEnglishVersion(Traduction translation) {
		final String question = translation.getVersionsAnglaiseList().get(
				(int) (Math.random() * translation.getVersionsAnglaiseList().size()));
		return question;
	}

}
