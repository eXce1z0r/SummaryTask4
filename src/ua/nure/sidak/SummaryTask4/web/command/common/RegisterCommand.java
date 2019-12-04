package ua.nure.sidak.SummaryTask4.web.command.common;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.nure.sidak.SummaryTask4.constants.GeneralAttributes;
import ua.nure.sidak.SummaryTask4.constants.PagePath;
import ua.nure.sidak.SummaryTask4.db.dao.DAOFactory;
import ua.nure.sidak.SummaryTask4.db.entity.User;
import ua.nure.sidak.SummaryTask4.web.command.Command;
import ua.nure.sidak.SummaryTask4.web.utility.Encoder;
import ua.nure.sidak.SummaryTask4.web.utility.FieldChecker;
import ua.nure.sidak.SummaryTask4.web.utility.Localizator;

/**
 * Used for register new user at database
 * @author eXce1z0r
 *
 */
public class RegisterCommand extends Command
{
	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{		
		String loginField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_LOGIN_EL_TAG.getValue()));
		String mailField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_MAIL_EL_TAG.getValue()));
		String passwordField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_PASSWORD_EL_TAG.getValue()));
		String passwordConfirmField = Encoder.encodeString(req.getParameter("passwordFieldConfirm"));
		
		Localizator localizator = new Localizator(req);
		
		String errorMessage;
		if((errorMessage = this.registerDataCheck(loginField, mailField, passwordField, passwordConfirmField, localizator)) != null)
		{
			req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), errorMessage.toString());
			
			ToRegisterPageCommand.setAttributesForPage(req);
			
			return PagePath.PAGE_REGISTER;
		}
		
		User user = new User();
		user.setLogin(loginField);
		user.setMail(mailField);
		user.setPassword(passwordField);
		user.setRoleId(1);
		user.setStatus(true);
		
		int userCreationRes = DAOFactory.getUserDao().create(user);
		if(userCreationRes < 0)
		{
			switch(userCreationRes)
			{
				case -3:
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("registerErrorCode.accWithSuchEmailExist"));
					break;
				case -2:
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("registerErrorCode.accWithSuchLoginExist"));
					break;
				default:
					req.setAttribute(GeneralAttributes.ERROR_MESSAGE_EL_TAG.getValue(), localizator.getContent("registerErrorCode.serverOverload"));
			}
			
			ToRegisterPageCommand.setAttributesForPage(req);
			
			return PagePath.PAGE_REGISTER;
		}
		
		ToLoginPageCommand.setAttributesForPage(req);
		
		req.setAttribute(GeneralAttributes.SUCCESS_MASSEGE_EL_TAG.getValue(), localizator.getContent("registerSuccess.message"));
		return PagePath.PAGE_LOGIN;
	}
	
	private String registerDataCheck(String loginField, String mailField, String passwordField, String passwordConfirmField, Localizator localizator)
	{
		String errorString = localizator.getContent("registerErrorCode.emptyFieldsPrep");
		
		boolean hasError = false; 
		
		if(FieldChecker.loginFieldCheck(loginField) < 0)
		{
			hasError = true;
			errorString += localizator.getContent("registerErrorCode.loginFieldError");
		}
		int mailCheckResult = FieldChecker.mailFieldCheck(mailField);
		if(mailCheckResult == FieldChecker.ERROR_EXPLANATION.EMPTY_FIELD.getValue() 
		|| mailCheckResult == FieldChecker.ERROR_EXPLANATION.FIELD_CONTAIN_WRONG_VALUE.getValue())
		{
			hasError = true;
			errorString += localizator.getContent("registerErrorCode.emailFieldError");
		}
		if(FieldChecker.passwordFieldCheck(passwordField) < 0)
		{
			hasError = true;
			errorString += localizator.getContent("registerErrorCode.passwordFieldError");
		}
		if(!hasError)
		{
			errorString = null;
		}
		
		if(!hasError 
		&& mailCheckResult == FieldChecker.ERROR_EXPLANATION.FIELD_DOES_NOT_MATCH_PATTERN.getValue())
		{
			hasError = true;
			errorString = localizator.getContent("registerErrorCode.wrongEmailFormat");
		}
		
		if(!hasError && FieldChecker.passwordEquality(passwordField, passwordConfirmField) < 0)
		{
			hasError = true;
			errorString = localizator.getContent("registerErrorCode.differentPasswords");
		}
		
		return errorString;
	}
}
