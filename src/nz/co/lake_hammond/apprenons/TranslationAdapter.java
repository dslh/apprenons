package nz.co.lake_hammond.apprenons;

import java.util.List;

import nz.co.lake_hammond.apprenons.model.Traduction;
import nz.co.lake_hammond.apprenons.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TranslationAdapter extends ArrayAdapter<Traduction> {

	private final LayoutInflater inflater;

	public TranslationAdapter(Context context, List<Traduction> translations) {
		super(context, R.layout.translation_list_fragment, translations);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Traduction traduction = getItem(position);
		
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.translation_list_fragment, parent, false);
		} else {
			view = convertView;
		}
		
		TextView french = (TextView) view.findViewById(R.id.textFrench);
		french.setText(traduction.getVersionFrançais());
		
		TextView english = (TextView) view.findViewById(R.id.textTranslations);
		english.setText(traduction.getVersionsAnglaise());
		
		return view;
	}
}
