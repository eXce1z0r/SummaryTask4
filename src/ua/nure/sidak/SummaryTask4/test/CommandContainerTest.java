package ua.nure.sidak.SummaryTask4.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ua.nure.sidak.SummaryTask4.web.command.CommandContainer;
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

public class CommandContainerTest 
{	
	@Test
	public void getCommandByNameMethodTestNoCommand()
	{	
		assertTrue(CommandContainer.getCommandByName("noCommand") instanceof NoCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestRegisterCommand()
	{
		assertTrue(CommandContainer.getCommandByName("registerCommand") instanceof RegisterCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestLoginCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("loginCommand") instanceof LoginCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestLogoutCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("logoutCommand") instanceof LogoutCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestTourListCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("tourListCommand") instanceof TourSearchCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestToLoginPageCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("toLoginPageCommand") instanceof ToLoginPageCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestToRegisterPageCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("toRegisterPageCommand") instanceof ToRegisterPageCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestToProfilePageCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("toProfilePageCommand") instanceof ToProfilePageCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestToOrdersManagementPageCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("toOrdersManagementPageCommand") instanceof ToOrdersManagementPageCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestToUserManagementPageCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("toUserManagementPageCommand") instanceof ToUserManagementPageCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestUpdateOrderStatusActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("updateOrderStatusActionCommand") instanceof UpdateOrderStatusActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestUpdateTourDiscountOptionsActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("updateTourDiscountOptionsActionCommand") instanceof UpdateTourDiscountOptionsActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestMakeAnOrderActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("makeAnOrderActionCommand") instanceof MakeAnOrderActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestUpdateUserStatusActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("updateUserStatusActionCommand") instanceof UpdateUserStatusActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestUpdateTourUpdateOrDestroyTourActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("updateOrDestroyTourActionCommand") instanceof UpdateOrDestroyTourActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestUpdateTourCreateNewUserActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("createNewUserActionCommand") instanceof CreateNewUserActionCommand);
	}
	
	@Test
	public void getCommandByNameMethodTestCreateNewTourActionCommand()
	{
		
		assertTrue(CommandContainer.getCommandByName("createNewTourActionCommand") instanceof CreateNewTourActionCommand);
	}
	
}
