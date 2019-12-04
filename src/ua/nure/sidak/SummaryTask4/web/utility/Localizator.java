package ua.nure.sidak.SummaryTask4.web.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;

/**
 * Used for localization of static pages text
 * @author eXce1z0r
 *
 */
public class Localizator
{	
	private static ClassLoader resourceLoader;
	
	private ResourceBundle bundle;
	
	private static final String NO_CONTENT_FOUND = "<not found>";
	
	public Localizator(HttpServletRequest req)
	{		
		Locale locale = Localizator.getLocaleFromSession(req);
		
		if(locale != null)
		{
			ClassLoader resourceLoader = Localizator.getClassLoader(req);
			
			if(resourceLoader != null)
			{
				bundle = ResourceBundle.getBundle("resources", locale, resourceLoader);
			}
			else
			{
				bundle = ResourceBundle.getBundle("resources", locale);
			}
		}
		else
		{
			bundle = ResourceBundle.getBundle("resources");
		}		
	}
	
	public String getContent(String contentKey)
	{	
		String content = null;
		
		content = bundle.getString(contentKey);
		
		if(content == null)
		{
			return Localizator.NO_CONTENT_FOUND;
		}
		
		return content;
	}	
	
	public static void checkForSessionLanguageUpdate(HttpServletRequest req)
	{
		
		HttpSession session = req.getSession();
		
		String languageParameterValue = req.getParameter("language");
		
		if(languageParameterValue != null)
		{
			switch(languageParameterValue)
			{
				case "ru":
					req.getSession().setAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue(), new Locale("ru"));
					break;
					
				case "en":
					req.getSession().setAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue(), Locale.ENGLISH);
					break;			
					
			}
		}
		else if(session.getAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue()) == null)
		{
			Locale clientLocale = req.getLocale();
						
			if(clientLocale == null)
			{			
				clientLocale = Locale.ENGLISH;
			}
			
			session.setAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue(), clientLocale);
		}
	}
	
	private static Locale getLocaleFromSession(HttpServletRequest req)
	{
		HttpSession session = req.getSession(false);
		
		Locale clientLocale = null; 
		
		if(session != null)
		{				
			try
			{
				clientLocale = (Locale) session.getAttribute(GeneralAttributes.SESSION_CLIENT_LOCALE.getValue());
			}
			catch(Exception e)
			{
			}			
		}
		
		if(clientLocale == null)
		{			
			clientLocale = req.getLocale();
		}
		
		if(clientLocale == null)
		{			
			clientLocale = Locale.ENGLISH;
		}
		
		return clientLocale;
	}
	
	private static ClassLoader getClassLoader(HttpServletRequest req)
	{
		if(Localizator.resourceLoader == null)
		{
			String localizationFilesDirectoryPath = req.getServletContext().getRealPath("/WEB-INF/res/localization");
			
			if(localizationFilesDirectoryPath != null)
			{			
				File localizationFilesDirectory = new File(localizationFilesDirectoryPath);
				
				try 
				{			
					Localizator.resourceLoader = new URLClassLoader(new URL[] {
							localizationFilesDirectory.toURI().toURL()
					});
				}
				catch(MalformedURLException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		return Localizator.resourceLoader;
	}
}
