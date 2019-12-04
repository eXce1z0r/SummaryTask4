package ua.nure.sidak.SummaryTask4.constants;

/**
 * Contains representation of values from table 'user_roles' 
 * @author eXce1z0r
 *
 */
public enum UserRole
{
	ERROR(-1), CUSTOMER(1), MANAGER(2), ADMIN(3);
	
	private int roleId;
	
	UserRole(int roleId)
	{
		this.roleId = roleId;
	}
	
	public int getRoleId()
	{
		return this.roleId;
	}
	
	public static UserRole getRoleByRoleId(int roleId)
	{
		UserRole role = null;
		switch(roleId)
		{
			case 1:
				role = UserRole.CUSTOMER;
				break;
			case 2:
				role = UserRole.MANAGER;
				break;
			case 3:
				role = UserRole.ADMIN;
				break;
			default:
				role = UserRole.ERROR;
		}
		
		return role;
	}
}
