import java.awt.*;

/**
 * La classe Altimetre permet d'afficher graphiquement un altimetre en pieds sur un formulaire ou un panel.
 * Le changement de l'altitude de l'instrument est possible grace a la fonction setAltitude
 * @author   GENETE MATHIEU 2008
 */
public class Altimetre extends InstrumentDeBord{
	
	//declaration des variables privees
	
	private Aiguille aiguilleMilliers;
	private Aiguille grandeAiguille;
	private Aiguille petiteAiguille;
	
	private GraduationsCadrans graduations;
	
	private Font valeursGraduationsFont;
	
	private boolean afficheAiguilleMilliers;
	
	private float altitude;
	

	/**
	 * Constructeur de la classe Altimetre
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public Altimetre(int tailleInstrument, Color couleurFond)
	{

		super(tailleInstrument, couleurFond);
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		/*
		 * Cree les differents objets dont l'altimetre a besoin. Tout depend de la taille de l'intrument
		 * entree en parametre
		 * La graduation va de 0 a 10 (10 et 0 sont confondu). 1 degre de graduation vaut donc 10/360. La graduation
		 * va de 0 a 359 (360 degres) d'ou une valeur de 9,97 pour le degre 359. Idem pour les aiguilles, mais X 100
		 */
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,0,9.97f,359,0,1,0.1f);
		aiguilleMilliers = new Aiguille((int)(tailleInstrument / 2.5),(int)(tailleInstrument / 30),Color.white,0,997,359,0);
		grandeAiguille = new Aiguille((int)(tailleInstrument / 2.8),(int)(tailleInstrument / 20),Color.white,0,997,359,0);
		petiteAiguille = new Aiguille((int)(tailleInstrument / 5),(int)(tailleInstrument / 20),Color.white,0,997,359,0);
		
		this.afficheAiguilleMilliers = false;
		
	}
	

	//Methodes publiques

	public void paint(Graphics gc)
	{
		//Les coordonnees des differents objets sont tous relatifs � la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.setFont(valeursGraduationsFont);
		tampon.clearRect(0,0,tailleInstrument, tailleInstrument);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument, tailleInstrument);
		

		
		//Dessine les graduations
		graduations.setCentre(centreX,centreY);
		graduations.dessineGraduations(tampon);
		
		//Affiche l'inscription "100 feets" et "Altimeter" sur l'altimetre
		//la police de caractere est souvegardee avant modification
		//pour etre restituee
		tampon.setColor(Color.white);
		ancienneFont = tampon.getFont();
		
		tampon.setFont(inscriptionFont);
		tampon.drawString("100 feet", centreX-(9* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  5)));
		tampon.drawString("Altimeter", centreX -(9* inscriptionFont.getSize()/4), (int)(centreY+(dimInstrument.width /  10)));
		
		tampon.setFont(ancienneFont);
		
		//Dessine les deux aiguilles
		petiteAiguille.setCentre(centreX,centreY);
		petiteAiguille.DessineAiguille(tampon);
		
		grandeAiguille.setCentre(centreX,centreY);
		grandeAiguille.DessineAiguille(tampon);
		
		if (afficheAiguilleMilliers == true)
		{
			aiguilleMilliers.setCentre(centreX,centreY);
			aiguilleMilliers.DessineAiguille(tampon);
		}
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'altimetre a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
		 
		
	}

	/** 
	 * Definit l'altitude a afficher sur l'altimetre et rafraichi l'affichage de l'instrument
	 * @param altitude en m
	 */
	public void setAltitude(float altitude)
	{
		float altiFt;
		
		//Redessine l'instrument seulement si la valeur a changee
		if(altitude != this.altitude)
		{
			this.altitude = altitude;
			
			//L'altimetre affiche l'altitude en pieds (ft)
			altiFt = altitude * 3.2808f;
			
			//les valeurs des aiguilles sont mises a jour
			aiguilleMilliers.setValue((altiFt / 100) % 1000);
			grandeAiguille.setValue(altiFt % 1000);
			petiteAiguille.setValue((altiFt / 10) % 1000);
		
			//l'altimetre est redessine
			this.repaint();
		}
	}
	
	/**
	 * Retourne l'altitude affichee par l'altimetre en m
	 * @return float altitude
	 */
	public float getAltitude() {
		return altitude;
	}

	/**
	 * Retourne TRUE si l'aiguille des 10aine de milliers est affichee sur l'altimetre; sinon FALSE
	 * @return boolean
	 */
	public boolean getAfficheAiguilleMilliers() {
		return afficheAiguilleMilliers;
	}


	/**
	 * Determine si l'aiguille des 10aine de milliers est  affichee sur l'altimetre
	 * @param isTrueOrFalse
	 */
	public void setAfficheAiguilleMilliers(boolean isTrueOrFalse) {
		this.afficheAiguilleMilliers = isTrueOrFalse;
	}
}
