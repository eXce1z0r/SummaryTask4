package ua.nure.sidak.SummaryTask4.constants;

/**
 * Contains representation of values from table 'tour_types' 
 * @author eXce1z0r
 *
 */
public enum TourType 
{
	ERROR(-1), RECREATION(1), EXCURSION(2), SHOPPING(3);
	
	private int value;
	
	TourType(int id)
	{
		this.value = id;
	}
	
	public int getTourTypeId()
	{
		return this.value;
	}
	
	public static TourType getTourTypeById(int tourTypeId)
	{
		TourType tourType = null;
		switch(tourTypeId)
		{
			case 1:
				tourType = TourType.RECREATION;
				break;
			case 2:
				tourType = TourType.EXCURSION;
				break;
			case 3:
				tourType = TourType.SHOPPING;
				break;
			default:
				tourType = TourType.ERROR;
		}
		
		return tourType;
	}
}
