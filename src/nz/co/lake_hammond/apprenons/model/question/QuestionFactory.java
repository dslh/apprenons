package nz.co.lake_hammond.apprenons.model.question;

import nz.co.lake_hammond.apprenons.model.Traduction;

/**
 * Interface for things that turn {@link Traduction}
 * objects into {@link Question}s.
 * 
 * @author doug
 */
public interface QuestionFactory {
	
	/**
	 * Takes a translation and produces a related
	 * question.
	 * 
	 * @param translation the source material.
	 * @return a question
	 */
	Question makeQuestion(Traduction translation);
	
}
