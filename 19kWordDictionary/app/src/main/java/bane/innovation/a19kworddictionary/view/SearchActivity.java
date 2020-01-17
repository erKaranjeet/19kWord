
package bane.innovation.a19kworddictionary.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.IOException;
import java.util.List;

import bane.innovation.a19kworddictionary.R;
import bane.innovation.a19kworddictionary.preference.SettingPreferenceActivity;
import bane.innovation.a19kworddictionary.util.CanonicalMapper;
import bane.innovation.a19kworddictionary.util.DataBaseHelper;
import bane.innovation.a19kworddictionary.util.Word;


public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener {
	
	private final String INTENT_ACTION_1="bane.innovation.a19kworddictionary.CANONICALTRANSLATION";
	private final String INTENT_ACTION_2="bane.innovation.a19kworddictionary.DIRECTTRANSLATION";
	
	private DataBaseHelper db;
	private MenuItem searchViewMenuItem;
	private TextView abTitle;

    private static final String[] COLUMNS = {
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            "TYPE"
    };

    private SuggestionsAdapter mSuggestionsAdapter;
    private SearchView searchView;
	private int autoSuggestionMinLength,TextColor,TextSize;
	private Typeface typeface;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       
        //Create the search view
        searchView = new SearchView(getSupportActionBar().getThemedContext());
        searchView.setQueryHint("جستجو...");
        searchView.setOnQueryTextListener(this);
        searchView.setOnSuggestionListener(this);
        AutoCompleteTextView searchText = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        searchText.setTextColor(TextColor); 
        
        searchViewMenuItem = menu.add("Search");
        searchViewMenuItem.setIcon(R.drawable.ic_action_search)
            .setActionView(searchView)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
       
        menu.add("Settings")
        .setIcon(R.drawable.ic_action_settings)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        
        menu.add("Developers")
        .setIcon(R.drawable.ic_action_about)
        .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        initDataBase();
        BaseFragment retainedInstance=null;
        if(savedInstanceState!=null)
        {
	        String currentTag=savedInstanceState.getString("FinalTag");
	        if(currentTag!=null)
		        retainedInstance = (BaseFragment) getSupportFragmentManager().findFragmentByTag(currentTag);
        }
        
        if(retainedInstance==null)
        {
   		 ActionBar ab = getSupportActionBar();
   		 ab.setHomeButtonEnabled(false);
   		 ab.setDisplayHomeAsUpEnabled(false);
   		 ab.setTitle("لغت نامه نوزده هزار لغت");
        }
        else
        	swipeLeft(retainedInstance);
        
		 int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		 if(titleId == 0)
		     titleId = R.id.action_bar_title;
		 abTitle = (TextView) findViewById(titleId);
		
		 
		 
		 // Get the intent that started this activity
		 Intent intent = getIntent();
		 if(intent.getAction().compareTo(INTENT_ACTION_1)==0)
		 {
	 		 boolean success=false;
				 String query = intent.getStringExtra(Intent.EXTRA_TEXT);
				 if(query!=null)
				 {
					 String key= CanonicalMapper.getCanonicalWord(query);
					 if(key!=null && key.trim().length()>0)
					 {
						 List<Word> resultList=db.search(key);
						 if(resultList!=null && resultList.size()>0)
						 {
							 Intent resultIntent= new Intent();
							 int index=0;
							 for(Word word:resultList)
								 resultIntent.putExtra((++index)+"", new String[]{word.getKey(), word.getValue()});
							 setResult(RESULT_OK, resultIntent);
							 success=true;
						 }
					 }
				 }
			 if(!success)
				 setResult(RESULT_CANCELED, null);
			 finish();
			 
		 }
		 else if(intent.getAction().compareTo(INTENT_ACTION_2)==0)
		 {
			 String query = intent.getStringExtra(Intent.EXTRA_TEXT);
			 if(query!=null)
				 handleNewQuerry(query);
		 }
		 
    }
	
	private void initDataBase() {

		db=new DataBaseHelper(this);
        try { 
        	db.createDataBase();
		 } catch (IOException ioe) { 
			 throw new Error("Unable to create database");
		 }try {
			 db.openDataBase();
		 }catch(SQLException sqle){
			 throw sqle;
		 }
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		BaseFragment currentFragment = (BaseFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container); 
    	if(currentFragment!=null)
    	{
			String tag= currentFragment.getBackStackTag();
			outState.putString("FinalTag", tag);
    	}
	}
	

	@Override
    protected void onResume() {
    	super.onResume();
    	SharedPreferences shpref = PreferenceManager.getDefaultSharedPreferences(this);
		autoSuggestionMinLength = Integer.parseInt(shpref.getString("AUTOSUGGESTIONMINLENGTH", "2"));
		if(autoSuggestionMinLength<2)
			autoSuggestionMinLength=2;
		
		String textFont = shpref.getString("TEXTFONT", "Far_Nazanin.ttf");
		TextColor = shpref.getInt("TEXTCOLOR",getResources().getColor(R.color.default_text_color));
		TextSize = Integer.parseInt(shpref.getString("TEXTSIZE", "25"));
		typeface = Typeface.createFromAsset(getAssets(),"fonts/" + textFont);
		
		if(abTitle!=null)
		{
			abTitle.setTextColor(TextColor);
			abTitle.setTextSize(TextSize);
			abTitle.setTypeface(typeface);
		}
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
    	handleNewQuerry(query);
    	searchViewMenuItem.collapseActionView();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
    	if(query.trim().length()<autoSuggestionMinLength)
    		return false;
    	search(query);
        return false;
    }

    private void search(String newText) {
    	
    	String key=CanonicalMapper.getCanonicalWord(newText);
    	List<String> historyList=db.lookupHistory(key);
    	List<String> suggestionList=db.inquiryCanonicals(key);
    	
        MatrixCursor cursor = new MatrixCursor(COLUMNS,(historyList.size()+suggestionList.size()+1));
        int index=0;
        for(String s:historyList)
        	cursor.addRow(new String[]{(index++)+"", s,"HISTORY",});
       index=0;
        for(String s:suggestionList)
        	cursor.addRow(new String[]{(index++)+"", s,"SUGGESTION"});
        
        mSuggestionsAdapter = new SuggestionsAdapter(getSupportActionBar().getThemedContext(), cursor);
        searchView.setSuggestionsAdapter(mSuggestionsAdapter);
		
	}

	@Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor c = (Cursor) mSuggestionsAdapter.getItem(position);
        String query = c.getString(c.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1));
       // Toast.makeText(this, "Suggestion clicked: " + query, Toast.LENGTH_LONG).show();
        
        handleNewQuerry(query);
        searchViewMenuItem.collapseActionView();
        
    	return true;
    }
    
    public void handleNewQuerry(String query)
    {
    	db.insertToHistory(query);
    	String canonicalQuerry=CanonicalMapper.getCanonicalWord(query);
        BaseFragment newFragment=null;
        List<Integer> wordIds=db.searchForIds(canonicalQuerry);
        if(wordIds==null || wordIds.size()==0)
        	newFragment = new DefinitionFragment(canonicalQuerry);
        else if(wordIds.size()==1)
        	newFragment = new DefinitionFragment(wordIds.get(0),db);
        else
        	newFragment = new ListFragment(canonicalQuerry,canonicalQuerry,db);
        
        swipeLeft(newFragment);
    }
    
    

	public Fragment getExistingFragment(String tag)
    {
    	Fragment fragment=getSupportFragmentManager().findFragmentByTag(tag);
    	
    	return fragment;
    }
    public void swipeLeft(BaseFragment fragment)
    {
    	FragmentManager fm=getSupportFragmentManager();
    	
    	if(fm.getBackStackEntryCount()>0 && 
    			fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1)!=null && 
    			fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().compareTo(fragment.getBackStackTag())==0)
    	{
    		fm.popBackStackImmediate(fragment.getBackStackTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    	
    	}
    	else if(fm.getBackStackEntryCount()>1 && 
    			fm.getBackStackEntryAt(fm.getBackStackEntryCount()-2)!=null && 
    			fm.getBackStackEntryAt(fm.getBackStackEntryCount()-2).getName().compareTo(fragment.getBackStackTag())==0)
    	{
    			fm.popBackStackImmediate(fragment.getBackStackTag(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
    	}
    	else{
    		
    		BaseFragment currentFragment = (BaseFragment)fm.findFragmentById(R.id.fragment_container); 
	    	
    		String tag= currentFragment!=null ? currentFragment.getBackStackTag() : "Developers";
    		fm.beginTransaction()
	    	.setCustomAnimations(R.anim.slide_in_right,	R.anim.slide_out_right,	R.anim.slide_in_left,R.anim.slide_out_left)
			.replace(R.id.fragment_container, fragment,fragment.getBackStackTag())
			.addToBackStack(tag).commit();  
    	}
    	
    	if(getSupportFragmentManager().getBackStackEntryCount()>0)
    	{
    		ActionBar ab = getSupportActionBar();
    		ab.setHomeButtonEnabled(true);
    		ab.setDisplayHomeAsUpEnabled(true);
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	if(item.getTitle().toString().trim().equalsIgnoreCase("Settings"))
    	{
    		startActivity(new Intent(this, SettingPreferenceActivity.class));
    		return true;	
    	}
    	else if(item.getTitle().toString().trim().equalsIgnoreCase("Developers"))
    	{
    		swipeLeft(new DevelopersFragment());	
    	}
    	else if (item.getItemId() == android.R.id.home) {
    		
			if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
				return super.onOptionsItemSelected(item);
			} else {
				getSupportFragmentManager().popBackStack();
			}
			
			if(getSupportFragmentManager().getBackStackEntryCount()<=1)
	    	{   
	    		ActionBar ab = getSupportActionBar();
	    		ab.setHomeButtonEnabled(false);
	    		ab.setDisplayHomeAsUpEnabled(false);
	    		ab.setTitle("لغت نامه نوزده هزار لغت");
	    	}
		}
    	   
    	return super.onOptionsItemSelected(item);
        
    }
    private class SuggestionsAdapter extends CursorAdapter {

        public SuggestionsAdapter(Context context, Cursor c) {
            super(context, c, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View v = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv = (TextView) view;
            final int textIndex = cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1);
            tv.setText("    "+cursor.getString(textIndex));
            tv.setTypeface(typeface);
            tv.setTextColor(TextColor);
            tv.setTextSize(TextSize);
            tv.setBackgroundColor(getResources().getColor(R.color.background_light));
            if(cursor.getString(cursor.getColumnIndex("TYPE")).equalsIgnoreCase("HISTORY"))
            {
            	tv.setTextColor(getResources().getColor(R.color.blue_divider));
            	tv.setBackgroundColor(getResources().getColor(R.color.gray_light_item_bg));
            }
            
           
        }
    }
}
