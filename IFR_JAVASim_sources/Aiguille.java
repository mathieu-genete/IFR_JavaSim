import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**
 * Cette classe permet de creer des aguille pour l'affichage des instruments. L'aiguille posede un centre,
 * un rayon, une valeur minimum et maximum, ainsi qu'un interval angulaire d'action
 * @author  GENETE MATHIEU 2008
 */
public class Aiguille extends ObjetsCadrans{

	
	
	public enum typeAiguille {standard, longue, adf}

	//Declaration des variables
	private int epaisseur;
	
	private typeAiguille styleAiguille;
	
	private Polygon polygonAguille;

	//value est la valeur que l'aiguille represente
	private float value;
	
	/**
	 * Constructeur de la classe aiguille.
	 * @param rayon
	 * @param epaisseur
	 * @param couleur
	 * @param valMin
	 * @param valMax
	 * @param valueAngle
	 * @param angleDepart
	 */
	public Aiguille(int rayon, int epaisseur,Color couleur,float valMin, float valMax, float valueAngle, float angleDepart)
	
	{
		polygonAguille = new Polygon();
		
		//une aiguille ne peut avoir un rayon et une epaisseur nul ou negatif
		if (rayon<0 || rayon ==0) rayon = 1;
		if (epaisseur<0 || epaisseur == 0) epaisseur = 1;
		
		//affecte aux variables de la classe les valeurs du constructeur
		centreX = 0;
		centreY = 0;
		this.rayon = rayon;
		this.epaisseur = epaisseur;
		this.couleur = couleur;
		this.valMin = valMin;
		this.valMax = valMax;
		//une valeur d'angle ne peut etre >360
		//On utilise le modulo 361 pour avoir un domaine entre 0 et 300
		this.valueAngle = valueAngle % valModulo;
		this.angleDepart = (angleDepart - 90) % valModulo;
		
		//value est initialise avec valMin
		this.value = valMin;

		styleAiguille = typeAiguille.standard;

	}
	
	//
	//Methodes PUBLIC
	//
	
	/** 
	 * Permet d'affecter une valeur a la variable value
	 * @param v
	 */
	public void setValue(float value)
	{
		this.value = value;
	}
	
	/** 
	 * retourne la valeur de value
	 * @return retourne value
	 * @uml.property  name="value"
	 */
	public float getValue()
	{
		return value;
	}
	
	/**
	 * definit le style d'aiguille a afficher. Le type par defaut est standard
	 * Les types disponibles sont standard, longue et adf.
	 * @param styleAiguille
	 */
	public void setStyleAiguille(typeAiguille styleAiguille)
	{
		this.styleAiguille = styleAiguille;
	}
	
	/**
	 * retourne le type de l'aiguille
	 * @return
	 */
	public typeAiguille getStyleAiguille()
	{
		return this.styleAiguille;
	}
	
	/**
	 * dessine l'aiguille dans le context graphique specifie
	 * @param gc
	 */
	public void DessineAiguille(Graphics gc)
	{
		float angle;
		//Sauvegarde la couleur utiliser par le context graphique
		Color ancienneCouleur;		
		ancienneCouleur = gc.getColor();

		
		angle = CalculAngle(value);
		
		//Dessin de l'aiguille	en fonction du type d'aiguille choisi
		if (styleAiguille == typeAiguille.standard)
		{
			polygonAguille.reset();
			polygonAguille.addPoint(coordPolX(centreX,angle,rayon),coordPolY(centreY,angle,rayon));
			polygonAguille.addPoint(coordPolX(centreX,angle+90,epaisseur/2),coordPolY(centreY,angle+90,epaisseur/2));
			polygonAguille.addPoint(coordPolX(centreX,angle-90,epaisseur/2),coordPolY(centreY,angle-90,epaisseur/2));
		}
		
		if (styleAiguille == typeAiguille.longue)
		{
			polygonAguille.reset();
			polygonAguille.addPoint(coordPolX(centreX,angle-2,rayon/1.1f),coordPolY(centreY,angle-2,rayon/1.1f));
			polygonAguille.addPoint(coordPolX(centreX,angle,rayon),coordPolY(centreY,angle,rayon));
			polygonAguille.addPoint(coordPolX(centreX,angle+2,rayon/1.1f),coordPolY(centreY,angle+2,rayon/1.1f));
			polygonAguille.addPoint(coordPolX(centreX,angle+175,rayon/3),coordPolY(centreY,angle+175,rayon/3));
			polygonAguille.addPoint(coordPolX(centreX,angle+185,rayon/3),coordPolY(centreY,angle+185,rayon/3));
		}
		
		if (styleAiguille == typeAiguille.adf)
		{
			polygonAguille.reset();
			polygonAguille.addPoint(coordPolX(centreX,angle-3,rayon/1.5f),coordPolY(centreY,angle-3,rayon/1.5f));
			polygonAguille.addPoint(coordPolX(centreX,angle-6,rayon/1.5f),coordPolY(centreY,angle-6,rayon/1.5f));
			polygonAguille.addPoint(coordPolX(centreX,angle,rayon),coordPolY(centreY,angle,rayon));
			polygonAguille.addPoint(coordPolX(centreX,angle+6,rayon/1.5f),coordPolY(centreY,angle+6,rayon/1.5f));
			polygonAguille.addPoint(coordPolX(centreX,angle+3,rayon/1.5f),coordPolY(centreY,angle+3,rayon/1.5f));
			polygonAguille.addPoint(coordPolX(centreX,angle+178,rayon),coordPolY(centreY,angle+178,rayon));
			polygonAguille.addPoint(coordPolX(centreX,angle+182,rayon),coordPolY(centreY,angle+182,rayon));
		}
		
			//Dessin de l'interireur de l'aiguille
			gc.setColor(couleur);
			gc.fillPolygon(polygonAguille);
			
			//Dessin du contour de l'aiguille
			gc.setColor(Color.darkGray);
			gc.drawPolygon(polygonAguille);
			
			gc.setColor(couleur);
			gc.fillOval(centreX-(epaisseur/2),centreY-(epaisseur/2),epaisseur,epaisseur);
					
			gc.setColor(Color.black);
			gc.drawOval(centreX-(epaisseur/2),centreY-(epaisseur/2),epaisseur,epaisseur);
		
		
		
		//restitue la couleur du context graphique
		gc.setColor(ancienneCouleur);
	}

}
