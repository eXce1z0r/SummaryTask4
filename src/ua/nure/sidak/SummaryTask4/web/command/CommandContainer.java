package ua.nure.sidak.SummaryTask4.web.command;

import java.util.HashMap;

import ua.nure.sidak.SummaryTask4.web.command.common.LoginCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.LogoutCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.NoCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.RegisterCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.ToLoginPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.ToRegisterPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.common.TourSearchCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.CreateNewTourActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.CreateNewUserActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.ToUserManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.UpdateOrDestroyTourActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.admin.UpdateUserStatusActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.customer.MakeAnOrderActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.customer.ToProfilePageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.ToOrdersManagementPageCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.UpdateOrderStatusActionCommand;
import ua.nure.sidak.SummaryTask4.web.command.individual.manager.UpdateTourDiscountOptionsActionCommand;

/**
 * Commands factory which returns command by reserved word
 * @author eXce1z0r
 *
 */
public class CommandContainer 
{
	private static HashMap<String, Command> commands = new HashMap<>();
	
	static 
	{
		CommandContainer.commands.put("noCommand", new NoCommand());
		CommandContainer.commands.put("registerCommand", new RegisterCommand());
		CommandContainer.commands.put("loginCommand", new LoginCommand());
		CommandContainer.commands.put("logoutCommand", new LogoutCommand());
		CommandContainer.commands.put("tourListCommand", new TourSearchCommand());
		
		CommandContainer.commands.put("toLoginPageCommand", new ToLoginPageCommand());
		CommandContainer.commands.put("toRegisterPageCommand", new ToRegisterPageCommand());
		CommandContainer.commands.put("toProfilePageCommand", new ToProfilePageCommand());
		CommandContainer.commands.put("toOrdersManagementPageCommand", new ToOrdersManagementPageCommand());
		CommandContainer.commands.put("toUserManagementPageCommand", new ToUserManagementPageCommand());
		
		CommandContainer.commands.put("updateOrderStatusActionCommand", new UpdateOrderStatusActionCommand());
		CommandContainer.commands.put("updateTourDiscountOptionsActionCommand", new UpdateTourDiscountOptionsActionCommand());
		
		CommandContainer.commands.put("makeAnOrderActionCommand", new MakeAnOrderActionCommand());
		
		CommandContainer.commands.put("updateUserStatusActionCommand", new UpdateUserStatusActionCommand());
		CommandContainer.commands.put("updateOrDestroyTourActionCommand", new UpdateOrDestroyTourActionCommand());
		CommandContainer.commands.put("createNewUserActionCommand", new CreateNewUserActionCommand());
		CommandContainer.commands.put("createNewTourActionCommand", new CreateNewTourActionCommand());
	}
	
	public static Command getCommandByName(String commandName)
	{
		if(commandName == null || !CommandContainer.commands.containsKey(commandName))
		{
			return CommandContainer.commands.get("noCommand");
		}
		return CommandContainer.commands.get(commandName);
	}
}
