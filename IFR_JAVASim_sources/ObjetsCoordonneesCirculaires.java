
/**
 * Classe abstraite qui definit les parametres de base pour des objets utilisant
 * les coordonnees circulaires.
 * @author GENETE MATHIEU 2008
 *
 */
public class ObjetsCoordonneesCirculaires {

	//Coordonnees du centre de l'objet
	protected int centreX;
	protected int centreY;
	
	/**
	 * Permet de changer les coordonnees du centre de l'objet
	 * @param cx
	 * @param cy
	 */
	public void setCentre(int cx,int cy)
	{
		this.centreX = cx;
		this.centreY = cy;
	}
	

	/**
	 * Retourne la coordonnee X de l'objet
	 * @return centreX
	 */
	public int getCentreX()
	{
		return this.centreX;
	}

	/**
	 * Retourne la coordonnee Y de l'objet
	 * @return centreY
	 */
	public int getCentreY()
	{
		return this.centreY;
	}
	

	/**
	 * renvoie la coordonnee x d'un point centre sur centreX en fonction de l'angle et du rayon
	 * @param centreX
	 * @param angle
	 * @param rayon
	 * @return
	 */
	protected int coordPolX(int centreX,float angle,float rayon)
	{
		int tmp;
		
		angle = angle % 361;
		
		tmp = (int)(rayon * Math.cos(angle * Math.PI/180) + centreX);
		
		return tmp;
	}
	
	/**
	 * renvoie la coordonnee y d'un point centre sur centreY en fonction de l'angle et du rayon
	 * @param centreY
	 * @param angle
	 * @param rayon
	 * @return
	 */
	protected int coordPolY(int centreY,float angle,float rayon)
	{
		int tmp;
		
		angle = angle % 361;
		
		tmp = (int)(rayon * Math.sin(angle * Math.PI/180) + centreY);
		
		return tmp;
	}
	
}
