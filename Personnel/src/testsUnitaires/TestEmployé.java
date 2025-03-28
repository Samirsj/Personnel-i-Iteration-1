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
    void setUp() throws Exception {
        gestionPersonnel = GestionPersonnel.getGestionPersonnel();
        ligue = gestionPersonnel.addLigue("Fléchettes_" + System.nanoTime()); // nom unique pour éviter doublons
    }

    @Test
    void testAddEmploye() throws SauvegardeImpossible, ExceptionD {
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));

        assertAll("Vérification des attributs de l'employé",
                () -> assertEquals("Bouchard", employe.getNom()),
                () -> assertEquals("Gérard", employe.getPrenom()),
                () -> assertEquals("g.bouchard@gmail.com", employe.getMail()),
                () -> assertTrue(employe.checkPassword("azerty"))
        );
    }

    @Test
    void testNomEmploye() throws SauvegardeImpossible, ExceptionD {
        Employe employe = ligue.addEmploye("Dupont", "Jean", "jean.dupont@mail.com", "1234",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("Dupont", employe.getNom());
    }

    @Test
    void testPrenomEmploye() throws SauvegardeImpossible, ExceptionD {
        Employe employe = ligue.addEmploye("Martin", "Luc", "luc.martin@mail.com", "abcd",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("Luc", employe.getPrenom());
    }

    @Test
    void testMailEmploye() throws SauvegardeImpossible, ExceptionD {
        Employe employe = ligue.addEmploye("Lemoine", "Anna", "anna.lemoine@mail.com", "qwerty",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertEquals("anna.lemoine@mail.com", employe.getMail());
    }

    @Test
    void testPasswordEmploye() throws SauvegardeImpossible, ExceptionD {
        Employe employe = ligue.addEmploye("Durand", "Paul", "paul.durand@mail.com", "password123",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));
        assertTrue(employe.checkPassword("password123"));
    }
}
