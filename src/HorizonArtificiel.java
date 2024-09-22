import java.awt.*;

/**
 * La classe HorizonArtificiel permet d'affiche graphiquement un horizon artificiel sur un formulaire ou un panel.
 * Le roulis et l'assiette sont definit respectivement par setAngleRoulis et setAngleAssiette
 * @author GENETE MATHIEU 2008
 *
 */
public class HorizonArtificiel extends InstrumentDeBord {

	private float angleRoulis;
	private float angleAssiette;
	
	private Color couleurSol;
	private Color couleurCiel;
	
	private DessinsHorizon dessinHorizon;

	/**
	 * Constructeur de la classe HorizonArtificiel
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public HorizonArtificiel(int tailleInstrument, Color couleurFond)
	{		
		super(tailleInstrument, couleurFond);
		
		couleurSol = new Color(176,103,0);
		couleurCiel = new Color(0,152,204);
		
		dessinHorizon = new DessinsHorizon(tailleInstrument,couleurSol);
	}
	
	//METHODES PUBLICS
	
	public void paint(Graphics gc) {

		//Les coordonnees des differents objets sont tous relatifs a la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.
		
		int facteurEchelleBordure = 10;
		//facteur indiquant x pixel = 1 degre d'assiette
		float facteurAffichageAssietteSol = 2;
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.clearRect(0,0,tailleInstrument,tailleInstrument);
		

		tampon.setColor(couleurCiel);
		tampon.fillRect(0,0,tailleInstrument,tailleInstrument);
		
		dessinHorizon.dessineSol(tampon,this.angleAssiette * facteurAffichageAssietteSol,this.angleRoulis,couleurCiel,facteurAffichageAssietteSol);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument,tailleInstrument/facteurEchelleBordure);
		tampon.fillRect(0,0,tailleInstrument/facteurEchelleBordure,tailleInstrument);
		tampon.fillRect(0,tailleInstrument-(tailleInstrument/facteurEchelleBordure),tailleInstrument,tailleInstrument+(tailleInstrument/facteurEchelleBordure));
		tampon.fillRect(tailleInstrument - tailleInstrument/facteurEchelleBordure,0,tailleInstrument + tailleInstrument/facteurEchelleBordure,tailleInstrument);
		
		tampon.setColor(Color.black);
		tampon.drawRect(tailleInstrument/facteurEchelleBordure, tailleInstrument/facteurEchelleBordure, tailleInstrument-(int)(tailleInstrument/(facteurEchelleBordure/2)) , tailleInstrument-(int)(tailleInstrument/(facteurEchelleBordure/2)));
		tampon.drawRect(-1, -1, tailleInstrument-1 , tailleInstrument-1);
		tampon.setColor(Color.LIGHT_GRAY);
		tampon.drawRect(0, 0, tailleInstrument , tailleInstrument);
		
		//Dessin de la maquette de l'avion
		int tailleCarreCentrale = (int)(tailleInstrument * 0.015);
		int tailleRectangles = (int)(tailleInstrument * 0.15);
		tampon.setColor(Color.white);
		tampon.fillRect(centreX-tailleCarreCentrale, centreY-tailleCarreCentrale, tailleCarreCentrale*2, tailleCarreCentrale*2);
		tampon.fillRect((tailleInstrument/4),centreY-tailleCarreCentrale, tailleRectangles, tailleCarreCentrale*2);
		tampon.fillRect(tailleInstrument - (tailleInstrument/4)-tailleRectangles, centreY-tailleCarreCentrale,tailleRectangles, tailleCarreCentrale*2);

		tampon.setColor(Color.black);
		tampon.drawRect(centreX-tailleCarreCentrale, centreY-tailleCarreCentrale, tailleCarreCentrale*2, tailleCarreCentrale*2);
		tampon.drawRect((tailleInstrument/4),centreY-tailleCarreCentrale, tailleRectangles, tailleCarreCentrale*2);
		tampon.drawRect(tailleInstrument - (tailleInstrument/4)-tailleRectangles, centreY-tailleCarreCentrale,tailleRectangles, tailleCarreCentrale*2);

		//dessine les vis sur l'instrument
		dessineVis(tampon,30);
		
		//Dessine l'horizon artificiel a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}

	
	/**
	 * Retourne la valeur du roulis affiche par l'instrument (en degres)
	 * @return
	 */
	public float getAngleRoulis() {
		return angleRoulis;
	}

	/**
	 * Definit l'angle a afficher sur l'horizon artificiel pour l'axe du roulis
	 * @param angleRoulis
	 */
	public void setAngleRoulis(float angleRoulis) {
		
		float tmp;
		
		tmp = angleRoulis % 360;
		
		//Redessine l'instrument seulement si la valeur a changee
		if (tmp != this.angleRoulis) 
		{
			this.angleRoulis = tmp;

			this.repaint();
		}

	}


	/**
	 * Retourne la valeur de l'assiette affichee par l'instrument (en degres)
	 * @return
	 */
	public float getAngleAssiette() {
		return angleAssiette;
	}

	/**
	 * Definit l'angle a afficher sur l'horizon artificiel pour l'axe de l'assiette
	 * @param angleAssiette
	 */
	public void setAngleAssiette(float angleAssiette) {
		
		if (angleAssiette > 90 ) angleAssiette = 90;
		if (angleAssiette < -90) angleAssiette = -90;
		
		//Redessine l'instrument seulement si la valeur a changee
		if (angleAssiette != this.angleAssiette) 
		{
			
			this.angleAssiette = -angleAssiette;
			
			this.repaint();
		}

	}


}
