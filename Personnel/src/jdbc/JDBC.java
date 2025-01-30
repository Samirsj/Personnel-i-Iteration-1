package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


import personnel.*;

public class JDBC implements Passerelle 
{
	Connection connection;

	public JDBC()
	{
		try
		{
			Class.forName(Credentials.getDriverClassName());
			connection = DriverManager.getConnection(Credentials.getUrl(), Credentials.getUser(), Credentials.getPassword());
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Pilote JDBC non installé.");
		}
		catch (SQLException e)
		{
			System.out.println(e);
		}
	}
	
	@Override
	public GestionPersonnel getGestionPersonnel() {
	    GestionPersonnel gestionPersonnel = new GestionPersonnel();

	    try {
	        // Charger les ligues avec leurs employés
	        String requete = "SELECT l.ID_Ligue, l.Nom_Ligue, e.ID_Employé, e.Nom_Employé, e.Prenom_Employé, " +
	                         "e.Mail_Employé, e.MDP_Employé, e.Date_Arrivee, e.Date_Depart " +
	                         "FROM ligue l " +
	                         "LEFT JOIN employe e ON l.ID_Ligue = e.ID_Ligue " +
	                         "ORDER BY l.ID_Ligue";

	        Statement instruction = connection.createStatement();
	        ResultSet resultSet = instruction.executeQuery(requete);

	        Ligue ligueActuelle = null;
	        int lastLigueId = -1; // Pour suivre la dernière ligue insérée

	        while (resultSet.next()) {
	            int idLigue = resultSet.getInt("ID_Ligue");
	            String nomLigue = resultSet.getString("Nom_Ligue");

	            // Si la ligue change alors nouvel objet Ligue
	            if (idLigue != lastLigueId) {
	                ligueActuelle = gestionPersonnel.addLigue(idLigue, nomLigue);
	                lastLigueId = idLigue;
	            }

	            // Vérifier si l'employé est présent
	            if (resultSet.getObject("ID_Employé") != null) {
	                int idEmploye = resultSet.getInt("ID_Employé");
	                String nomEmploye = resultSet.getString("Nom_Employé");
	                String prenomEmploye = resultSet.getString("Prenom_Employé");
	                String mailEmploye = resultSet.getString("Mail_Employé");
	                String passwordEmploye = resultSet.getString("MDP_Employé");
	                LocalDate dateArrivee = resultSet.getDate("Date_Arrivee") != null ? resultSet.getDate("Date_Arrivee").toLocalDate() : null;
	                LocalDate dateDepart = resultSet.getDate("Date_Depart") != null ? resultSet.getDate("Date_Depart").toLocalDate() : null;

	                Employe employe = new Employe(gestionPersonnel, idEmploye, ligueActuelle, nomEmploye, prenomEmploye, mailEmploye, passwordEmploye, dateArrivee, dateDepart);
	                ligueActuelle.getEmployes().add(employe);
	            }
	        }

	        // Charger le root 
	        String requeteRoot = "SELECT * FROM employe WHERE Nom_Employé = 'root' LIMIT 1";
	        Statement instructionRoot = connection.createStatement();
	        ResultSet rootResult = instructionRoot.executeQuery(requeteRoot);

	        if (rootResult.next()) {
	            int id = rootResult.getInt("ID_Employé");
	            String nom = rootResult.getString("Nom_Employé");
	            String prenom = rootResult.getString("Prenom_Employé");
	            String mail = rootResult.getString("Mail_Employé");
	            String password = rootResult.getString("MDP_Employé");
	            LocalDate dateArrivee = rootResult.getDate("Date_Arrivee") != null ? rootResult.getDate("Date_Arrivee").toLocalDate() : null;
	            LocalDate dateDepart = rootResult.getDate("Date_Depart") != null ? rootResult.getDate("Date_Depart").toLocalDate() : null;

	            Employe rootEmploye = new Employe(gestionPersonnel, id, null, nom, prenom, mail, password, dateArrivee, dateDepart);
	            gestionPersonnel.setRoot(rootEmploye);
	        }


	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Erreur lors du chargement des données depuis la base.");
	    }

	    return gestionPersonnel;
	}

	@Override
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel) throws SauvegardeImpossible 
	{
		close();
	}
	
	public void close() throws SauvegardeImpossible
	{
		try
		{
			if (connection != null)
				connection.close();
		}
		catch (SQLException e)
		{
			throw new SauvegardeImpossible(e);
		}
	}
	
	@Override
	public int insert(Ligue ligue) throws SauvegardeImpossible 
	{
		try 
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into ligue (Nom_Ligue) values(?)", Statement.RETURN_GENERATED_KEYS);
			instruction.setString(1, ligue.getNom());		
			instruction.executeUpdate();
			ResultSet id = instruction.getGeneratedKeys();
			id.next();
			return id.getInt(1);
		} 
		catch (SQLException exception) 
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}		
	}
	
	@Override
	public int insert(Employe employe) {
		String query = "INSERT INTO employe (nom, prenom, mail, password, dateArrive, dateDepart, ligue_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
	
		try (PreparedStatement instruction = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			// Remplissage des paramètres
			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, employe.getDateArrivee() != null ? java.sql.Date.valueOf(employe.getDateArrivee()) : null);
			instruction.setDate(6, employe.getDateDepart() != null ? java.sql.Date.valueOf(employe.getDateDepart()) : null);
			instruction.setObject(7, employe.getLigue() != null ? employe.getLigue().getId() : null);
	
			
			instruction.executeUpdate();
	
			// Récupération de l'ID généré
			try (ResultSet id = instruction.getGeneratedKeys()) {
				if (id.next()) {
					return id.getInt(1); // Retourne l'ID
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	
		// Retourne -1 si une erreur
		return -1;
	}

	@Override
	public void update(Ligue ligue) throws SauvegardeImpossible {

    	try {
       	 	PreparedStatement instruction;
        	instruction = connection.prepareStatement("UPDATE ligue SET nom = ? WHERE id = ?");
        	instruction.setString(1, ligue.getNom());
        	instruction.setInt(2, ligue.getId());
        	instruction.executeUpdate();

    	} catch (SQLException exception) {
        	exception.printStackTrace();
        	throw new SauvegardeImpossible(exception);
    	}
	}

	@Override
	public void update(Employe employe) throws SauvegardeImpossible {

    	try {
        	String requete = "UPDATE employe SET nom = ?, prenom = ?, mail = ?, password = ?, dateArrive = ?, dateDepart = ?, ligue_id = ? WHERE id = ?";
        	PreparedStatement instruction = connection.prepareStatement(requete);

        	instruction.setString(1, employe.getNom());
        	instruction.setString(2, employe.getPrenom());
        	instruction.setString(3, employe.getMail());
        	instruction.setString(4, employe.getPassword());
        	instruction.setDate(5, employe.getDateArrivee() != null ? java.sql.Date.valueOf(employe.getDateArrivee()) : null);
        	instruction.setDate(6, employe.getDateDepart() != null ? java.sql.Date.valueOf(employe.getDateDepart()) : null);
        	instruction.setObject(7, employe.getLigue() != null ? employe.getLigue().getId() : null);
        	instruction.setInt(8, employe.getId()); 

        	
        	instruction.executeUpdate();
    		} catch (SQLException e) {
        		e.printStackTrace();
        	throw new SauvegardeImpossible(e);
    	}
	}	
}
	
	
