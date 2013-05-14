package nz.co.lakehammond.apprenons;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * An encapsulation of my best effort at making something
 * happen when the "done" button or the enter key is pressed.
 * I'm not sure if this is a complete collection of the ways
 * that a user might try to complete a task while they are
 * entering text so I'm keeping it in one place in case it
 * has to change.
 * 
 * <p>The {@link TextView} to which a listener of this type
 * is registered should have the IME options flag
 * <code>actionDone</code> set. 
 * 
 * @author doug
 *
 */
public abstract class OnDoneEditingListener implements OnEditorActionListener {

	@Override
	public final boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_DONE ||
			(event.getAction() == KeyEvent.ACTION_DOWN
			&& event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
			
			onDone(v);
			return true;
		}
		return false;
	}

	/**
	 * Called when the user has signaled that they are 
	 * done with the <code>TextView</code>.
	 * 
	 * @param v the View being clicked
	 */
	protected abstract void onDone(TextView v);
}
