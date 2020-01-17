package bane.innovation.a19kworddictionary.view;

import androidx.fragment.app.Fragment;


public class BaseFragment extends Fragment {
	
	protected String backStackTag;

	
	
	public BaseFragment(String backStackTag) {
		super();
		this.backStackTag = backStackTag;
	}

	public String getBackStackTag() {
		return backStackTag;
	}

	public void setBackStackTag(String backStackTag) {
		this.backStackTag = backStackTag;
	}

	
}

