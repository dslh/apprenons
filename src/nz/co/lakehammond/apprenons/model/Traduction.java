package nz.co.lakehammond.apprenons.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nz.co.lakehammond.apprenons.model.Classification.Genre;

import android.text.TextUtils;

/**
 * Un mot ou locution qui a une version Françiase et
 * une ou plus des versions Anglaises.
 * 
 * @author Doug
 *
 */
public class Traduction {
	/**
	 * The database id of this translation.
	 * 0 for translations not yet inserted into the database.
	 */
	private long id = 0;
	
	/**
	 * Le mot ou la locution qui sera traducée.
	 */
	private String versionFrançaise;
	
	/**
	 * La liste des versions anglaise du mot francais
	 * ou de la locution francaise.
	 */
	private final List<String> versionsAnglaise = new ArrayList<String>();
	
	/**
	 * The taxonomic group of this word or sentence fragment,
	 * along with any details that are specific to that group.
	 */
	private Classification classification;
	
	public Traduction() {
		versionFrançaise = null;
		classification = null;
	}
	
	/**
	 * Creates a simplified translation with no classification info.
	 * 
	 * @param id the database id of the translation
	 * @param français the French version of the word or phrase
	 * @param anglaise the English version of the word or phrase
	 */
	public Traduction(long id, String français, String anglaise) {
		this.id = id;
		versionFrançaise = français;
		setVersionsAnglaise(anglaise);
	}
	
	/**
	 * Crée une nouvelle traduction avec versions française et
	 * anglaise.
	 * 
	 * @param français la version française
	 * @param anglaise la version anglaise 
	 * @param classification la classification du mot français
	 */
	public Traduction(String français, String anglaise, Classification classification) {
		versionFrançaise = français;
		this.classification = classification;
		setVersionsAnglaise(anglaise);
	}
	
	/**
	 * Set the English translation for this French word or phrase.
	 * Replaces any existing translations. Multiple translations
	 * can be separated by commas.
	 * 
	 * @param versions a comma separated list of English translations.
	 */
	public void setVersionsAnglaise(String versions) {
		versionsAnglaise.clear();
		
		for (String version : versions.split(","))
			versionsAnglaise.add(version.trim());
	}
	
	/**
	 * Add a single English translation to the list for this
	 * French word or phrase. 
	 */
	public void addVersionAnglaise(String version) {
		versionsAnglaise.add(version);
	}
	
	/**
	 * Remove a single English translation from the list for
	 * this French word or phrase.
	 * 
	 * @param version the translation to be removed
	 * @return true if the version existed, false otherwise.
	 */
	public boolean removeVersionAnglaise(String version) {
		return versionsAnglaise.remove(version);
	}
	
	/**
	 * Get the full list of translations for this French
	 * word or phrase.
	 * 
	 * @return a comma separated list of translations.
	 */
	public String getVersionsAnglaise() {
		return TextUtils.join(", ", versionsAnglaise);
	}
	
	/**
	 * Get an array of the translations for this French
	 * word or phrase.
	 * 
	 * @return a {@link List} of translations.
	 */
	public List<String> getVersionsAnglaiseList() {
		return Collections.unmodifiableList(versionsAnglaise);
	}

	/**
	 * Returns whether or not the given string is
	 * among the English versions.
	 * 
	 * @param version the English version to be checked
	 * @return <code>true</code> if the version is present
	 */
	public boolean hasVersionAnglaise(String version) {
		for (String english : versionsAnglaise)
			if (english.equalsIgnoreCase(version))
				return true;
				
		return false;
	}
	
	/**
	 * Donne la version Française de cette traduction.
	 *  
	 * @return la version Francaise
	 */
	public String getVersionFrançais() {
		return versionFrançaise;
	}
	
	/**
	 * Mets la version Française de cette traduction.
	 *  
	 * @param versionFrançais la nouvelle version Francaise
	 */
	public void setVersionFrancais(String versionFrançais) {
		this.versionFrançaise = versionFrançais;
	}

	/**
	 * Get the taxonomic group of this word or sentence fragment,
	 * along with any details that are specific to that group.
	 * 
	 * @return the taxonomic group of the French word
	 */	
	public Classification getClassification() {
		return classification;
	}

	/**
	 * Set the taxonomic group of this word or sentence fragment,
	 * along with any details that are specific to that group.
	 * 
	 * @param the new taxonomic classification for the word
	 */
	public void setClassification(Classification classification) {
		this.classification = classification;
	}
	
	/**
	 * Gets the type for this French word or idiom,
	 * i.e. noun, adjective...
	 * 
	 * @return the type of the word
	 * @see Genre
	 */
	public Genre getGenre() {
		if (classification == null)
			return null;
		
		return classification.getGenre();
	}

	/**
	 * Gets the database id for this translation.
	 * 
	 * @return the database id, or 0 if the translation has not yet been saved
	 */
	public long getId() {
		return id;
	}

	/**
	 * Set the database id of the translation. Should be called
	 * after the translation has been saved to the database.
	 * 
	 * @param id the database id for the translation
	 */
	public void setId(long id) {
		this.id = id;
	}
}
