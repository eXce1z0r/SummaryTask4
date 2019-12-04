package ua.nure.sidak.SummaryTask4.constants;

/**
 * Contains representation of values from table 'tour_lodging_types' 
 * @author eXce1z0r
 *
 */
public enum TourLodgingType 
{
	ERROR(-1), HOTEL(1), HOSTEL(2), OPEN_AIR(3);
	
	private int value;
	
	TourLodgingType(int id)
	{
		this.value = id;
	}
	
	public int getTourLodgingTypeId()
	{
		return this.value;
	}
	
	public static TourLodgingType getTourLodgingTypeById(int tourLodgingTypeId)
	{
		TourLodgingType tourLodgingType = null;
		switch(tourLodgingTypeId)
		{
			case 1:
				tourLodgingType = TourLodgingType.HOTEL;
				break;
			case 2:
				tourLodgingType = TourLodgingType.HOSTEL;
				break;
			case 3:
				tourLodgingType = TourLodgingType.OPEN_AIR;
				break;
			default:
				tourLodgingType = TourLodgingType.ERROR;
		}
		
		return tourLodgingType;
	}
}
