package personnel;

class ExceptionArrivee extends Exception
{
	public ExceptionArrivee()
	{
		System.out.println("La date d'arrivée ne peut pas être avant la date de départ");
	}
	
	public String tostring()
	{
		return "ExceptionArrivee: La date d'arrivée ne peut pas être avant la date de départ.";
	}
	
}