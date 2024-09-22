import java.awt.*;


/**
 * La classe Variometre permet d'affiche graphiquement un variometre sur un formulaire ou un panel.
 * La vitesse verticale affichee sur l'instrument est defini avec la methode setVitesseVerticale
 * @author GENETE MATHIEU 2008
 *
 */
public class Variometre extends InstrumentDeBord{
	
	//declaration des variables privees
	
	private Aiguille aiguille;
	
	private GraduationsCadrans graduations;
	
	private Font valeursGraduationsFont;
		
	private float vitesseVerticale;

	private  int vitesseVerticaleMax; 
	
	/**
	 * constructeur de la classe Variometre
	 * @param tailleInstrument
	 * @param couleurFond
	 * @param vitesseVerticaleMax ft/min
	 */
	public Variometre(int tailleInstrument, Color couleurFond,int vitesseVerticaleMax)
	{
		super(tailleInstrument, couleurFond);
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		/*
		 * Cree les differents objets dont l'altimetre a besoin. Tout depend de la taille de l'intrument
		 * entree en parametre
		 */
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,-vitesseVerticaleMax/100,vitesseVerticaleMax/100,320,270,5,1);
		aiguille = new Aiguille((int)(tailleInstrument / 2.8),(int)(tailleInstrument / 20),Color.white,0,vitesseVerticaleMax*2,320,110);
		
		//Les graduations sont en valeur absolue
		graduations.setAfficheValeursAbsolu(true);
		
		
		this.vitesseVerticaleMax = vitesseVerticaleMax;
		
		vitesseVerticale = 0;
		aiguille.setValue(this.vitesseVerticaleMax+vitesseVerticale);
	}

	//METHODES PUBLIC

	public void paint(Graphics gc)
	{
		//Les coordonnees des differents objets sont tous relatifs � la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.
		
//		Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.setFont(valeursGraduationsFont);
		tampon.clearRect(0,0,tailleInstrument,tailleInstrument);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument,tailleInstrument);
		
		//Dessine les graduations
		graduations.setCentre(centreX,centreY);
		graduations.dessineGraduations(tampon);
		
		//Affiche l'inscription "Vertical speed" et "x 1000 ft" sur le variometre
		//la police de caractere est souvegardee avant modification
		//pour etre restituee
		tampon.setColor(Color.white);
		ancienneFont = tampon.getFont();
		
		tampon.setFont(inscriptionFont);
		tampon.drawString("Vertical speed", centreX-(16* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  10)));
		tampon.drawString("x 100 ft", centreX -(9* inscriptionFont.getSize()/4), (int)(centreY +(dimInstrument.width /  10)));
		
		tampon.setFont(ancienneFont);
		
		//Dessine l'aiguille
		aiguille.setCentre(centreX,centreY);
		aiguille.DessineAiguille(tampon);
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine le variometre a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}

	/**
	 * Retourne la vitesse verticale affichee sur l'instrument
	 * @return m/s
	 */
	public float getVitesseVerticale() {
		return vitesseVerticale;
	}


	/**
	 * Definit la vitesse verticale a afficher sur l'instrument
	 * @param vitesseVerticale m/s
	 */
	public void setVitesseVerticale(float vitesseVerticale) {
		
		float vitVertFtMin;
		//le variometre affiche des pieds par minute
		vitVertFtMin = vitesseVerticale * 60f * 3.281f;
		
		if (vitVertFtMin > vitesseVerticaleMax) vitesseVerticale = (vitesseVerticaleMax/60f)/3.281f;
		if (vitVertFtMin < -vitesseVerticaleMax) vitesseVerticale = -(vitesseVerticaleMax/60f)/3.281f;
		
		//Redessine l'instrument seulement si la valeur a changee
		
		if (vitesseVerticale != this.vitesseVerticale)
		{
			//la valeur de la vitesse verticale est mise a jour
			this.vitesseVerticale = vitesseVerticale;
			
			//L'aiguille est positionnee a cette valeur
			this.aiguille.setValue(this.vitesseVerticaleMax+vitVertFtMin);
			
			//L'instrument est redessine
			this.repaint();
		}
		
	}

}
