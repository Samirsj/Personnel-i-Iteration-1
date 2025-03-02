package commandLine;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import commandLineMenus.ListOption;
import commandLineMenus.Menu;
import commandLineMenus.Option;
import static commandLineMenus.rendering.examples.util.InOut.getString;
import personnel.Employe;
import personnel.SauvegardeImpossible;

public class EmployeConsole 
{
	private Option afficher(final Employe employe)
	{
		return new Option("Afficher l'employé", "l", () -> {System.out.println(employe);});
	}

	ListOption<Employe> editerEmploye()
	{
		return (employe) -> editerEmploye(employe);		
	}

	Option editerEmploye(Employe employe)
	{
			Menu menu = new Menu("Gérer le compte " + employe.getNom(), "c");
			menu.add(afficher(employe));
			menu.add(changerNom(employe));
			menu.add(changerPrenom(employe));
			menu.add(changerMail(employe));
			menu.add(changerPassword(employe));
			menu.add(modifierDateArrivee(employe));
			menu.add(modifierDateDepart(employe));
			menu.addBack("q");
			return menu;
	}
	
	private Option modifierDateArrivee(Employe employe) {
	    return new Option("Changer date d'arrivée", "a", 
	        () -> {
	            try {
	                employe.setDateArrivee(LocalDate.parse(getString("Nouvelle date : ")));
	            } catch (DateTimeParseException e) {
	                System.out.println("Merci de fournir la date dans le format suivant : AAAA-MM-JJ.");
	            } catch (SauvegardeImpossible e) {
	                System.err.println("Impossible de sauvegarder la date : " + e.getMessage());
	            }
	        });
	}
	

	private Option modifierDateDepart(Employe employe) {
	    return new Option("Changer date de départ", "d", 
	        () -> {
	            try {
	                employe.setDateDepart(LocalDate.parse(getString("Nouvelle date : ")));
	            } catch (DateTimeParseException e) {
	                System.out.println("Merci de fournir la date dans le format suivant : AAAA-MM-JJ.");
	            } catch (SauvegardeImpossible e) {
	                System.err.println("Impossible de sauvegarder la date : " + e.getMessage());
	            }
	        });
	}


	private Option changerNom(final Employe employe)
	{
		return new Option("Changer le nom", "n", 
				() -> {
					try {
						employe.setNom(getString("Nouveau nom : "));
					} catch (SauvegardeImpossible e) {
						System.err.println("Impossible de sauvegarder le nouveau nom : " + e.getMessage());
					}
				});
	}
	
	private Option changerPrenom(final Employe employe)
	{
		return new Option("Changer le prénom", "p", 
				() -> {
					try {
						employe.setPrenom(getString("Nouveau prénom : "));
					} catch (SauvegardeImpossible e) {
						System.err.println("Impossible de sauvegarder le nouveau prénom : " + e.getMessage());
					}
				});
	}
	
	private Option changerMail(final Employe employe)
	{
		return new Option("Changer le mail", "e", 
				() -> {
					try {
						employe.setMail(getString("Nouveau mail : "));
					} catch (SauvegardeImpossible e) {
						System.err.println("Impossible de sauvegarder le nouveau mail : " + e.getMessage());
					}
				});
	}
	
	private Option changerPassword(final Employe employe)
	{
		return new Option("Changer le password", "x", 
				() -> {
					try {
						employe.setPassword(getString("Nouveau password : "));
					} catch (SauvegardeImpossible e) {
						System.err.println("Impossible de sauvegarder le nouveau password : " + e.getMessage());
					}
				});
	}
	

}