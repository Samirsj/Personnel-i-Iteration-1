package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import personnel.*;


class testLigue {
    GestionPersonnel gestionPersonnel = GestionPersonnel.getGestionPersonnel();

    @Test
    void createLigue() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertEquals("Fléchettes", ligue.getNom());
    }

    @Test
    void addEmploye() throws SauvegardeImpossible, ExceptionD {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
            LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        assertEquals(employe, ligue.getEmployes().first());
    }

    @Test
    void changeAdmin() throws SauvegardeImpossible, ExceptionD {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
            LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        ligue.setAdministrateur(employe);
        assertEquals(employe, ligue.getAdministrateur());

        employe.remove();
        assertFalse(ligue.getEmployes().contains(employe));
        assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
    }

    @Test
    void deleteLigue() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        ligue.remove();
        assertFalse(gestionPersonnel.getLigues().contains(ligue));
    }

    @Test
    void testValidDates() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        assertDoesNotThrow(() -> ligue.addEmploye("Wassim", "El Arche", "wsmsevran", "mdp", 
            LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31)));
    }

    @Test
    void testInvalidDates() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Exception exception = assertThrows(ExceptionD.class, () -> 
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
            LocalDate.of(2023, 12, 31), LocalDate.of(2023, 1, 1))
        );
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }

    @Test
    void testNullDates() throws SauvegardeImpossible {
        Ligue ligue = gestionPersonnel.addLigue("Football");

        // Test avec date de départ nulle
        Exception exception = assertThrows(ExceptionD.class, () -> 
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", null, 
            LocalDate.of(2023, 1, 1))
        );
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());

        // Test avec date d'arrivée nulle
        exception = assertThrows(ExceptionD.class, () -> 
            ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
            LocalDate.of(2023, 1, 1), null)
        );
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }

    @Test
    void testSetDateInvalid() throws SauvegardeImpossible, ExceptionD {
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes");
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty", 
            LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));

        // Test date départ invalide
        Exception exception = assertThrows(ExceptionD.class, () -> 
            employe.setDateDepart(LocalDate.of(2022, 1, 1))
        );
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());

        // Test date arrivée invalide
        exception = assertThrows(ExceptionD.class, () -> 
            employe.setDateArrivee(LocalDate.of(2024, 12, 2))
        );
        assertEquals("La date de départ ne peut pas être avant la date d'arrivée.", exception.getMessage());
    }
}

