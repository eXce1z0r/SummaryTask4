package ua.nure.sidak.SummaryTask4.db.constants;

/**
 * Class used in each DAO for sql request generation
 * @author eXce1z0r
 *
 */
public enum SQLTemplates 
{	
	//	main requests
	INSERT_REQUEST_TEMPLATE("INSERT INTO <table-name> (<table-columns>) VALUES (<insert-request-values>)"), 
	SELECT_REQUEST_TEMPLATE("SELECT <table-columns> FROM <table-name> <where-case> <additional-info>"), 
	UPDATE_REQUEST_TEMPLATE("UPDATE <table-name> SET <update-set-expression> <where-case>"), 
	DELETE_REQUEST_TEMPLATE("DELETE FROM <table-name> <where-case>"),
	
	// fields to fill
	TABLE_NAME("<table-name>"), TABLE_COLUMNS("<table-columns>"),
	SELECT_ADDITIONAL_INFO("<additional-info>"),
	INSERT_REQUEST_VALUES("<insert-request-values>"),
	UPDATE_SET_EXPRESSION("<update-set-expression>"),
	WHERE_CASE("<where-case>");
	
	private String value;
	
	SQLTemplates(String value)
	{
		this.value = value;
	}
	
	public String getValue()
	{
		return this.value;
	}
}
