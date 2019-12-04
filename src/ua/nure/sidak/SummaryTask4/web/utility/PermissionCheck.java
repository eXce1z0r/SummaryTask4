package ua.nure.sidak.SummaryTask4.web.utility;

import ua.nure.sidak.SummaryTask4.constants.UserRole;

/**
 * First version of SecurityFilter
 * @author eXce1z0r
 *
 */
public class PermissionCheck 
{
	public static boolean isAllowed(Object userRoleAttribute, UserRole... allowedRoles)
	{
		UserRole userRole = MultiParser.objectToUserRole(userRoleAttribute);
		
		for(UserRole allowedUserRole : allowedRoles)
		{
			if(allowedUserRole == userRole)
			{
				return true;
			}
		}
		
		return false;
	}
}
