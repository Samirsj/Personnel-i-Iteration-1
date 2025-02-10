package personnel;

public interface Passerelle 
{
	public GestionPersonnel getGestionPersonnel();
	public void sauvegarderGestionPersonnel(GestionPersonnel gestionPersonnel)  throws SauvegardeImpossible;
	public int insert(Ligue ligue) throws SauvegardeImpossible;
	//ajout insert
	public int insert(Employe employe) throws SauvegardeImpossible;
	// Ajout update
	public void update(Ligue ligue) throws SauvegardeImpossible;
	public void update(Employe employe) throws SauvegardeImpossible;
	//Ajout delete
	public void delete(Employe employe) throws SauvegardeImpossible;

}
