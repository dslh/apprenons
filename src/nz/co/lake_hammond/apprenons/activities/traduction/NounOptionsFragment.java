package nz.co.lake_hammond.apprenons.activities.traduction;

import nz.co.lake_hammond.apprenons.model.Classification;
import nz.co.lake_hammond.apprenons.model.Classification.NounDetails;
import nz.co.lake_hammond.apprenons.model.Classification.NounDetails.Gender;
import nz.co.lake_hammond.apprenons.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class NounOptionsFragment extends TraductionOptionsFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.noun_options_fragment, container, false);
	}
	
	public void setDetails(Classification  classification) {
		NounDetails details = Classification.nounDetails(classification);
		
		Gender gender = details.getGender();
		if (gender != null) {
			switch (gender) {
			case MASCULINE:
				radioMasculine().setChecked(true);
				break;
			case FEMININE:
				radioFeminine().setChecked(true);
				break;
			}
		}
		
		checkProper().setChecked(details.isProper());
		checkPlural().setChecked(details.isPlural());
	}
	
	public Classification getClassification() {
		Gender gender = null;
		if (radioMasculine().isChecked())
			gender = Gender.MASCULINE;
		else if (radioFeminine().isChecked())
			gender = Gender.FEMININE;
		
		return Classification.noun(gender,
				checkProper().isChecked(), checkPlural().isChecked());
	}

	private RadioButton radioFeminine() {
		return (RadioButton) getView().findViewById(R.id.radioFeminine);
	}

	private RadioButton radioMasculine() {
		return (RadioButton) getView().findViewById(R.id.radioMasculine);
	}

	private CheckBox checkPlural() {
		return (CheckBox) getView().findViewById(R.id.checkPlural);
	}

	private CheckBox checkProper() {
		return (CheckBox) getView().findViewById(R.id.checkProper);
	}
}
