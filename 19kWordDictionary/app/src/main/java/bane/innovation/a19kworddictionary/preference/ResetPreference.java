package bane.innovation.a19kworddictionary.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import java.util.List;

import bane.innovation.a19kworddictionary.R;

public class ResetPreference extends DialogPreference implements OnClickListener {

	// Keeps the font file paths and names in separate arrays
	private List<String> m_fontNames;

	// Font adaptor responsible for redrawing the item TextView with the
	// appropriate font.
	// We use BaseAdapter since we need both arrays, and the effort is quite
	// small.

	public ResetPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		super.onPrepareDialogBuilder(builder);

		String message=getContext().getResources().getString(R.string.reset_setting_preference_dialog);
		builder.setMessage(message);
		// The typical interaction for list-based dialogs is to have
		// click-on-an-item dismiss the dialog
		builder.setPositiveButton("تایید", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
				Editor editor = preferences.edit();
				editor.clear();
				editor.commit();
				dialog.dismiss();
				
			}
		});
		builder.setNegativeButton("لغو", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which >= 0 && which < m_fontNames.size()) {
			String selectedFontPath = m_fontNames.get(which);
			Editor editor = getSharedPreferences().edit();
			editor.putString(getKey(), selectedFontPath);
			editor.commit();

			dialog.dismiss();
		}
	}
}