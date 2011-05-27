package gov.lbl.glamm.shared;

import java.util.Collection;



public abstract class GlammUtils {
	
	//********************************************************************************
	
	public static boolean arrayContainsString( String[] array, String target ) {
		for( int i = 0; i < array.length; i++ ) {
			if( array[i].equals( target ) ) {
				return true;
			}
		}
		return false;
	}

	//********************************************************************************

	public static String joinArray ( Object[] a ) {
		return joinArray( a, ",", true );
	}
	
	//********************************************************************************

	public static String joinArray ( Object[] a, String separator, boolean shouldQuote ) {
		
		String	result 	= "";
		String	quote 	= shouldQuote ? "\"" : "";
		
		if ( a != null && a.length > 0 ) {
			for ( int i = 0; i < a.length - 1; i++ ) {
				result += quote + a[i].toString() + quote + separator;
			}

			result += quote + a[a.length - 1].toString() + quote;
		}
		
		
		return result;
	}
		
	//********************************************************************************
	
	public static String joinCollection(Collection<String> a) {
		return joinCollection(a, ",", true);
	}
	
	//********************************************************************************
	
	public static String joinCollection(Collection<String> a, String separator, boolean shouldQuote) {
		String result = "";
		String quote = shouldQuote ? "\"" : "";
		
		if(a != null && a.size() > 0) {
			int i = 0;
			for(Object ai : a) {
				if(i < a.size() - 1) 
					result += quote + ai + quote + separator;
				else
					result += quote + ai + quote;
				i++;
			}
		}
	
		return result;
	}
	
	//********************************************************************************


}