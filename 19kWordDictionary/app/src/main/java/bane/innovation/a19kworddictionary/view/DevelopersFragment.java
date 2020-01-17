package bane.innovation.a19kworddictionary.view;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import bane.innovation.a19kworddictionary.R;
import bane.innovation.a19kworddictionary.util.HandlePdf;


public class DevelopersFragment extends BaseFragment {

	private TextView tv;

	public DevelopersFragment() {
		this("Developers");
	}

	private DevelopersFragment(String backStackTag) {
		super(backStackTag);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.developers_activity, container,
				false);
		tv = (TextView) v.findViewById(R.id.developersTextView);
		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		SpannableString ss = new SpannableString("Instruction For Developers");

		ClickableSpan clickableSpan = new ClickableSpan() {
			@Override
			public void onClick(View textView) {
				HandlePdf handlePdf = new HandlePdf(getActivity());
				handlePdf.show();
			}
		};
		ss.setSpan(clickableSpan, 0, ss.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv.setText(ss);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}

}
