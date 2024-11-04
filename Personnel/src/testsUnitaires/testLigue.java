package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import personnel.*;

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
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30)); 
		assertEquals(employe, ligue.getEmployes().first());
	}
	
	@Test
	void setEmploye() throws SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty" , "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30)); 
		employe.setNom("test");
		assertEquals("test" , employe.getNom());
	}
	
	@Test
	void getNom() throws SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		assertEquals("Fléchettes", ligue.getNom());
	}
	
	@Test
	void setNom() throws SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		ligue.setNom("Bowling");
		assertEquals("Bowling", ligue.getNom());
	}
	
	
	@Test
	void getAdministrator() throws SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30)); 
		ligue.setAdministrateur(employe);
		assertEquals(employe, ligue.getAdministrateur());
	}
	@Test
	void deleteAndChangeAdmin() throws SauvegardeImpossible{
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		Employe employe;
			 
			employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", "test", LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
			
			ligue.setAdministrateur(employe);
			assertEquals(employe, ligue.getAdministrateur());
			
			employe.remove();
			assertFalse(ligue.getEmployes().contains(employe));
		
			assertFalse(ligue.getEmployes().contains(employe));
			assertEquals(gestionPersonnel.getRoot() , ligue.getAdministrateur());
			
	}
	
	@Test
	void deleteLigue() throws SauvegardeImpossible {
		Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
		ligue.remove();
		assertFalse(gestionPersonnel.getLigues().contains(ligue));
	}
	
}

