package personnel;

class ExceptionDepart extends Exception
{

	public ExceptionDepart()
	{
		System.out.println("La date de départ ne peut pas être avant la date d'arrivée.");
	}
	
	public String toString()
	{
		return "La date de départ ne peut pas être avant ma date d'arrivée";
	}
}