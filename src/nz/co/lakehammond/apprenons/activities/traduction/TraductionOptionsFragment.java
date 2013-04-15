package nz.co.lakehammond.apprenons.activities.traduction;

import nz.co.lakehammond.apprenons.model.Classification;
import android.app.Fragment;

public abstract class TraductionOptionsFragment extends Fragment {
	/**
	 * Set the user interface on this fragment to match
	 * the details in the given Classification.
	 * 
	 * @param classification the classification to display
	 * @throws IllegalArgumentException if the genre of the classification does not match that of the interface
	 */
	public abstract void setDetails(Classification classification);
	
	/**
	 * Creates a {@link Classification} that matches the details selected
	 * on the interface.
	 * 
	 * @return a new classification object.
	 */
	public abstract Classification getClassification();
}
