package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import personnel.*;

public class TestEmployé {

    private GestionPersonnel gestionPersonnel;
    private Ligue ligue;

    @BeforeEach
    void setUp() throws SauvegardeImpossible {
        // Initialisation de la gestion du personnel et d'une ligue
        gestionPersonnel = GestionPersonnel.getGestionPersonnel();
        ligue = gestionPersonnel.addLigue("Fléchettes");
    }

    @Test
    void testAddEmploye() throws SauvegardeImpossible, ExceptionD {
        // Ajout de l'employé avec les bonnes informations
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        
        // Test des attributs de l'employé
        assertEquals("Bouchard", employe.getNom(), "Le nom de l'employé ne correspond pas.");
        assertEquals("Gérard", employe.getPrenom(), "Le prénom de l'employé ne correspond pas.");
        assertEquals("g.bouchard@gmail.com", employe.getMail(), "L'email de l'employé ne correspond pas.");
        assertTrue(employe.checkPassword("azerty"), "Le mot de passe ne correspond pas.");
    }

    @Test
    void testNomEmploye() throws SauvegardeImpossible, ExceptionD {
        // Ajout de l'employé et test du nom
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("Bouchard", employe.getNom(), "Le nom de l'employé ne correspond pas.");
    }

    @Test
    void testPrenomEmploye() throws SauvegardeImpossible, ExceptionD {
        // Ajout de l'employé et test du prénom
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("Gérard", employe.getPrenom(), "Le prénom de l'employé ne correspond pas.");
    }

    @Test
    void testMailEmploye() throws SauvegardeImpossible, ExceptionD {
        // Ajout de l'employé et test du mail
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("g.bouchard@gmail.com", employe.getMail(), "L'email de l'employé ne correspond pas.");
    }

    @Test
    void testPasswordEmploye() throws SauvegardeImpossible, ExceptionD {
        // Ajout de l'employé et test du mot de passe
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
                                           LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        // Vérification du mot de passe avec la méthode checkPassword()
        assertTrue(employe.checkPassword("azerty"), "Le mot de passe ne correspond pas.");
    }
}


	
	
