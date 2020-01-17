package bane.innovation.a19kworddictionary.util;

import java.util.HashSet;
import java.util.Hashtable;

public class CanonicalMapper {
	private static Hashtable<Character, Character>substitutionMap;
	private static HashSet<Character> eliminationMap;
	
	static{

		substitutionMap = new Hashtable<Character, Character>();

		//replace "Arabic Letter Alef Maksura 'ى' " with "Arabic Letter Farsi Yeh 'ی' "
		substitutionMap.put('\u0649', '\u06CC');	 
		//replace "Arabic Letter Yeh 'ي' " with "Arabic Letter Farsi Yeh 'ی' "
		substitutionMap.put('\u064A', '\u06CC');
		//replace "Arabic Letter Yeh with Hamza Above 'ئ' " with "Arabic Letter Farsi Yeh 'ی' "
		substitutionMap.put('\u0626', '\u06CC');
		
		//replace "Arabic Letter Alef with Madda Above 'آ' " with "Arabic Letter Alef 'ا' "
		substitutionMap.put('\u0622', '\u0627');
		//replace "Arabic Letter Alef with Hamza Above 'أ' " with "Arabic Letter Alef 'ا' "
		substitutionMap.put('\u0623', '\u0627');
		//replace "Arabic Letter Alef with Hamza Below 'إ' " with "Arabic Letter Alef 'ا' "
		substitutionMap.put('\u0625', '\u0627');
		
		//replace "Arabic Letter Kaf 'ك' " with "Arabic Letter Keheh 'ک' "
		substitutionMap.put('\u0643', '\u06A9');
		//replace "Arabic Letter Swash Kaf 'ڪ' " with "Arabic Letter Keheh 'ک' "
		substitutionMap.put('\u06AA', '\u06A9');	
		
		//replace "Arabic Letter Waw with Hamza Above 'ؤ' " with "Arabic Letter Waw 'و' "
		substitutionMap.put('\u0624', '\u0648');	
		
		//replace "Arabic Letter Teh Marbuta 'ة' " with "Arabic Letter Heh 'ه' "
		substitutionMap.put('\u0629', '\u0647');
			
		//replace Full Stop with Space	
		substitutionMap.put('\u002E', '\u0020');
		//replace "،" with Space	
		substitutionMap.put('\u060C', '\u0020');	
		
	}
	 
	static{
		eliminationMap=new HashSet<Character>();
		
		//eliminate "Arabic Fathatan  ً "
		eliminationMap.add('\u064B');
		//eliminate "Arabic Dammatan  ٌ "
		eliminationMap.add('\u064C');
		//eliminate "Arabic Kasratan  ٍ "
		eliminationMap.add('\u064D');
		//eliminate "Arabic Fatha  َ "
		eliminationMap.add('\u064E');
		//eliminate "Arabic Damma  ُ "
		eliminationMap.add('\u064F');
		//eliminate "Arabic Kasra  ِ "
		eliminationMap.add('\u0650');
		//eliminate "Arabic Shadda  ّ "
		eliminationMap.add('\u0651');
		//eliminate "Arabic Sukun  ْ "
		eliminationMap.add('\u0652');
		//eliminate "Arabic Maddah Above  ٓ"
		eliminationMap.add('\u0653');
		//eliminate "Arabic Hamza Above  ٔ "
		eliminationMap.add('\u0654');
		//eliminate "Arabic Hamza Below  ٕ"
		eliminationMap.add('\u0655');
		
	}
	
	public static String getCanonicalWord(String word)
	{
		word = word.trim();
		StringBuilder canonicalKey=new StringBuilder(word.length());
		for(int i=0; i<word.length();i++)
		{
			char indexChar=word.charAt(i);
			if(substitutionMap.get(indexChar)!=null)
				canonicalKey.append(substitutionMap.get(indexChar));
			else if(!eliminationMap.contains(indexChar))
				canonicalKey.append(indexChar);
		}
		return canonicalKey.toString().trim();
	}
	

}
