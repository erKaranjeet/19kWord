package bane.innovation.a19kworddictionary.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.widget.Toast;

import bane.innovation.a19kworddictionary.R;
import bane.innovation.a19kworddictionary.util.DataBaseHelper;


public class ClearHistory extends DialogPreference implements OnClickListener {

	public ClearHistory(Context context, AttributeSet attrs) {
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
				
				DataBaseHelper db=new DataBaseHelper(getContext());
		        try { 
		        	db.openDataBase();
		        	db.clearHistory();
		        	Toast.makeText(getContext(), "سابقه جستجو با موفقیت پاک شد", Toast.LENGTH_SHORT).show();
				 } catch (Exception ioe) { 
					 Toast.makeText(getContext(), "لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
				 }
				
			}
		});
		builder.setNegativeButton("لغو", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
	}

}