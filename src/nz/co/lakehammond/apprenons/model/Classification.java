package nz.co.lakehammond.apprenons.model;

import nz.co.lakehammond.apprenons.model.Classification.NounDetails.Gender;
import nz.co.lakehammond.apprenons.model.Classification.VerbDetails.Group;
import nz.co.lakehammond.apprenons.model.Classification.VerbDetails.Transitivity;

public class Classification {
	private final Genre genre;
	protected final Details details;
	
	private Classification(Genre genre, Details details) {
		this.genre = genre;
		this.details = details;
	}
	
	/**
	 * Gets the classification of this French word
	 * or sentence fragment. i.e. noun, vrb, etc.
	 * 
	 * @return the taxonomic group 
	 */
	public Genre getGenre() {
		return genre;
	}
	
	/**
	 * Create new classification details for a noun.
	 * 
	 * @param gender the gender of the noun
	 * @param proper whether the noun is a proper name or not
	 * @param plural whether the noun is a plural or not
	 * @return a {@link Classification} for the noun
	 */
	public static Classification noun(Gender gender, boolean proper, boolean plural) {
		return new Classification(Genre.NOUN,
				new NounDetails(gender, proper, plural));
	}
	
	/**
	 * Create new classification details for a verb.
	 * 
	 * @param group the conjugative group of the verb
	 * @param transitivity if the verb is transitive or not
	 * @param reflexive if this classification is for the reflexive form of this verb or not
	 * @return a {@link Classification} for the verb
	 */
	public static Classification verb(Group group,
			Transitivity transitivity, boolean reflexive) {
		return new Classification(Genre.VERB,
				new VerbDetails(group, transitivity, reflexive));
	}
	
	/**
	 * Create new classification details for an adjective.
	 * 
	 * @param feminineForm the feminine form for the adjective
	 * @param pluralForm the plural form for the adjective
	 * @param femininePlural the feminine  plural form for the adjective
	 * @return a {@link Classification} for the adjective
	 */
	public static Classification adjective(String feminineForm, String pluralForm, String femininePlural) {
		return new Classification(Genre.ADJECTIVE,
				new AdjectiveDetails(feminineForm, pluralForm, femininePlural));
	}
	
	// Adverbs don't have any details at the moment...
	private static final Classification ADVERB_CLASSIFICATION =
			new Classification(Genre.ADVERB, null);
	/**
	 * Get a classification denoting an adverb. Adverbs have
	 * no specific details.
	 * 
	 * @return a {@link Classification} for adverbs
	 */
	public static Classification adverb() {
		return ADVERB_CLASSIFICATION;
	}
	
	/**
	 * Gets taxonomic details for the given noun,
	 * whether it is masculine or feminine and so on.
	 * 
	 * @param classification the classification to get the details of
	 * @return the taxonomic details of this noun
	 * @throws IllegalArgumentException if the classification does not describe a noun
	 */
	public static NounDetails nounDetails(Classification classification) {
		if (classification.getGenre() != Genre.NOUN)
			throw new IllegalArgumentException(
					"Noun details requested for a " + classification.getGenre());
		
		return (NounDetails) classification.details;
	}
	
	/**
	 * Gets taxonomic details for the given French
	 * noun or nominal sentence fragment,
	 * whether it is masculine or feminine and so on.
	 * 
	 * @param traduction the French noun or nominal locution to get details for
	 * @return the associated taxonomic details
	 * @throws IllegalArgumentException if the French word is not a noun
	 */
	public static NounDetails nounDetails(Traduction traduction) {
		return nounDetails(traduction.getClassification());
	}
	
	/**
	 * Gets taxonomic details for the given French verb,
	 * the conjugative group and so on.
	 * 
	 * @param traduction the French verb to get details for
	 * @return the associated taxonomic details
	 * @throws IllegalArgumentException if the French word is not a verb
	 */
	public static VerbDetails verbDetails(Classification classification) {
		if (classification.getGenre() != Genre.VERB)
			throw new IllegalArgumentException(
					"Verb details requested for a " + classification.getGenre());
		
		return (VerbDetails) classification.details;
	}
	
	/**
	 * Gets taxonomic details for the given French verb,
	 * the conjugative group and so on.
	 * 
	 * @param traduction the French verb to get details for
	 * @return the associated taxonomic details
	 * @throws IllegalArgumentException if the French word is not a verb
	 */	
	public static VerbDetails verbDetails(Traduction traduction) {
		return verbDetails(traduction.getClassification());
	}
	
	/**
	 * Gets taxonomic details for the given French
	 * adjective or adjectival sentence fragment.
	 * 
	 * @param traduction the French adjective to get details for
	 * @return the associated taxonomic details
	 * @throws IllegalArgumentException if the French word is not an adjective
	 */
	public static AdjectiveDetails adjectiveDetails(Classification classification) {
		if (classification.getGenre() != Genre.ADJECTIVE)
			throw new IllegalArgumentException(
					"Adjective details requested for a " + classification.getGenre());
		
		return (AdjectiveDetails) classification.details;
	}
	
	/**
	 * Gets taxonomic details for the given French
	 * adjective or adjectival sentence fragment.
	 * 
	 * @param traduction the French adjective to get details for
	 * @return the associated taxonomic details
	 * @throws IllegalArgumentException if the French word is not an adjective
	 */
	public static AdjectiveDetails adjectiveDetails(Traduction traduction) {
		return adjectiveDetails(traduction.getClassification());
	}
	
