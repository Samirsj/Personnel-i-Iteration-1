package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import personnel.Employe;
import personnel.ExceptionD;
import personnel.GestionPersonnel;
import personnel.Ligue;
import personnel.Passerelle;
import personnel.SauvegardeImpossible;

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
	public GestionPersonnel getGestionPersonnel() throws SauvegardeImpossible, ExceptionD
	{
		GestionPersonnel gestionPersonnel = new GestionPersonnel();
		try
		{
			PreparedStatement instructionRoot;
			instructionRoot = connection.prepareStatement("select ID_Employé, Prenom_Employé, Nom_Employé, ID_Ligue, MDP_Employé  from employe where ID_Ligue is null and ID_Employé  = 1");
			ResultSet root = instructionRoot.executeQuery();

			if (!root.next()) {
				gestionPersonnel.addRoot("roverveot", null, null, "rootpass",
					LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-13"));
			} else {
				gestionPersonnel.addRoot(root.getInt("ID_Employé"), root.getString("Nom_Employé"),
					null, null, root.getString("MDP_Employé"),
					LocalDate.parse("2020-12-12"), LocalDate.parse("2020-12-13"));
			}

			PreparedStatement instruction;
			instruction = connection.prepareStatement("select * from employe");
			ResultSet resultats = instruction.executeQuery();

			Ligue ligueCourante = null;
			int idLiguePrecedente = -1;

			while (resultats.next())
			{

				int idLigue = resultats.getInt("ID_Ligue");
				if (idLigue != idLiguePrecedente)
				{
					PreparedStatement instruction2;
					instruction2 = connection.prepareStatement("select * from ligue where ID_Ligue = (?)");
					instruction2.setInt(1, idLigue);
					ResultSet res = instruction2.executeQuery();


					if(res.next()){
						String Ligue = res.getString(2);
						ligueCourante = gestionPersonnel.addLigue(idLigue,Ligue);
						idLiguePrecedente = idLigue;
						Employe employe = ligueCourante.addEmploye(
								resultats.getInt("ID_Employé"),
								resultats.getString("Nom_Employé"),
								resultats.getString("Prenom_Employé"),
								resultats.getString("Mail_Employé"),
								resultats.getString("MDP_Employé"),
								LocalDate.parse(resultats.getString("Date_Arrivee")),
								resultats.getString("Date_Depart") != null ?
									LocalDate.parse(resultats.getString("Date_Depart")) : null,
								resultats.getBoolean("Fonction_Employé")

							);
						if (resultats.getBoolean("Fonction_Employé"))
						{
							ligueCourante.setAdministrateur(employe);
						}
					}
				}



			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new SauvegardeImpossible(e);
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
			instruction = connection.prepareStatement(
				"INSERT INTO Ligue (Nom_Ligue) VALUES(?)",
				Statement.RETURN_GENERATED_KEYS
			);
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
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement("insert into employe (Prenom_Employé, Nom_Employé, Mail_Employé, MDP_Employé, Date_Arrivee, Date_Depart, Fonction_Employé, ID_Ligue) " +
			"values(?, ?, ?, ?, ?, ?, ?, ?)",
			Statement.RETURN_GENERATED_KEYS);

			instruction.setString(1, employe.getPrenom());
			instruction.setString(2, employe.getNom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setString(5, employe.getDateArrivee().toString());

			if (employe.getDateDepart() != null)
				instruction.setString(6, employe.getDateDepart().toString());
			else
				instruction.setNull(6, java.sql.Types.VARCHAR);

			if (employe.getLigue() == null) {
				instruction.setNull(8, java.sql.Types.INTEGER);
				instruction.setBoolean(7,false);
			}
			else {
				instruction.setInt(8, employe.getLigue().getIdLigue());
				instruction.setBoolean(7, employe.estAdmin(employe.getLigue()));
			}
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
	public void update(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction;
			instruction = connection.prepareStatement(
				"UPDATE ligue SET Nom_Ligue = ? WHERE ID_Ligue = ?"
			);
			instruction.setString(1, ligue.getNom());
			instruction.setInt(2, ligue.getIdLigue());

			int lignesModifiees = instruction.executeUpdate();
			if (lignesModifiees == 0)
				throw new SauvegardeImpossible(new Exception("Aucune ligue n'a été modifiée"));
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

	@Override
	public void update(Employe employe) throws SauvegardeImpossible
	{
		
		try
		
		{
			PreparedStatement instruction = connection.prepareStatement(
				"UPDATE employe SET Nom_Employé = (?), Prenom_Employé = (?), Mail_Employé = (?), MDP_Employé = (?), Date_Arrivee = (?), Date_Depart = (?), Fonction_Employé  = (?) WHERE ID_Employé = (?)");

			instruction.setString(1, employe.getNom());
			instruction.setString(2, employe.getPrenom());
			instruction.setString(3, employe.getMail());
			instruction.setString(4, employe.getPassword());
			instruction.setDate(5, java.sql.Date.valueOf(employe.getDateArrivee()));

			if (employe.getDateDepart() != null)
				instruction.setDate(6, java.sql.Date.valueOf(employe.getDateDepart()));
			else
				instruction.setNull(6, java.sql.Types.DATE);
			
			instruction.setBoolean(7, false);
			if(employe.getLigue() != null && employe.estAdmin(employe.getLigue())){
				instruction.setBoolean(7, true);
			}
			instruction.setInt(8, employe.getId());

			instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			exception.printStackTrace();
			throw new SauvegardeImpossible(exception);
		}
	}

		@Override
	public void delete(Employe employe) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement instruction = connection.prepareStatement(
				"DELETE FROM employe WHERE id = ?"
			);
			instruction.setInt(1, employe.getId());
			instruction.executeUpdate();
		}
		catch (SQLException exception)
		{
			throw new SauvegardeImpossible(exception);
		}
	}

	@Override
	public void delete(Ligue ligue) throws SauvegardeImpossible
	{
		try
		{
			PreparedStatement deleteLigue = connection.prepareStatement(
				"DELETE FROM ligue WHERE ID_Ligue = ?"
			);
			deleteLigue.setInt(1, ligue.getIdLigue());
			deleteLigue.executeUpdate();
		}
		catch (SQLException exception)
		{
			throw new SauvegardeImpossible(exception);
		}
	}
}