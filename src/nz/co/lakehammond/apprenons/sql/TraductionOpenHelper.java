package nz.co.lakehammond.apprenons.sql;

import static nz.co.lakehammond.apprenons.R.string.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TraductionOpenHelper extends SQLiteOpenHelper {

	private final Context context;
	private SQLiteDatabase db;

	public TraductionOpenHelper(Context context) {
		super(context, TraductionDatabase.DATABASE_NAME,
				null, TraductionDatabase.DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		execResourceSQL(sql_create_phrase_types);
		execResourceSQL(sql_populate_phrase_types);
		execResourceSQL(sql_create_phrase);
		execResourceSQL(sql_create_noun_gender);
		execResourceSQL(sql_create_noun_details);
		execResourceSQL(sql_create_adjective_details);
		execResourceSQL(sql_create_verb_family);
		execResourceSQL(sql_populate_verb_family);
		execResourceSQL(sql_create_verb_transitivity);
		execResourceSQL(sql_populate_verb_transitivity);
		execResourceSQL(sql_create_verb_details);
		execResourceSQL(sql_create_tag);
		execResourceSQL(sql_create_phrase_tag);
	}
	
	public void execResourceSQL(int stringId) {
		db.execSQL(context.getResources().getString(stringId));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		throw new Error("Upgrading traduction database not supported.");
	}

}
