package jdbc;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        // 1. Charger les ligues 
        String requeteLigue = "SELECT * FROM ligue";
        Statement instructionLigue = connection.createStatement();
        ResultSet ligues = instructionLigue.executeQuery(requeteLigue);
        while (ligues.next()) {
            gestionPersonnel.addLigue(ligues.getInt("id"), ligues.getString("nom"));
        }

        // 2. Charger le root 
        String requeteRoot = "SELECT * FROM employe WHERE nom = 'root' LIMIT 1";
        Statement instructionRoot = connection.createStatement();
        ResultSet rootResult = instructionRoot.executeQuery(requeteRoot);

        if (rootResult.next()) {
            // Récupération des informations du root
            int id = rootResult.getInt("id");
            String nom = rootResult.getString("nom");
            String prenom = rootResult.getString("prenom");
            String mail = rootResult.getString("mail");
            String password = rootResult.getString("password");
            LocalDate dateArrivee = rootResult.getDate("dateArrive") != null ? rootResult.getDate("dateArrive").toLocalDate() : null;
            LocalDate dateDepart = rootResult.getDate("dateDepart") != null ? rootResult.getDate("dateDepart").toLocalDate() : null;

            
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
			instruction = connection.prepareStatement("insert into ligue (nom) values(?)", Statement.RETURN_GENERATED_KEYS);
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
	
	
}