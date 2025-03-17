package commandLine;

import static commandLineMenus.rendering.examples.util.InOut.getString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
		menu.add(changerNom(ligue));
		menu.add(supprimer(ligue));
		menu.addBack("q");
		return menu;
	}

	private Option changerNom(final Ligue ligue)
	{
		return new Option("Renommer", "r", 
				() -> {
					try {
						ligue.setNom(getString("Nouveau nom : "));
					} catch (SauvegardeImpossible e) {
						System.err.println("Impossible de sauvegarder le nouveau nom : " + e.getMessage());
					}
				});
	}

	private List<Ligue> selectionnerLigue()
	{
		return new List<Ligue>("Sélectionner une ligue", "e", 
				() -> new ArrayList<>(gestionPersonnel.getLigues()),
				(element) -> editerLigue(element)
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
						getString("password : "), LocalDate.parse(getString("Saisir date arriver : ")) , LocalDate.parse(getString("Saisir date depart : ")));
					}catch(ExceptionD e) {
						System.out.println("La date d'arrivée est avant la date de départ");
						
					}
					catch(DateTimeParseException e) {
						System.out.println("Format de date invalide. Veuillez saisir un bon format de date ex: AAAA-MM-JJ");
					} catch (SauvegardeImpossible e) {
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
	
	private Menu editerEmployer(Employe employe) {
		Menu menu = new Menu("Gerer : " + employe.getNom());
		menu.add(modifierEmploye(employe));
		menu.add(supprimerEmploye(employe));
		menu.add(changerAdmin(employe));
		
		menu.addBack("q");
		return menu;
	}
	
	private Option changerAdmin(final Employe employe) {
		return new Option("Nommer l'administrateur", "a", () -> {
			try {
				employe.getLigue().setAdministrateur(employe);
			} catch (SauvegardeImpossible e) {
				System.err.println("Impossible de sauvegarder le changement d'administrateur : " + e.getMessage());
			}
		});
	}

	private Option supprimerEmploye(final Employe employe)
	{
		return new Option("Supprimer Employe", "s", 
			() -> {
				try {
					employe.remove();
				} catch (SauvegardeImpossible e) {
					System.err.println("Impossible de sauvegarder la suppression : " + e.getMessage());
				}
			});
	}
	

	private Option modifierEmploye(final Employe employe)
	{
		return employeConsole.editerEmploye(employe);
	}
	
	private Option supprimer(Ligue ligue)
	{
		return new Option("Supprimer", "d", () -> {
			try {
				ligue.remove();
			} catch (SauvegardeImpossible e) {
				System.err.println("Impossible de sauvegarder la suppression : " + e.getMessage());
			}
		});
	}
	
	private Option selectEmploye(Ligue ligue)
	{
		return new List<>("Selectionner un employe", "e", 
				() -> new ArrayList<>(ligue.getEmployes()),
				(nb) -> editerEmployer(nb));}
	}
	
