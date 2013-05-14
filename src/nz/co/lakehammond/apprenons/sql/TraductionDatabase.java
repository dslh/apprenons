package nz.co.lakehammond.apprenons.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nz.co.lakehammond.apprenons.R;
import nz.co.lakehammond.apprenons.model.Classification;
import nz.co.lakehammond.apprenons.model.Classification.AdjectiveDetails;
import nz.co.lakehammond.apprenons.model.Classification.Genre;
import nz.co.lakehammond.apprenons.model.Classification.NounDetails;
import nz.co.lakehammond.apprenons.model.Classification.VerbDetails;
import nz.co.lakehammond.apprenons.model.Classification.NounDetails.Gender;
import nz.co.lakehammond.apprenons.model.Classification.VerbDetails.Group;
import nz.co.lakehammond.apprenons.model.Classification.VerbDetails.Transitivity;
import nz.co.lakehammond.apprenons.model.Traduction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class TraductionDatabase {
	static final String DATABASE_NAME = "traductions";
	static final int DATABASE_VERSION = 1;

	private final Context context;
	private final TraductionOpenHelper dbHelper;
	
	public TraductionDatabase(Context context) {
		this.context = context;
		dbHelper = new TraductionOpenHelper(context);
	}
	
	/**
	 * Get a list of all of the category tags stored
	 * in the database.
	 * 
	 * @return a list of tags as strings
	 */
	public List<String> getAllTags() {
		Cursor cursor = dbHelper.getReadableDatabase().query("tag", new String[] {"tag"}, null, null, null, null, "tag");
		
		List<String> tags = new ArrayList<String>();
		
		while (cursor.moveToNext())
			tags.add(cursor.getString(0));
		
		return tags;
	}
	
	/**
	 * Insert a new category tag into the database.
	 * 
	 * @param tag the tag to be inserted
	 * @return true if the tag was inserted successfully
	 */
	public boolean addTag(String tag) {
		ContentValues values = new ContentValues();
		values.put("tag", tag);
		return dbHelper.getWritableDatabase().insert("tag",null,values) != -1;
	}

	/**
	 * Remove a category tag from the database, and all
	 * references to the tag.
	 * 
	 * @param tag the category to be removed
	 */
	public void deleteTag(String tag) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long id = getTagId(db, tag);
		if (id != -1) {
			db.beginTransaction();
			try {
				String[] args = new String[] {String.valueOf(id)};
				db.delete("phrase_tag", "tag_id=?", args);
				db.delete("tag", "id=?", args);
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}
	
	protected long getTagId(SQLiteDatabase db, String tag) {
		Cursor cursor = db.query("tag", new String[] {"id"},
				"tag=?", new String[] {tag}, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor.getLong(0);
		} else {
			return -1;
		}
	}
	
	/**
	 * Inserts a new French phase along with its
	 * classification, tags and English translations
	 * into the database. 
	 * 
	 * @param traduction the translation to be inserted 
	 * @param tags categories that the phrase falls into
	 * @return the id of the newly added translation or -1 if it could not be added
	 */
	public long addTraduction(Traduction traduction, Collection<String> tags) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.beginTransaction();
		try {
			long id = insertTraduction(db, traduction);
			if (id == -1)
				return -1;
			switch (traduction.getGenre()) {
			case ADJECTIVE:
				insertAdjectiveDetails(db, id, traduction);
				break;
			case ADVERB:
				// No extra details to add
				break;
			case VERB:
				insertVerbDetails(db, id, traduction);
				break;
			case NOUN:
				insertNounDetails(db, id, traduction);
				break;
			}
			
			if (tags != null)
				for (String tag : tags)
					tagPhrase(db, id, tag);
			
			db.setTransactionSuccessful();
			traduction.setId(id);
			return id;
		} finally {
			db.endTransaction();
		}
	}
	
	private void tagPhrase(SQLiteDatabase db, long phraseId, String tag) {
		long tagId = getTagId(db, tag);
		ContentValues values = new ContentValues(2);
		values.put("phrase_id", phraseId);
		values.put("tag_id", tagId);
		
		db.insert("phrase_tag", null, values);
	}

	private void insertAdjectiveDetails(SQLiteDatabase db, long id,
			Traduction traduction) {
		ContentValues values = new ContentValues(4);
		values.put("phrase_id", id);
		
		AdjectiveDetails details = Classification.adjectiveDetails(traduction);
		values.put("feminine_form", details.getFeminineForm());
		values.put("plural", details.getPluralForm());
		values.put("feminine_plural_form", details.getFemininePluralForm());
		
		db.insert("adjective_details", null, values);
	}

	private void insertVerbDetails(SQLiteDatabase db, long id,
			Traduction traduction) {
		ContentValues values = new ContentValues(4);
		values.put("phrase_id", id);
		
		VerbDetails details = Classification.verbDetails(traduction);
		values.put("reflexive", details.isReflexive());
		values.put("family", details.getGroup().toString());
		values.put("transitivity", details.getTransitivity().toString());
		
		db.insert("verb_details", null, values);
	}

	private void insertNounDetails(SQLiteDatabase db, long id,
			Traduction traduction) {
		ContentValues values = new ContentValues(4);
		values.put("phrase_id", id);
		
		NounDetails details = Classification.nounDetails(traduction);
		values.put("proper", details.isProper());
		values.put("plural", details.isPlural());
		values.put("gender", details.getGender().toString());
		
		db.insert("noun_details", null, values);
	}

	protected long insertTraduction(SQLiteDatabase db, Traduction traduction) {
		ContentValues values = new ContentValues(3);
		values.put("phrase", traduction.getVersionFrançais());
		values.put("translation", traduction.getVersionsAnglaise());
		values.put("type", traduction.getGenre().toString());
		return db.insert("phrase", null, values);
	}
	
	/**
	 * Gets a full list of translations stored in the database,
	 * including classification info.
	 * 
	 * @return a list of translations
	 */
	public List<Traduction> getAllTraductions() {
		return getTraductions(null, null);
	}
	
	/**
	 * Get a list of all of the translations which match the
	 * given category tag.
	 * 
	 * @param tag the tag to get matching translations for
	 * @return a list of translations
	 */
	public List<Traduction> getTraductionsByTag(String tag) {
		return getTraductions(
				context.getString(R.string.sql_snippet_phrase_id_matches_tag),
				new String[] {tag});
	}
	
	/**
	 * Get a list of all of the translations which match
	 * any of the given tags.
	 * 
	 * @param tags a list of tags to get translations for
	 * @return a list of matching translations
	 */
	public List<Traduction> getTraductionsByTags(Collection<String> tags) {
		List<String> escaped = new ArrayList<String>();
		for (String tag : tags)
			escaped.add(DatabaseUtils.sqlEscapeString(tag));
		String listedTags = TextUtils.join(",", escaped);
		
		return getTraductions(
				context.getString(R.string.sql_snippet_phrase_id_matches_any_tag)
					.replace("?", listedTags),
				null);
	}
	
	private List<Traduction> getTraductions(String selection,String[] selectionArgs) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("phrase",
				new String[] {"id","phrase","translation", "type"},
				selection, selectionArgs, null, null, "phrase");
		
		Map<Long,Traduction> nouns = new HashMap<Long,Traduction>();
		Map<Long,Traduction> verbs = new HashMap<Long,Traduction>();
		Map<Long,Traduction> adjectives = new HashMap<Long,Traduction>();
		
		List<Traduction> traductions = new ArrayList<Traduction>();
		while (cursor.moveToNext()) {
			
			Traduction traduction = new Traduction(
					cursor.getLong(0),
					cursor.getString(1),
					cursor.getString(2));
			traductions.add(traduction);
			
			switch (Genre.valueOf(cursor.getString(3))) {
			case NOUN:
				nouns.put(traduction.getId(), traduction);
				break;
			case ADJECTIVE:
				adjectives.put(traduction.getId(), traduction);
				break;
			case VERB:
				verbs.put(traduction.getId(), traduction);
				break;
			case ADVERB:
				traduction.setClassification(Classification.adverb());
				break;
			}
			
		}
		
		affixNounDetails(nouns);
		affixAdjectiveDetails(adjectives);
		affixVerbDetils(verbs);
		
		return traductions;
	}

	private void affixVerbDetils(Map<Long, Traduction> verbs) {
		if (verbs.isEmpty())
			return;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("verb_details",
				new String[] {"phrase_id","reflexive",
							"family","transitivity"},
				"phrase_id IN (?)",
				new String[] { TextUtils.join(",", verbs.keySet()) },
				null, null, null);
		
		while (cursor.moveToNext()) {
			verbs.get(cursor.getLong(0)).setClassification(
				Classification.verb(
						Group.valueOf(cursor.getString(2)),
						Transitivity.valueOf(cursor.getString(3)),
						cursor.getInt(1) != 0));
		}
	}

	private void affixAdjectiveDetails(Map<Long, Traduction> adjectives) {
		if (adjectives.isEmpty())
			return;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("adjective_details",
				new String[] {"phrase_id","feminine_form",
							"plural_form","feminine_plural_form"},
				"phrase_id IN (?)",
				new String[] { TextUtils.join(",", adjectives.keySet()) },
				null, null, null);
		
		while (cursor.moveToNext()) {
			adjectives.get(cursor.getLong(0)).setClassification(
				Classification.adjective(
						cursor.getString(1),
						cursor.getString(2),
						cursor.getString(3)));
		}
	}

	private void affixNounDetails(Map<Long, Traduction> nouns) {
		if (nouns.isEmpty())
			return;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query("noun_details",
				new String[] {"phrase_id","proper","plural","gender"},
				"phrase_id IN (?)",
				new String[] { TextUtils.join(",", nouns.keySet()) },
				null, null, null);
		
		while (cursor.moveToNext()) {
			nouns.get(cursor.getLong(0)).setClassification(
				Classification.noun(
						Gender.valueOf(cursor.getString(3)),
						cursor.getInt(1) != 0,
						cursor.getInt(2) != 0));
		}
	}
}
