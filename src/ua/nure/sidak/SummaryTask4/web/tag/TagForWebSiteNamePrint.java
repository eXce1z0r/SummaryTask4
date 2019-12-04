package ua.nure.sidak.SummaryTask4.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Super important tag for printing website title
 * @author eXce1z0r
 *
 */
public class TagForWebSiteNamePrint extends TagSupport
{
	private static final String SITE_NAME = "Flushed away!";	//	Let's fly away
	
	@Override
	public int doStartTag() throws JspException 
	{
		JspWriter writer = pageContext.getOut();
		
		try
		{
			writer.write(SITE_NAME);
			writer.flush();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}
	
	@Override
	public int doEndTag() throws JspException 
	{
		return EVAL_PAGE;
	}
}