	/**
	 * An enumeration of the possible groups a
	 * French word or sentence fragment might fall under.
	 * 
	 * @author Doug
	 */
	public static enum Genre {
		NOUN,
		VERB,
		ADJECTIVE,
		ADVERB;
	}
	
	private static class Details {}
	
	/**
	 * Stores details for French words that are
	 * classified as nouns. May be accessed via
	 * {@link #nounDetails(Classification)}.
	 * 
	 * @author Doug
	 *
	 */
	public static class NounDetails extends Details {
		private final Gender gender;
		private final boolean proper;
		private final boolean plural;

		/**
		 * Enumeration of the different genders of
		 * French nouns. The French language allows
		 * for male and female words.
		 * 
		 * @author Doug
		 *
		 */
		public static enum Gender {
			MASCULINE,
			FEMININE
		}
		
		NounDetails(Gender gender, boolean proper, boolean plural) {
			this.gender = gender;
			this.proper = proper;
			this.plural = plural;
		}

		/**
		 * Gets the gender of the noun. May be null in
		 * the case of proper nouns.
		 * 
		 * @return the gender
		 */
		public Gender getGender() {
			return gender;
		}

		/**
		 * Gets whether the noun is a proper name or not.
		 * 
		 * @return true if the noun is proper
		 */
		public boolean isProper() {
			return proper;
		}

		/**
		 * Gets whether the noun is a plural or not. This refers
		 * to nouns that are always referred to in the plural,
		 * such as vacances or jumelles.
		 * 
		 * @return true if the noun is a plural
		 */
		public boolean isPlural() {
			return plural;
		}
	}
	
	/**
	 * Stores details for French words that are
	 * classified as verbs. May be accessed via
	 * {@link #verbDetails(Classification)}.
	 * 
	 * @author Doug
	 */
	public static class VerbDetails extends Details {
		private final Group group;
		private final Transitivity transitivity;
		private final boolean reflexive;

		/**
		 * Enumerates the conjugative groups
		 * that French verbs may fall into.
		 * 
		 * @author Doug
		 */
		public static enum Group {
			FIRST,
			SECOND,
			THIRD,
			IRREGULAR
		}
		
		/**
		 * Enumerates the possible transitive
		 * properties of French verbs.
		 * 
		 * @author Doug
		 */
		public static enum Transitivity {
			TRANSITIVE,
			INTRANSITIVE,
			BOTH
		}
		
		VerbDetails(Group group,
				Transitivity transitivity,
				boolean reflexive) {
					this.group = group;
					this.transitivity = transitivity;
					this.reflexive = reflexive;
		}

		/**
		 * Gets the conjugative group to which
		 * this verb belongs.
		 * 
		 * @return the conjugative group.
		 */
		public Group getGroup() {
			return group;
		}

		/**
		 * Gets whether this verb is transitive,
		 * intransitive, or both.
		 * 
		 * @return the transitivity of the verb.
		 */
		public Transitivity getTransitivity() {
			return transitivity;
		}

		/**
		 * Gets whether this translation represents
		 * the reflexive mode of this verb or not.
		 * 
		 * @return the reflexivity of this verb.
		 */
		public boolean isReflexive() {
			return reflexive;
		}
	}
	
	public static class AdjectiveDetails extends Details {
		private final String feminineForm;
		private final String pluralForm;
		private final String femininePlural;

		AdjectiveDetails(String feminineForm,
				String pluralForm, String femininePlural) {
			this.feminineForm = feminineForm;
			this.pluralForm = pluralForm;
			this.femininePlural = femininePlural;
		}

		/**
		 * Get the feminine form of this adjective,
		 * or <code>null</code> if it is the same as
		 * the masculine form.
		 * 
		 * @return the feminine form of the adjective
		 */
		public String getFeminineForm() {
			return feminineForm;
		}

		/**
		 * Get the plural form of this adjective, 
		 * or <code>null</code> if it is the same as
		 * the singular form.
		 * 
		 * @return the plural form of the adjective
		 */
		public String getPluralForm() {
			return pluralForm;
		}

		/**
		 * Get the feminine plural form of this adjective, 
		 * or <code>null</code> if it is the same as
		 * the singular form.
		 * 
		 * @return the feminine form of the adjective
		 */
		public String getFemininePluralForm() {
			return femininePlural;
		}
		
		/**
		 * Gets whether this French adjective has a
		 * distinct form when used in conjunction with
		 * a feminine noun.
		 * 
		 * @return <code>true</code> if the feminine form is distinct
		 */
		public boolean hasFeminineForm() {
			return feminineForm != null; 
		}
		
		/**
		 * Gets whether this French adjective has a
		 * distinct form when used in conjunction with
		 * a plural noun.
		 * 
		 * @return <code>true</code> if the plural form is distinct
		 */
		public boolean hasPluralForm() {
			return pluralForm != null; 
		}
		
		/**
		 * Gets whether this French adjective has a
		 * distinct form when used in conjunction with
		 * a feminine plural noun.
		 * 
		 * @return <code>true</code> if the feminine plural form is distinct
		 */
		public boolean hasFemininePluralForm() {
			return femininePlural != null; 
		}
	}
}
