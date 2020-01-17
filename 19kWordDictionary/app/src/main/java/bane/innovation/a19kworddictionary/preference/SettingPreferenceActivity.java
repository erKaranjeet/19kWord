package bane.innovation.a19kworddictionary.preference;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;

import bane.innovation.a19kworddictionary.R;


public class SettingPreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_setting);

        CharSequence[] entries = new CharSequence[26];
        CharSequence[] entryValues = new CharSequence[26];
        int index = 0;
        for (int i = 10; i <= 60; i = i + 2) {
            entries[index] = i + "";
            entryValues[index] = i + "";
            index++;
        }

        ListPreference sizes = (ListPreference) findPreference("TEXTSIZE");
        sizes.setEntries(entries);
        sizes.setEntryValues(entryValues);
        sizes.setNegativeButtonText(null);

        ListPreference detailSizes = (ListPreference) findPreference("DETAILTEXTSIZE");
        detailSizes.setEntries(entries);
        detailSizes.setEntryValues(entryValues);
        detailSizes.setNegativeButtonText(null);
    }
}