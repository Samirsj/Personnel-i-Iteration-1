package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

import personnel.*;

public class testGestionPersonnel {
	
	GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    @Test
    void getLigue () throws SauvegardeImpossible
    {
    Ligue ligue = gestionPersonnel.addLigue ("Fléchettes") ;
    
    assertEquals ("root", ligue.getAdministrateur ());
    }

    @Test
    void addLigue () throws SauvegardeImpossible
    {
    Ligue ligue = gestionPersonnel. addLigue ("Flechettes") ;
    assertEquals ("Flechettes", ligue.getNom ()) ;
    }

    @Test
    void addEmploye () throws SauvegardeImpossible
    {
    Ligue ligue = gestionPersonnel.addLigue ("Fléchettes") ;
    Employe employe = ligue.addEmploye ("Bouchard", "Gérard", "g.bouchardegmail.com", "azerty") ;
    assertEguals (employe, ligue.getEmployes().first()) ;
    }

}