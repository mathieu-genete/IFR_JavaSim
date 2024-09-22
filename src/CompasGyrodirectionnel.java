import java.awt.*;

/**
 * La classe CompasGyrodirectionnel permet d'affiche graphiquement un Compas Gyrodirectionnel sur un formulaire ou un panel.
 * Le cap affichee sur l'instrument est defini avec la methode setHeadingValue
 * @author GENETE MATHIEU 2008
 *
 */
public class CompasGyrodirectionnel extends InstrumentDeBord {

	
	//declaration des variables privees
	
	private GraduationsCadrans graduations;
	
	private Font valeursGraduationsFont;
	
	private Polygon polygonDessin;
	
	private int heading;

	
	/**
	 * Constructeur de la classe CompasGyrodirectionnel
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public CompasGyrodirectionnel(int tailleInstrument, Color couleurFond)
	{
		super(tailleInstrument, couleurFond);
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,0,35.9f,359,0,3,0.5f);
		
		graduations.setAfficherPointsCardinaux(true);
		graduations.AfficheMarqueursStatiques();
		
		//Polygone repesentant la maquette de l'avion sur le compas
		polygonDessin = new Polygon();
		
		polygonDessin.addPoint(centreX,centreY - (int)(tailleInstrument / 4));
		polygonDessin.addPoint(centreX -tailleInstrument/14,centreY + (int)(tailleInstrument / 4));
		polygonDessin.addPoint(centreX ,centreY + (int)(tailleInstrument / 5));
		polygonDessin.addPoint(centreX+tailleInstrument/14,centreY + (int)(tailleInstrument / 4));
	}
	
	
	 //Methodes publiques
	
	public void paint(Graphics gc)
	{
		int tmpRayon;
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.setFont(valeursGraduationsFont);
		tampon.clearRect(0,0,tailleInstrument,tailleInstrument);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument,tailleInstrument);
		
		//Dessine les graduations
		graduations.setCentre(centreX,centreY);
		graduations.dessineGraduations(tampon);
		
		tmpRayon = graduations.getRayon();
		
		//dessin de la maquette de l'avion sur le compas
		tampon.setColor(Color.white);
		tampon.drawPolygon(polygonDessin);
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine le compas a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Definit le cap a afficher sur le compas
	 * @param heading
	 */
	public void setHeadingValue(int heading)
	{
		
		heading = heading % 360;

		//Redessine l'instrument seulement si la valeur a changee
		if(heading != this.heading)
		{
			this.heading = heading;
			
			graduations.angleDepart = 270 - heading;
			
			this.repaint();
		}

	}
	
	/**
	 * Retourne le cap affiche sur le compas
	 * @return
	 */
	public int getHeading()
	{
		return this.heading;
	}
}
