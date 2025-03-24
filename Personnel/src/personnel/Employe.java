package personnel;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Employé d'une ligue hébergée par la M2L. Certains peuvent
 * être administrateurs des employés de leur ligue.
 * Un seul employé, rattaché à aucune ligue, est le root.
 * Il est impossible d'instancier directement un employé,
 * il faut passer la méthode {@link Ligue#addEmploye addEmploye}.
 */

public class Employe implements Serializable, Comparable<Employe>
{
	private static final long serialVersionUID = 4795721718037994734L;
	private String nom, prenom, password, mail;
	private Ligue ligue;
	private GestionPersonnel gestionPersonnel;
	private LocalDate dateArrivee;
	private LocalDate dateDepart;
	private int id;


	Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrivee, LocalDate dateDepart)
	throws ExceptionD, SauvegardeImpossible
	{
		this.gestionPersonnel = gestionPersonnel;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.ligue = ligue;
		this.password = password;
		if (dateDepart.isBefore(dateArrivee)) 
			throw new ExceptionD();
		this.dateArrivee = dateArrivee;
		this.dateDepart = dateDepart;
		this.id = gestionPersonnel.insert(this);
	}

	Employe(int id, GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom,
		String mail, String password, LocalDate dateArrivee, LocalDate dateDepart, boolean FonctionEmploye)
		throws SauvegardeImpossible, ExceptionD
	{
		this.id = id;
		this.gestionPersonnel = gestionPersonnel;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.ligue = ligue;
		this.password = password;
		if (dateDepart.isBefore(dateArrivee)) throw new ExceptionD();
		this.dateArrivee = dateArrivee;
		this.dateDepart = dateDepart;
		if (FonctionEmploye && ligue != null)
			this.ligue.setAdministrateur(this);
	}

	/**
	 * Retourne vrai ssi l'employé est administrateur de la ligue
	 * passée en paramètre.
	 * @return vrai ssi l'employé est administrateur de la ligue
	 * passée en paramètre.
	 * @param ligue la ligue pour laquelle on souhaite vérifier si this
	 * est l'admininstrateur.
	 */

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	/**
	 * Retourne le nom de l'employé.
	 * @return le nom de l'employé.
	 */

	public String getNom()
	{
		return nom;
	}

	/**
	 * Change le nom de l'employé.
	 * @param nom le nouveau nom.
	 */

	public void setNom(String nom) throws SauvegardeImpossible
	{
		this.nom = nom;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne le prénom de l'employé.
	 * @return le prénom de l'employé.
	 */

	public String getPrenom()
	{
		return prenom;
	}
	

	public boolean estAdmin(Ligue ligue)
	{
		return ligue.getAdministrateur() == this;
	}

	/**
	 * Retourne vrai ssi l'employé est le root.
	 * @return vrai ssi l'employé est le root.
	 */

	public boolean estRoot()
	{
		return gestionPersonnel.getRoot() == this;
	}


	/**
	 * Change le prénom de l'employé.
	 * @param prenom le nouveau prénom de l'employé.
	 */

	public void setPrenom(String prenom) throws SauvegardeImpossible
	{
		this.prenom = prenom;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne le mail de l'employé.
	 * @return le mail de l'employé.
	 */

	public String getMail()
	{
		return mail;
	}

	/**
	 * Change le mail de l'employé.
	 * @param mail le nouveau mail de l'employé.
	 */

	public void setMail(String mail) throws SauvegardeImpossible
	{
		this.mail = mail;
		gestionPersonnel.update(this);
	}

	/**
	 * Retourne vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @return vrai ssi le password passé en paramètre est bien celui
	 * de l'employé.
	 * @param password le password auquel comparer celui de l'employé.
	 */

	public boolean checkPassword(String password)
	{
		return this.password.equals(password);
	}

	/**
	 * Change le password de l'employé.
	 * @param password le nouveau password de l'employé.
	 */

	public void setPassword(String password) throws SauvegardeImpossible
	{
		this.password = password;
		gestionPersonnel.update(this);
	}

	public String getPassword()
	{
		return password;
	}

	/**
	 * Retourne la ligue à laquelle l'employé est affecté.
	 * @return la ligue à laquelle l'employé est affecté.
	 */

	public Ligue getLigue()
	{
		return ligue;
	}

	/**
	 * Supprime l'employé. Si celui-ci est un administrateur, le root
	 * récupère les droits d'administration sur sa ligue.
		 * @throws SauvegardeImpossible
		 */

	public void remove() throws SauvegardeImpossible {
	    Employe root = gestionPersonnel.getRoot();
	    if (this != root) {
	        if (estAdmin(getLigue()))
	            getLigue().setAdministrateur(root);
	        getLigue().remove(this);
	        gestionPersonnel.delete(this); // 🔥 très important !
	    } else {
	        throw new ImpossibleDeSupprimerRoot();
	    }
	}


	public LocalDate getDateArrivee() {
        return dateArrivee;
    }

	public LocalDate getDateDepart() {
        return dateDepart;
    }


	public void setDateArrivee(LocalDate dateArrivee) throws SauvegardeImpossible
			 {
				 this.dateArrivee = dateArrivee;
				 gestionPersonnel.update(this);
			 }

	 public void setDateDepart(LocalDate dateDepart) throws SauvegardeImpossible
			 {
				 this.dateDepart = dateDepart;
				 gestionPersonnel.update(this);
			 }


	@Override
	public int compareTo(Employe autre)
	{
		int cmp = getNom().compareTo(autre.getNom());
		if (cmp != 0)
			return cmp;
		return getPrenom().compareTo(autre.getPrenom());
	}

	@Override
	public String toString()
	{
		String res = nom + " " + prenom + " " + mail + " (";
		if (estRoot())
			res += "super-utilisateur";
		else
			res += ligue.toString();
		return res + ")";
	}

}