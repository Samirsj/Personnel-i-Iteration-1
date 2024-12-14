package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.time.format.DateTimeParseException;

import commandLineMenus.List;
import commandLineMenus.Menu;
import commandLineMenus.Option;

import personnel.*;

public class LigueConsole 
{
	private GestionPersonnel gestionPersonnel;
	private EmployeConsole employeConsole;

	public LigueConsole(GestionPersonnel gestionPersonnel, EmployeConsole employeConsole)
	{
		this.gestionPersonnel = gestionPersonnel;
		this.employeConsole = employeConsole;
	}

	Menu menuLigues()
	{
		Menu menu = new Menu("Gérer les ligues", "l");
		menu.add(afficherLigues());
		menu.add(ajouterLigue());
		menu.add(selectionnerLigue());
		menu.addBack("q");
		return menu;
	}

	private Option afficherLigues()
	{
		return new Option("Afficher les ligues", "l", () -> {System.out.println(gestionPersonnel.getLigues());});
	}

	private Option afficher(final Ligue ligue)
	{
		return new Option("Afficher la ligue", "l", 
				() -> 
				{
					System.out.println(ligue);
					System.out.println("administrée par " + ligue.getAdministrateur());
				}
		);
	}
	private Option afficherEmployes(final Ligue ligue)
	{
		return new Option("Afficher les employes", "l", () -> {System.out.println(ligue.getEmployes());});
	}

	private Option ajouterLigue()
	{
		return new Option("Ajouter une ligue", "a", () -> 
		{
			try
			{
				gestionPersonnel.addLigue(getString("nom : "));
			}
			catch(SauvegardeImpossible exception)
			{
				System.err.println("Impossible de sauvegarder cette ligue");
			}
		});
	}
	
	private Menu editerLigue(Ligue ligue)
	{
		Menu menu = new Menu("Editer " + ligue.getNom());
		menu.add(afficher(ligue));
		menu.add(gererEmployes(ligue));
		menu.add(changerAdministrateur(ligue));
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {ligue.setNom(getString("Nouveau nom : "));});
	}
	
	private Option selectionnerEmployé(Ligue ligue)
	{
	    return new List<>("Selectionner un employé", "s", 
	            () -> new ArrayList<>(ligue.getEmployes()),
	            employe -> editerEmployer(employe));
	}


	private Option ajouterEmploye(final Ligue ligue)
	{
		return new Option("ajouter un employé", "a",
				() -> 
				{
					try {
						ligue.addEmploye(getString("nom : "), 
							getString("prenom : "), getString("mail : "), 
							getString("password : ") , LocalDate.parse(getString("date arrive : ")) , 
							LocalDate.parse(getString("date depart : ")) 
							);
					} catch (ExceptionD e) {
						// TODO Auto-generated catch block
						System.out.println("Erreur : La date de départ ne peut pas être avant la date d'arrivée.");
					}
					catch (DateTimeParseException s) {
						System.out.println("Format invalide. Veuillez entrer une date au format yyyy-MM-dd.");			}
				}
		);
	}

	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectionnerEmployé(ligue));
		menu.addBack("q");
		return menu;
	}
	
	
	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
				);
	}
	
	
	private Menu editerEmployer(Employe employe) {
		Menu menu = new Menu("Gerer :" + employe.getNom());
		menu.add(modifierEmploye(employe));
		menu.add(supprimerEmploye(employe));
		menu.addBack("q");
		return menu;
	}
	
	private Option supprimerEmploye(final Employe employe)
	{
		return new Option("Supprimer Employé", "s" , () -> employe.remove());
	}
	

	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
	    return new List<>("Changer l'administrateur", "c", 
	        () -> new ArrayList<>(ligue.getEmployes()),
	        (index, element) -> {
	            ligue.setAdministrateur(element);
	            System.out.println("Le nouvel administrateur est : " + element);
	        }
	    );
	}
	
	private Option modifierEmploye(final Employe employe)
	{
		return employeConsole.editerEmploye(employe);
	}
	

	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}
