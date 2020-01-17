package bane.innovation.a19kworddictionary.view;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;


import java.util.List;

import bane.innovation.a19kworddictionary.R;
import bane.innovation.a19kworddictionary.util.DataBaseHelper;
import bane.innovation.a19kworddictionary.util.Word;

public class ListFragment extends BaseFragment {
	
	private ListView listView;
	private List<Word> suggestionList;
	private String query;
	private DataBaseHelper db;
	private int TextColor=0, TextSize=0;
	private Typeface typeface;
	
	public ListFragment(String tag,String query,DataBaseHelper db )
	{
		super(tag);
		this.query=query;
		this.db=db;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View v = inflater.inflate(R.layout.canonical_words_activity, container, false);
	    listView=(ListView)v.findViewById(R.id.listView);
	    return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
    	suggestionList=db.search(query);
    	listView.setAdapter(new CanonicalAdapter());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String textFont = shpref.getString("TEXTFONT", "Far_Nazanin.ttf");
		TextColor = shpref.getInt("TEXTCOLOR",getResources().getColor(R.color.default_text_color));
		TextSize = Integer.parseInt(shpref.getString("TEXTSIZE", "25"));
		typeface = Typeface.createFromAsset(getActivity().getAssets(),"fonts/" + textFont);
		
		ActionBar actionBar = ((SearchActivity)getActivity()).getSupportActionBar();
		actionBar.setTitle(query);
		
	}
	
	class CanonicalAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			if(suggestionList!=null)
				return suggestionList.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int index) {
			if(suggestionList!=null)
				return suggestionList.get(index);
			else
				return null;
		}

		@Override
		public long getItemId(int index) {
			return index;
		}

		@Override
		public View getView(int index, View view, ViewGroup viewGroup) {
				
			view=View.inflate(getActivity(), R.layout.canonical_worlds_list_view_item, null);
			TextView rowIdTextView=(TextView)view.findViewById(R.id.rowIdTextView);
			TextView wordTextView=(TextView)view.findViewById(R.id.wordTextView);
			TextView definitionTextView=(TextView)view.findViewById(R.id.definitionTextView);
			rowIdTextView.setText((index+1)+"");
			wordTextView.setText(suggestionList.get(index).getKey());
			definitionTextView.setText(suggestionList.get(index).getValue());
			view.setTag(suggestionList.get(index).getId());
			view.setOnClickListener(onClickListener);
			
			if(index%2!=0)
				view.setBackgroundColor(getResources().getColor(R.color.light_blue));
			
			rowIdTextView.setTextColor(TextColor);
			wordTextView.setTextColor(TextColor);
			definitionTextView.setTextColor(TextColor);
			
			rowIdTextView.setTextSize(TextSize);
			wordTextView.setTextSize(TextSize+3);
			definitionTextView.setTextSize(TextSize-3);
			
			if (typeface != null)
			{
				rowIdTextView.setTypeface(typeface,Typeface.NORMAL);
				wordTextView.setTypeface(typeface,Typeface.BOLD);
				definitionTextView.setTypeface(typeface,Typeface.NORMAL);
			}
			
			return view;
		}
		
	}
	
	OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			try{
				int id= (Integer)view.getTag();
				DefinitionFragment definitionFragment= new DefinitionFragment(id,db);
				SearchActivity activity=(SearchActivity)getActivity();
				activity.swipeLeft(definitionFragment);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}
