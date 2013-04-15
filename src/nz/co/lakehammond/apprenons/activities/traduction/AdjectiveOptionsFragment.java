package nz.co.lakehammond.apprenons.activities.traduction;

import nz.co.lakehammond.apprenons.R;
import nz.co.lakehammond.apprenons.model.Classification;
import nz.co.lakehammond.apprenons.model.Classification.AdjectiveDetails;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class AdjectiveOptionsFragment extends TraductionOptionsFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.adjective_options_fragment, container, false);
	}
	
	public void setDetails(Classification classification) {
		AdjectiveDetails details = Classification.adjectiveDetails(classification);
		
		editFeminine().setText(details.getFeminineForm());
		editPlural().setText(details.getPluralForm());
		editFemininePlural().setText(details.getFemininePluralForm());
	}
	
	public Classification getClassification() {
		String feminine = editFeminine().getText().toString().trim();
		if (feminine.isEmpty())
			feminine = null;
		
		String plural = editPlural().getText().toString().trim();
		if (plural.isEmpty())
			plural = null;
		
		String femininePlural = editFemininePlural().getText().toString().trim();
		if (femininePlural.isEmpty())
			femininePlural = null;
		
		return Classification.adjective(feminine, plural, femininePlural);
	}
	
	private EditText editFeminine() {
		return ((EditText) getView().findViewById(R.id.editFeminine));
	}
	
	private EditText editPlural() {
		return ((EditText) getView().findViewById(R.id.editPlural));
	}
	
	private EditText editFemininePlural() {
		return ((EditText) getView().findViewById(R.id.editFemininePlural));
	}
}
