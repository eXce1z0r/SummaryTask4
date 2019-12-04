package ua.nure.sidak.SummaryTask4.web.command.individual.admin;

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
import ua.nure.sidak.SummaryTask4.web.utility.MultiParser;

/**
 * Used by admin user to create new user
 * @author eXce1z0r
 *
 */
public class CreateNewUserActionCommand extends Command
{

	@Override
	public String execute(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		String loginField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_LOGIN_EL_TAG.getValue()));
		int loginFieldCheckResult = FieldChecker.loginFieldCheck(loginField);
		
		String mailField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_MAIL_EL_TAG.getValue()));
		int mailFieldCheckResult = FieldChecker.loginFieldCheck(mailField);
			
		String passwordField = Encoder.encodeString(req.getParameter(GeneralAttributes.USER_PASSWORD_EL_TAG.getValue()));
		int passwordFieldCheckResult = FieldChecker.loginFieldCheck(passwordField);
		
		int roleField = MultiParser.stringToIntValue(req.getParameter("accountRoleField"));
		int roleFieldCheckResult = FieldChecker.roleFieldCheck(roleField);
		
		boolean accountStatusField = MultiParser.stringToBoolean(req.getParameter("accountStatus"));
		
		if(loginFieldCheckResult > -1 && mailFieldCheckResult > -1 && passwordFieldCheckResult > -1
		&& roleFieldCheckResult > -1)
		{			
			User user = new User();
			
			user.setLogin(loginField);
			user.setMail(mailField);
			user.setPassword(passwordField);
			user.setRoleId(roleField);
			user.setStatus(accountStatusField);
			
			DAOFactory.getUserDao().create(user);
		}
		
		ToUserManagementPageCommand.setAttributesForPage(req);
		
		return PagePath.PAGE_ADMIN;
	}

}
