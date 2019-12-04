package ua.nure.sidak.SummaryTask4.web.utility;

import ua.nure.sidak.SummaryTask4.constants.TourLodgingType;
import ua.nure.sidak.SummaryTask4.constants.TourType;
import ua.nure.sidak.SummaryTask4.constants.UserRole;

/**
 * Used for checking all forms field which needs it
 * @author eXce1z0r
 *
 */
public class FieldChecker 
{
	private static final int LOGIN_MIN_LENGTH = 0;
	private static final int LOGIN_MAX_LENGTH = 20;
	
	private static final int MAIL_MAX_LENGTH = 50;

	private static final String MAIL_PATTERN = "[A-Za-z\\.0-9]+@[A-Za-z0-9]+\\.[A-Za-z0-9]+";
	
	private static final int PASSWORD_MIN_LENGTH = 6;
	private static final int PASSWORD_MAX_LENGTH = 30;
	
	private static final int TITLE_MIN_LENGTH = 0;	
	private static final int TITLE_MAX_LENGTH = 20;
	
	private static final int INFO_MIN_LENGTH = 0;	
	private static final int INFO_MAX_LENGTH = 255;
	
	private static final String INPUT_TEXT_PATTERN = "[A-Za-zР-пр-џ0-9\\.\\@\\!\\?\\%\\s]*";
	
	public enum ERROR_EXPLANATION {
		EMPTY_FIELD(-1), FIELD_CONTAIN_WRONG_VALUE(-2), FIELD_DOES_NOT_MATCH_PATTERN(-3), FIELDS_DIFFERENT(-4), OK(0);
		
		private int value; 
		
		ERROR_EXPLANATION(int status)
		{
			this.value = status;
		}
		
		public int getValue()
		{
			return this.value;
		}
	};
	
	public static int loginFieldCheck(String loginField)
	{
		int checkStatus = 0;
		
		if(loginField == null || loginField.isEmpty())
		{
			checkStatus = -1;
		}
		else if(loginField.length() < FieldChecker.LOGIN_MIN_LENGTH || loginField.length() > FieldChecker.LOGIN_MAX_LENGTH)
		{
			checkStatus = -2;
		}
		else if(!FieldChecker.isInputTextCorrectForEncoding(loginField))
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	public static int mailFieldCheck(String mailField)
	{
		int checkStatus = 0;
		
		if(mailField == null || mailField.isEmpty())
		{
			checkStatus = -1;
		}
		else if(mailField.length() > FieldChecker.MAIL_MAX_LENGTH)
		{
			checkStatus = -2;
		}
		else if(!mailField.matches(FieldChecker.MAIL_PATTERN))
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	public static int passwordFieldCheck(String passwordField)
	{
		int checkStatus = 0;
		
		if(passwordField == null || passwordField.isEmpty())
		{
			checkStatus = -1;
		}
		else if(passwordField.length() < FieldChecker.PASSWORD_MIN_LENGTH || passwordField.length() > FieldChecker.PASSWORD_MAX_LENGTH)
		{
			checkStatus = -2;
		}
		else if(!FieldChecker.isInputTextCorrectForEncoding(passwordField))
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	public static int passwordEquality(String passwordField1, String passwordField2)
	{
		int checkStatus = 0;
		
		checkStatus = FieldChecker.passwordFieldCheck(passwordField1);
		
		if(checkStatus > -1)
		{
			checkStatus = FieldChecker.passwordFieldCheck(passwordField2);
		}
		
		if(checkStatus > -1)
		{
			if(passwordField1.equals(passwordField2))
			{
				return checkStatus;
			}
			checkStatus = -4;			
		}
		
		return checkStatus;
	}
	
	public static int roleFieldCheck(int roleId)
	{
		return UserRole.getRoleByRoleId(roleId).getRoleId();
	}
	
	public static int tourTitleFieldCheck(String titleField)
	{
		int checkStatus = 0;
		
		if(titleField == null || titleField.isEmpty())
		{
			checkStatus = -1;
		}
		else if(titleField.length() < FieldChecker.TITLE_MIN_LENGTH || titleField.length() > FieldChecker.TITLE_MAX_LENGTH)
		{
			checkStatus = -2;
		}
		else if(!FieldChecker.isInputTextCorrectForEncoding(titleField))
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	public static int tourInfoFieldCheck(String infoField)
	{
		int checkStatus = 0;
		
		if(infoField == null || infoField.isEmpty())
		{
			checkStatus = -1;
		}
		else if(infoField.length() < FieldChecker.INFO_MIN_LENGTH 
			|| infoField.length() > FieldChecker.INFO_MAX_LENGTH)
		{
			checkStatus = -2;
		}
		else if(!FieldChecker.isInputTextCorrectForEncoding(infoField))
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	public static int tourTypeFieldCheck(int tourTypeId)
	{
		return TourType.getTourTypeById(tourTypeId).getTourTypeId();
	}
	
	public static int tourLodgingTypeFieldCheck(int tourLodgingTypeId)
	{
		return TourLodgingType.getTourLodgingTypeById(tourLodgingTypeId).getTourLodgingTypeId();
	}
	
	public static int tourDiscountOptionsCheck(float discountValue)
	{
		int checkStatus = 0;
		
		if(discountValue < 0 || discountValue > 100)
		{
			checkStatus = -3;
		}
		
		return checkStatus;
	}
	
	private static boolean isInputTextCorrectForEncoding(String text)
	{
		if(text != null)
		{
			return text.matches(FieldChecker.INPUT_TEXT_PATTERN);
		}
		return false;
	}
}
