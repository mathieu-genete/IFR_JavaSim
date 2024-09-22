import java.awt.Color;


/**
 * Classe abstraite qui definie les parametre communs des objets utilises
 * dans des instruments a cadrans
 * @author  GENETE MATHIEU 2008
 */
abstract class ObjetsCadrans extends ObjetsCoordonneesCirculaires {
	
	//Declaration des variables
	
	/**
	 * variable statique definissant le diviseur dans les operation modulo
	 * dans les calculs trigonometriques, le valeur d'un angle va de 0 à 360 degres
	 */
	protected final int valModulo = 360;
	
	/**
	 * Rayon de l'objet
	 */
	protected int rayon;

	/**
	 * Valeur minimum que prendra l'objet
	 */
	protected float valMin;
	
	/**
	 * Valeur maximum que prendra l'objet
	 */
	protected float valMax;
	
	/**
	 * valueAngle indique l'angle que doit parcourir l'aiguille
	 * pour aller de valMin a valMax
	 */
	protected float valueAngle;
	
	/**
	 * AngleDepart permet de selectionner l'angle de depart quand value est a la valeur valMin
	 */
	protected float angleDepart;
	
	/**
	 * Couleur de l'objet
	 */
	protected Color couleur;
	
	/**
	 * Cette methode retourne un d'angle en fonction des valeurs valmin, valmax et anglevalue
	 * @param value
	 * @return angle en degre
	 */
	protected float CalculAngle(float value)
	{
		float coefDir,tmp;

		coefDir = (valueAngle)/(valMax - valMin);
		
		tmp = ((value * coefDir + angleDepart)) % valModulo;

		return tmp;
	}
	
	
	/**
	 * definition de la valeur de l'angle de depart quand value = valMin
	 * @param a
	 */
	public void setAngleDepart(float a)
	{
		a = (a - 90) % valModulo;
		this.angleDepart = a;
	}
	
	/**
	 * definit la couleur de l'objet
	 * @param c
	 */
	public void setCouleur(Color c)
	{
		this.couleur = c;
	}
	
	/**
	 * retroune la valeur de l'angle de depart
	 * @return angle en degre
	 */
	public float getAngleDepart()
	{
		return this.angleDepart;
	}
	
	/**
	 * Retourne la valeur du rayon de l'objet
	 * @return valeur entiere du rayon
	 */
	public int getRayon()
	{
		return this.rayon;
	}

}
