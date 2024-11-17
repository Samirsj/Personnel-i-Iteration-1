package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.*;

public class TestGestionPersonnel {
    
    private GestionPersonnel gestionPersonnel;
    
    @BeforeEach
    void setUp() {
        gestionPersonnel = GestionPersonnel.getGestionPersonnel();
    }
    
    @Test 
    void testAddLigue() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        
        assertEquals("Fléchettes", ligue.getNom(), "Le nom de la ligue ne correspond pas.");
        
        assertTrue(gestionPersonnel.getLigues().contains(ligue), "La ligue n'a pas été ajoutée à la liste.");
    }
    
    @Test 
    void testAddEmploye() throws SauvegardeImpossible, ExceptionD {

    	Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        
        assertEquals(employe, ligue.getEmployes().first(), "L'employé ajouté n'est pas le premier de la ligue.");
        
        assertTrue(ligue.getEmployes().contains(employe), "L'employé n'a pas été ajouté à la ligue.");
    }
}

