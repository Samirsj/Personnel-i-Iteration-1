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

     /**
     * Constructeur principal pour ajouter un employé avec toutes ses données
     */

public class Employe implements Serializable, Comparable<Employe>
{
    private static final long serialVersionUID = 4795721718037994734L;
    private String nom, prenom, password, mail;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;
    private LocalDate dateArrive;
    private LocalDate dateDepart;
    private int id; // Ajout de l'ID

    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password, LocalDate dateArrive, LocalDate dateDepart)
            throws ExceptionD
    {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.mail = mail;
        this.ligue = ligue;
        

        if (dateArrive == null || dateDepart == null || dateDepart.isBefore(dateArrive) ) {
            throw new ExceptionD();
        }

        this.dateArrive = dateArrive;
        this.dateDepart = dateDepart;
    

        // Insertion dans la base de données pour root
        if ("root".equals(nom)) {
            try {
                this.id = gestionPersonnel.insert(this);
            }
        }
    }

     /**
     * Surcharge du constructeur : création d'un employé sans dates.
     */

    public Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password)
            throws SauvegardeImpossible, ExceptionD {
        this(gestionPersonnel, ligue, nom, prenom, mail, password, null, null);
    }

     /**
     * Constructeur spécial pour ajouter le Root
     */

    public Employe(GestionPersonnel gestionPersonnel, String nom, String password) throws SauvegardeImpossible {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        this.password = password;
        this.prenom = "";
        this.mail = "";
        this.ligue = null; // Le root n'est pas lié à une ligue

        // Insérer base de données
        try {
            this.id = gestionPersonnel.insert(this);
        } catch (Exception e) {
            throw new SauvegardeImpossible("Erreur lors de l'insertion du Root en base", e);
        }
    }


    /**
     * Retourne vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @return vrai ssi l'employé est administrateur de la ligue
     * passée en paramètre.
     * @param ligue la ligue pour laquelle on souhaite vérifier si this
     * est l'admininstrateur.
     */
    
     // Getter 
     public int getId() 
     {
        return id;
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

    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * Retourne le prénom de l'employé.
     * @return le prénom de l'employé.
     */

    public String getPrenom()
    {
        return this.prenom;
    }

    /**
     * Change le prénom de l'employé.
     * @param prenom le nouveau prénom de l'employé.
     */

    public void setPrenom(String prenom)
    {
        this.prenom = prenom;
    }

    /**
     * Retourne le mail de l'employé.
     * @return le mail de l'employé.
     */

    public String getMail()
    {
        return this.mail;
    }

    /**
     * Change le mail de l'employé.
     * @param mail le nouveau mail de l'employé.
     */

    public void setMail(String mail)
    {
        this.mail = mail;
    }
    /**
     * Retourne la date d'arrivée de l'employé.
     * @return la date d'arrivée de l'employé.
     */

    public LocalDate getDateArrivee()
    {
        return this.dateArrive;
    }

    /**
     * Retourne la date d'arrivée de l'employé.
     * @return la date d'arrivée de l'employé.
     */

    public void setDateArrivee(LocalDate dateArrive)
            throws ExceptionD
    {
        if (dateArrive == null  || dateDepart.isBefore(dateArrive)) {
            throw new ExceptionD();
        }
        else {

            this.dateArrive = dateArrive;}
    }

    /**
     * Modifie la date de départ de l'employé.
     * @return la date de départ de l'employé.
     */

    public LocalDate getDateDepart()
    {
        return this.dateDepart;
    }

    /**
     * Modifie la date de départ de l'employé.
     * @return la date de départ de l'employé.
     */

    public void setDateDepart(LocalDate dateDepart)
            throws ExceptionD
    {
        if (dateDepart == null || dateDepart.isBefore(dateArrive)) {
            throw new ExceptionD();
        }
        else {
            this.dateDepart = dateDepart;}
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password)
    {
        this.password= password;
    }

    /**
     * Retourne la ligue à laquelle l'employé est affecté.
     * @return la ligue à laquelle l'employé est affecté.
     */

    public Ligue getLigue()
    {
        return this.ligue;
    }

    /**
     * Supprime l'employé. Si celui-ci est un administrateur, le root
     * récupère les droits d'administration sur sa ligue.
     */

    public void remove()
    {
        Employe root = gestionPersonnel.getRoot();
        if (this != root)
        {
            if (estAdmin(getLigue()))
                getLigue().setAdministrateur(root);
            getLigue().remove(this);
        }
        else
            throw new ImpossibleDeSupprimerRoot();
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