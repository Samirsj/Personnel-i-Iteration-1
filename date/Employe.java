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
public class Employe implements Serializable, Comparable<Employe> {
    private static final long serialVersionUID = 4795721718037994734L;
    private String nom, prenom, password, mail;
    private LocalDate dateArrivee, dateDepart;
    private Ligue ligue;
    private GestionPersonnel gestionPersonnel;

    Employe(GestionPersonnel gestionPersonnel, Ligue ligue, String nom, String prenom, String mail, String password) {
        this.gestionPersonnel = gestionPersonnel;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.mail = mail;
        this.ligue = ligue;
    }

    public boolean estAdmin(Ligue ligue) {
        return ligue.getAdministrateur() == this;
    }

    public boolean estRoot() {
        return gestionPersonnel.getRoot() == this;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Ligue getLigue() {
        return ligue;
    }

    public LocalDate getDateArrivee() {
        return dateArrivee;
    }

    public void setDateArrivee(LocalDate dateArrivee) throws ExceptionArrivee {
        if (dateDepart != null && dateArrivee.isAfter(dateDepart)) {
            throw new ExceptionArrivee();
        }
        this.dateArrivee = dateArrivee;
        
        try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace();
        }
    }

    public LocalDate getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(LocalDate dateDepart) throws ExceptionDepart {
        if (dateArrivee != null && dateDepart.isBefore(dateArrivee)) {
            throw new ExceptionDepart();
        }
        this.dateDepart = dateDepart;
        
        try {
            gestionPersonnel.update(this);
        } catch (SauvegardeImpossible e) {
            e.printStackTrace();
        }
    }

    public void remove() {
        Employe root = gestionPersonnel.getRoot();
        if (this != root) {
            if (estAdmin(getLigue())) {
                getLigue().setAdministrateur(root);
            }
            getLigue().remove(this);
        } else {
            throw new ImpossibleDeSupprimerRoot();
        }
    }

    @Override
    public int compareTo(Employe autre) {
        int cmp = getNom().compareTo(autre.getNom());
        if (cmp != 0) {
            return cmp;
        }
        return getPrenom().compareTo(autre.getPrenom());
    }

    @Override
    public String toString() {
        return "Employe [nom=" + nom + ", prenom=" + prenom + ", password=" + password + ", mail=" + mail
                + ", dateArrivee=" + dateArrivee + ", dateDepart=" + dateDepart + ", ligue=" + ligue
                + ", gestionPersonnel=" + gestionPersonnel + "]";
    }
}
