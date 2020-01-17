package bane.innovation.a19kworddictionary.util;

import java.util.HashSet;

public class StringUtil {
	
	private static HashSet<Character> eliminationMap,SignCharacters;
	
	static{
		SignCharacters=new HashSet<Character>();
		SignCharacters.add('.');
		SignCharacters.add('-');
		SignCharacters.add(':');
		SignCharacters.add(')');
		SignCharacters.add('(');
		SignCharacters.add('\"');
		SignCharacters.add('\'');
		// "،"
		SignCharacters.add('\u060C');
		
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
	
	/*
	 * Insert space between Sign Characters and Alphatic characters if there is no space already 
	 */
	public static String formatSignCharactersWithSpace(String word)
	{
		
		StringBuilder formatedText=new StringBuilder(word.length());
		for(int i=0; i<word.length();i++)
		{
			if(word.charAt(i)==' ')
				formatedText.append(word.charAt(i));
			else if(i<word.length()-1 &&
					SignCharacters.contains(word.charAt(i+1)) && 
					!SignCharacters.contains(word.charAt(i))  )
				formatedText.append(word.charAt(i)+" ");
			else if(i<word.length()-1 &&
					SignCharacters.contains(word.charAt(i)) && 
					!SignCharacters.contains(word.charAt(i+1))  )
				formatedText.append(word.charAt(i)+" ");
			else
				formatedText.append(word.charAt(i));
		}
		return formatedText.toString().trim();
	}

	public static boolean isPersianCharacter(char charAt) {
		if(Character.isLetter(charAt) ||
				eliminationMap.contains(charAt))
			return true;
		else
			return false;
	}

	
	public static String replaceSignCharactersWithSpace(String word)
	{
		
		StringBuilder formatedText=new StringBuilder(word.length());
		for(int i=0; i<word.length();i++)
		{
			if(SignCharacters.contains(word.charAt(i)))
				formatedText.append(" ");
			else
				formatedText.append(word.charAt(i));
		}
		return formatedText.toString().trim();
	}

}
