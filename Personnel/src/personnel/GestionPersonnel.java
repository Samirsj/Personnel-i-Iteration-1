package personnel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Gestion du personnel. Un seul objet de cette classe existe.
 * Il n'est pas possible d'instancier directement cette classe, 
 * la méthode {@link #getGestionPersonnel getGestionPersonnel} 
 * le fait automatiquement et retourne toujours le même objet.
 * Dans le cas où {@link #sauvegarder()} a été appelé lors 
 * d'une exécution précédente, c'est l'objet sauvegardé qui est
 * retourné.
 */

public class GestionPersonnel implements Serializable
{
	private static final long serialVersionUID = -105283113987886425L;
	private static GestionPersonnel gestionPersonnel = null;
	private SortedSet<Ligue> ligues;
	private Employe root;// Supprime par défaut
	public final static int SERIALIZATION = 1, JDBC = 2, 

			TYPE_PASSERELLE = JDBC;  

	private static Passerelle passerelle = TYPE_PASSERELLE == JDBC ? new jdbc.JDBC() : new serialisation.Serialization();	
	
	/**
	 * Retourne l'unique instance de cette classe.
	 * Crée cet objet s'il n'existe déjà.
	 * @return l'unique objet de type {@link GestionPersonnel}.
	 */
	
	public static GestionPersonnel getGestionPersonnel()
	{
		if (gestionPersonnel == null)
		{
			gestionPersonnel = passerelle.getGestionPersonnel();
			if (gestionPersonnel == null)
				gestionPersonnel = new GestionPersonnel();
		}
		return gestionPersonnel;
	}

	public GestionPersonnel()
	{
		if (gestionPersonnel != null)
			throw new RuntimeException("Vous ne pouvez créer qu'une seuls instance de cet objet.");
		ligues = new TreeSet<>();
		gestionPersonnel = this;
	}
	
	public void sauvegarder() throws SauvegardeImpossible
	{
		passerelle.sauvegarderGestionPersonnel(this);
	}

	public void update(Employe employe) throws SauvegardeImpossible 
	{
        passerelle.update(employe);
    }


	 /**
     * Crée un super-utilisateur  à partir du nom et du mot de passe fournis
     */

	public Employe addRoot(String nom, String password) throws SauvegardeImpossible {
        // Vérifie si le root existe déjà 
        if (root != null) {
            throw new IllegalStateException("Un root existe déjà !");
        }

        try {
            // Création du root
            root = new Employe(this, null, nom, "", "", password);
            return root;
        } catch (Exception e) {
            throw new SauvegardeImpossible("Erreur lors de la création du root.", e);
        }
    }
	
	
	/**
	 * Retourne la ligue dont administrateur est l'administrateur,
	 * null s'il n'est pas un administrateur.
	 * @param administrateur l'administrateur de la ligue recherchée.
	 * @return la ligue dont administrateur est l'administrateur.
	 */
	
	public Ligue getLigue(Employe administrateur)
	{
		if (administrateur.estAdmin(administrateur.getLigue()))
			return administrateur.getLigue();
		else
			return null;
	}

	/**
	 * Retourne toutes les ligues enregistrées.
	 * @return toutes les ligues enregistrées.
	 */
	
	public SortedSet<Ligue> getLigues()
	{
		return Collections.unmodifiableSortedSet(ligues);
	}

	public Ligue addLigue(String nom) throws SauvegardeImpossible
	{
		Ligue ligue = new Ligue(this, nom); 
		ligues.add(ligue);
		return ligue;
	}
	
	public Ligue addLigue(int id, String nom)
	{
		Ligue ligue = new Ligue(this, id, nom);
		ligues.add(ligue);
		return ligue;
	}

	void remove(Ligue ligue)
	{
		ligues.remove(ligue);
	}
	
	int insert(Ligue ligue) throws SauvegardeImpossible
	{
		return passerelle.insert(ligue);
	}

	int insert(Employe employe) throws SauvegardeImpossible
	{
		return passerelle.insert(employe);
	}

	/**
	 * Retourne le root (super-utilisateur).
	 * @return le root.
	 */
	
	public void setRoot(Employe root) {
	    this.root = root;
	}

	public Employe getRoot() {
		// Récupère le root depuis la base de données
		if (root == null) {
			// Si root n'est pas encore chargé en mémoire, le charger depuis la base
			try {
				root = passerelle.getRoot();
			} catch (Exception e) {
				// Gérer l'exception en cas d'erreur
				e.printStackTrace();
			}
		}
		return root;
	}
	
	//Ajoutez la méthode removeRoot si nécessaire pour empêcher la suppression accidentelle du root
	public void removeRoot() {
		if (root != null) {
			throw new ImpossibleDeSupprimerRoot();

	public void update(Ligue ligue) throws SauvegardeImpossible
		{
	    if (passerelle != null)
	        passerelle.update(ligue);
		}
	}
}

	// Appelle méthode supression
	public void deleteEmploye(Employe employe) throws SauvegardeImpossible {
    	passerelle.delete(employe);
	}

	public void delete(Ligue ligue) throws SauvegardeImpossible
	{
		if (passerelle != null)
			passerelle.delete(ligue);
	}
}

