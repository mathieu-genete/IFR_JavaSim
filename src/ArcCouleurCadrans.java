import java.awt.*;

/**
 * Cette classe permet de dessiner des arc colore sur les cadrans
 * @author GENETE mathieu 2008
 *
 */
public class ArcCouleurCadrans extends ObjetsCoordonneesCirculaires {
	
	private InstrumentDeBord instruBord;
	
	private float angleDepart;
	
	private float angleArrive;
	
	private float angleValeur;
	
	private float rayon;
	
	private float epaisseur;
	
	private Color couleurArc, couleurFondCadran;
	
	/**
	 * Constructeur de la classe ArcCouleurCadrans
	 * @param instruBord instrument concerne
	 * @param angleDepart angle de depart de l'arc
	 * @param angleArrive angle d'arrive de larc
	 * @param rayon rayon en pixels
	 * @param epaisseur epaisseur en pixels
	 * @param couleurArc couleur de l'arc
	 */
	public ArcCouleurCadrans(InstrumentDeBord instruBord, float angleDepart,float angleArrive,float rayon,float epaisseur,Color couleurArc,Color couleurFondCadran)
	{
		this.instruBord = instruBord;
		this.angleDepart = 450 - angleDepart;
		this.angleArrive = 450 - angleArrive;
		
		this.angleValeur = this.angleArrive - this.angleDepart;
		this.rayon = rayon;
		this.epaisseur = epaisseur;
		this.couleurArc = couleurArc;
		this.couleurFondCadran = couleurFondCadran;
	}
	
	/**
	 * Dessine l'arc dans le contexte graphique entre en parametre
	 * @param gc
	 */
	public void dessineArc(Graphics gc)
	{
		int centreX,centreY,tailleInstrument;
		float i;
		
		//sauvegarde la couleur du contexte graphique
		Color ancienneCouleur;
		
		ancienneCouleur = gc.getColor();
		
		tailleInstrument = instruBord.tailleInstrument;
		
		centreX = tailleInstrument /2;
		centreY = centreX;
		
		gc.setColor(couleurArc);
		i=rayon+ epaisseur;
		gc.fillArc(centreX - (int)i, centreY - (int)i, (int)(i*2), (int)(i*2), (int)angleDepart, (int)angleValeur);

		gc.setColor(couleurFondCadran);
		i=rayon ;
		gc.fillArc(centreX - (int)i, centreY - (int)i, (int)(i*2), (int)(i*2), (int)angleDepart, (int)angleValeur);

		
		//restitue la couleur du contexte graphique
		gc.setColor(ancienneCouleur);
	}


}
