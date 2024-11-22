package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.util.ArrayList;

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
		//menu.add(changerAdministrateur(ligue));
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
	
	private List<Employe> selectionnerEmployé(final Ligue ligue)
	{
		return new List<>("Sélectionner un employé", "s", 
				() -> new ArrayList<>(ligue.getEmployes()),
				employeConsole.editerEmploye()
				);
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
						e.printStackTrace();
					}
				}
		);
	}
	
	private Menu gererEmployes(Ligue ligue)
	{
		Menu menu = new Menu("Gérer les employés de " + ligue.getNom(), "e");
		menu.add(afficherEmployes(ligue));
		menu.add(ajouterEmploye(ligue));
		menu.add(selectEmploye(ligue));
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
	
	
	private List<Employe> selectEmploye(Ligue ligue){
		return new List<Employe>("Selectionner un employer" , "s" , () -> new ArrayList(ligue.getEmployes()) , (nb) -> suppOuEditEmploye(ligue , nb));
	}
	
	private Menu suppOuEditEmploye(Ligue ligue , Employe employe) {
		Menu menu = new Menu("Editer Employe "+ employe.getNom() + " " + employe.getPrenom() + " de chez " + ligue.getNom());
		menu.add(modifierEmploye(ligue , employe));
		menu.add(supprimerEmploye(ligue, employe));
		menu.addBack("q");
		return menu;
		
	}
	
	
	private Option supprimerEmploye(final Ligue ligue, Employe employe)
	{
		return new Option("Supprimer l'employer" , "s" , () -> suppEmployeRetour(ligue , employe));
			
	}
	
	private void suppEmployeRetour(Ligue ligue , Employe employe) 
	
	{
		ligue.getEmployes().remove(employe);
	
	}
	
	private List<Employe> changerAdministrateur(final Ligue ligue)
	{
		return null;
	}
	
	

	private Option modifierEmploye(final Ligue ligue, Employe employe)
	{
		EmployeConsole test = new EmployeConsole();
		return test.editerEmploye(employe);
	}
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {ligue.remove();});
	}
	
}
