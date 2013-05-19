package nz.co.lake_hammond.apprenons.model.question;

import java.util.List;

import nz.co.lake_hammond.apprenons.model.Traduction;

/**
 * A factory that produces fifty/fifty English and
 * French questions.
 * 
 * @author doug
 *
 */
public class FrenchAndEnglishQuestionFactory implements QuestionFactory {

	private final FrenchQuestionFactory frenchFactory;
	private final EnglishQuestionFactory englishFactory;

	public FrenchAndEnglishQuestionFactory(List<Traduction> translations) {
		frenchFactory = new FrenchQuestionFactory();
		englishFactory = new EnglishQuestionFactory(translations);
	}
	
	@Override
	public Question makeQuestion(Traduction translation) {
		if (theCoinSaysHeads())
			return englishFactory.makeQuestion(translation);
		else
			return frenchFactory.makeQuestion(translation);
	}

	private boolean theCoinSaysHeads() {
		return Math.random() < 0.5;
	}
}
