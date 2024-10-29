package testsUnitaires;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.Employe;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.SauvegardeImpossible;

class testLigue 
{
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();
	
	@Test
	void createLigue() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}

	@Test
	void addEmploye() throws SauvegardeImpossible
	{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe=null;
		LocalDate datedepart;
		LocalDate datearrivee;
		try {
			datedepart = LocalDate.of(2023, 4, 1);
			datearrivee = LocalDate.of(2024, 6, 1);
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", datedepart, datearrivee ); 
			
			
		} catch (DateTimeException e) {
			// TODO Auto-generated catch block
			System.out.println("Date  incorrecte");
		}
		
		assertEquals(employe, ligue.getEmployes().first());
	}
	

	
}
