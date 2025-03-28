package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import personnel.*;

public class TestGestionPersonnel {

    // 🔥 Supprime la sauvegarde si elle existe (utile pour la sérialisation)
    static {
        File fichier = new File("GestionPersonnel.srz");
        if (fichier.exists()) fichier.delete();
    }

    private final GestionPersonnel gestionPersonnel;

    public TestGestionPersonnel() throws ExceptionD, SauvegardeImpossible {
        this.gestionPersonnel = GestionPersonnel.getGestionPersonnel();
    }

    @Test
    void testAddLigue() throws Exception {
        // nom unique pour éviter conflits en base ou sérialisation
        String nomLigue = "Fléchettes_" + System.nanoTime();

        Ligue ligue = gestionPersonnel.addLigue(nomLigue);

        assertEquals(nomLigue, ligue.getNom(), "Le nom de la ligue est incorrect.");
        assertTrue(gestionPersonnel.getLigues().contains(ligue), "La ligue n'a pas été retrouvée.");
    }

    @Test
    void testAddEmploye() throws Exception {
        // Créer une ligue temporaire
        Ligue ligue = gestionPersonnel.addLigue("Fléchettes_" + System.nanoTime());

        // Ajouter un employé avec des dates valides
        Employe employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty",
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 30));

        // Vérifications
        assertTrue(ligue.getEmployes().contains(employe), "L'employé n'est pas dans la ligue.");
        assertEquals("Bouchard", employe.getNom());
        assertEquals("Gérard", employe.getPrenom());
        assertEquals("g.bouchard@gmail.com", employe.getMail());
        assertTrue(employe.checkPassword("azerty"), "Le mot de passe est incorrect.");
    }
}
