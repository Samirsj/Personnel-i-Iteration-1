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
	public GestionPersonnel getGestionPersonnel() 
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try 
		{
			String requete = "select * from ligue";
			Statement instruction = connection.createStatement();
			ResultSet ligues = instruction.executeQuery(requete);
			while (ligues.next())
				gestionPersonnel.addLigue(ligues.getInt(1), ligues.getString(2));
		}
		catch (SQLException e)
		{
			System.out.println(e);
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
public int insert(Employe employe) throws SauvegardeImpossible 
{
   
    PreparedStatement instruction = null;
    ResultSet generatedKeys = null;
    
    try 
    {
       
        instruction = connection.prepareStatement(
            "INSERT INTO employe (nom, prenom, mail, password, fonction, date_arrivee, date_depart, id_ligue) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS
        );
        
       
        instruction.setString(1, employe.getNom());
        instruction.setString(2, employe.getPrenom());
        instruction.setString(3, employe.getMail());
        instruction.setString(4, employe.getPassword());
        
        
        String fonction = (employe.estAdmin(employe.getLigue())) ? "Admin" : "Employé";
        instruction.setString(5, fonction);
        
       
        instruction.setDate(6, java.sql.Date.valueOf(employe.getDateArrivee()));
        instruction.setDate(7, java.sql.Date.valueOf(employe.getDateDepart()));
        
       
        int ligueId = (employe.getLigue() != null) ? employe.getLigue().getId() : 0;
        instruction.setInt(8, ligueId);
        
       
        instruction.executeUpdate();
        
       
        generatedKeys = instruction.getGeneratedKeys();
        
        
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1); 
        } else {
            throw new SauvegardeImpossible("Aucun ID généré pour l'employé.");
        }
    } 
    catch (SQLException exception) 
    {
        
        exception.printStackTrace();
        throw new SauvegardeImpossible(exception);
    } 
    finally 
    {
      
        try {
            if (generatedKeys != null) generatedKeys.close();
            if (instruction != null) instruction.close();
        } catch (SQLException e) {
            e.printStackTrace(); 
        }
    }
}
