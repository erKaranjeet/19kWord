package bane.innovation.a19kworddictionary.view;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import java.util.List;

import bane.innovation.a19kworddictionary.R;
import bane.innovation.a19kworddictionary.util.CanonicalMapper;
import bane.innovation.a19kworddictionary.util.DataBaseHelper;
import bane.innovation.a19kworddictionary.util.StringUtil;
import bane.innovation.a19kworddictionary.util.Word;

public class DefinitionFragment extends BaseFragment {
	private TextView definitionTextView, titleTextView;
	private Word word;
	private DataBaseHelper db;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;
	private boolean linkWords;

	public DefinitionFragment()
	{
		super("");
	}
	public DefinitionFragment(Integer wordId, DataBaseHelper db)
	{
		super(wordId+"");
		this.word = db.getWord(wordId);
		this.db = db;
	}
	public DefinitionFragment(String searchedKey)
	{
		super(searchedKey);
		this.word = null;
		this.db = null;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View view= inflater.inflate(R.layout.detailed_definition_activity,container,false);
		definitionTextView=(TextView)view.findViewById(R.id.detailedDefinitionTextView);
		titleTextView=(TextView)view.findViewById(R.id.titleTextView);
		ActionBar actionBar = ((SearchActivity)getActivity()).getSupportActionBar();
		actionBar.setTitle("");
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String textFont = shpref.getString("DETAILTEXTFONT", "B Mitra.ttf");
		TextColor = shpref.getInt("DETAILTEXTCOLOR",getResources().getColor(R.color.default_detail_text_color));
		TextSize = Integer.parseInt(shpref.getString("DETAILTEXTSIZE", "30"));
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/" + textFont);
		
		linkWords = shpref.getBoolean("LINKWORDS", true);
	
		
		definitionTextView.setTextColor(TextColor);
		definitionTextView.setTextSize(TextSize);
		titleTextView.setTextColor(TextColor);
		titleTextView.setTextSize(TextSize+7);
		if (typeface != null)
		{
			definitionTextView.setTypeface(typeface);
			titleTextView.setTypeface(typeface);
		}
		
		if(word!=null)
		{
			titleTextView.setText(word.getKey());
			if(linkWords)
				formatText(word);
			else
				definitionTextView.setText(word.getValue());
		}
		else 
		{
			titleTextView.setText("موردی یافت نشد");
			definitionTextView.setText("");
			ActionBar actionBar = ((SearchActivity)getActivity()).getSupportActionBar();
			actionBar.setTitle(getBackStackTag());
		}
		
	}
	
	private void formatText(Word word)
	{

		SpannableString ss = new SpannableString(word.getValue());
		int index = 0;
		while (index < word.getValue().length()) {

			int si = -1;
			if (index < word.getValue().length() - 1) {
				if (!StringUtil.isPersianCharacter(ss.charAt(index))
						&& StringUtil.isPersianCharacter(ss.charAt(index + 1)))
					si = ++index;
				else if (StringUtil.isPersianCharacter(ss.charAt(index)))
					si = index++;
				else
					si = -1;
				if (si > -1) {
					while (index<ss.length()-1 && StringUtil.isPersianCharacter(ss.charAt(index)))
						index++;
					final String querry = CanonicalMapper.getCanonicalWord(ss.subSequence(si, index).toString());
					List<Integer> wordIds=db.searchForIds(querry);
					if (wordIds!=null && wordIds.size()> 0) {
						ClickableSpan clickableSpan = new ClickableSpan() {
							@Override
							public void onClick(View textView) {
								((SearchActivity) getActivity()).handleNewQuerry(querry);
							}
						};
						ss.setSpan(clickableSpan, si, index,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
				else
					index++;
			} 
			else
				index++;

		}
		definitionTextView.setText(ss);
		definitionTextView.setMovementMethod(LinkMovementMethod.getInstance());

	}
	
	
}
