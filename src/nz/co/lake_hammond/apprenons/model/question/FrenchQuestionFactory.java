package nz.co.lake_hammond.apprenons.model.question;

import nz.co.lake_hammond.apprenons.model.Language;
import nz.co.lake_hammond.apprenons.model.Traduction;

/**
 * A factory which produces questions in French that
 * must be answered in English.
 * 
 * @author doug
 * @see QuestionFactory
 */
public class FrenchQuestionFactory implements QuestionFactory {

	@Override
	public Question makeQuestion(Traduction translation) {
		return new QuestionAdapter(translation) {
			
			@Override
			public boolean isCorrect(Object answer) {
				if (!(answer instanceof String))
					return false;
				
				for (String english : getTranslation().getVersionsAnglaiseList())
					if (english.equalsIgnoreCase((String) answer))
						return true;
						
				return false;
			}
			
			@Override
			public String getQuestionText() {
				return getTranslation().getVersionFrançais();
			}
			
			@Override
			public Language getQuestionLanguage() {
				return Language.FRENCH;
			}
			
			@Override
			public String getAnswerText() {
				return getTranslation().getVersionsAnglaise();
			}
		};
	}

}
