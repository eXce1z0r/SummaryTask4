package ua.nure.sidak.SummaryTask4.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * Should encode input russian symbols into UTF-8, but doesn't do that
 * @author eXce1z0r
 *
 */
@WebFilter("/*")
public class EncodingFilter implements Filter{

	@Override
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		// TODO Auto-generated method stub		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException 
	{
		
		String encoding = request.getCharacterEncoding();
		
		request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
		
		chain.doFilter(request, response);
	}
	
	@Override
	public void destroy() 
	{
		// TODO Auto-generated method stub		
	}

}
