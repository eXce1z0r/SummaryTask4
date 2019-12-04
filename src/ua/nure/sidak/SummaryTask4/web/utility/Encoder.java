package ua.nure.sidak.SummaryTask4.web.utility;

import java.nio.charset.StandardCharsets;

/**
 * Does the job for the EncodingFilter (convert russian symbols from "ISO_8859_1" to UTF-8)
 * @author eXce1z0r
 *
 */
public class Encoder 
{
	public static String encodeString(String string)
	{
		if(string != null)
		{
			return new String(string.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);	
		}
		
		return null;
	}
}
