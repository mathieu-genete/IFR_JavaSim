import java.awt.*;

/**
 * La classe Anemometre permet d'affiche graphiquement un anemometre gradue en noeuds sur un formulaire ou un panel.
 * La vitesse affichee sur l'instrument est defini avec la methode setVitesse
 * @author GENETE MATHIEU 2008
 *
 */
public class Anemometre extends InstrumentDeBord{


	//declaration des variables privees
		
	private Aiguille aiguille;
	private GraduationsCadrans graduations;
	
	private Font valeursGraduationsFont;
	
	private float vitesse;
	
	//Vitesse maximum affichable par l'anemometre
	private int vitesseMax;

	/**
	 * Constructeur de la classe Anemometre
	 * @param tailleInstrument
	 * @param couleurFond
	 * @param vitesseMax Vitesse maximum affichable par l'anemometre en noeuds (kt)
	 * @param angleDepartArcBlanc zone de vitesse d'utilisation des volets
	 * @param angleFinArcBlanc
	 * @param angleDepartArcVert zone de vitesse normale de l'avion
	 * @param angleFinArcVert
	 * @param angleDepartArcJaune zone de vitesse utilisable en atmosphere calme
	 * @param angleFinArcJaune
	 * @param angleDepartArcRouge zone de vitesse a ne jamais depasser
	 * @param angleFinArcRouge
	 */
	public Anemometre(int tailleInstrument, Color couleurFond,int vitesseMax,float angleDepartArcBlanc,float angleFinArcBlanc,float angleDepartArcVert,float angleFinArcVert,float angleDepartArcJaune,float angleFinArcJaune,float angleDepartArcRouge,float angleFinArcRouge)
	{
		
		super(tailleInstrument, couleurFond);
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,0,vitesseMax,300,0,20,5);
		aiguille = new Aiguille((int)(tailleInstrument / 2.8),(int)(tailleInstrument / 20),Color.white,0,vitesseMax,300,0);
		aiguille.setStyleAiguille(Aiguille.typeAiguille.longue);
		
		//ajoute les arcs colores
		graduations.addArcCouleur(this,angleDepartArcBlanc,angleFinArcBlanc,(int)(tailleInstrument / 2.4),(tailleInstrument / 35),new Color(226,226,226));
		graduations.addArcCouleur(this,angleDepartArcVert,angleFinArcVert,(int)(tailleInstrument / 2.6),(tailleInstrument / 35),new Color(0,113,0));
		graduations.addArcCouleur(this,angleDepartArcJaune,angleFinArcJaune,(int)(tailleInstrument / 2.6),(tailleInstrument / 35),new Color(225,223,47));
		graduations.addArcCouleur(this,angleDepartArcRouge,angleFinArcRouge,(int)(tailleInstrument / 2.9),(tailleInstrument / 10),new Color(202,0,37));
		
		//vitesseKt est initialise a 0
		this.vitesseMax = vitesseMax;
		this.vitesse=0;
	}

	
	//Methodes publiques
	 
	public void paint(Graphics gc)
	{
		//Les coordonnees des differents objets sont tous relatifs a la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.
		
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
		
		
		//Affiche l'inscription "Airspeed" et "konts" sur l'anemometre
		//la police de caractere est souvegardee avant modification
		//pour etre restituee
		tampon.setColor(Color.white);
		ancienneFont = tampon.getFont();
		
		tampon.setFont(inscriptionFont);
		tampon.drawString("Airspeed", centreX-(8* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  10)));
		tampon.drawString("knots", centreX-(5* inscriptionFont.getSize()/4), (int)(centreY+(dimInstrument.width /  10)));
		
		tampon.setFont(ancienneFont);
		
		//Dessine l'aiguille
		aiguille.setCentre(centreX,centreY);
		aiguille.DessineAiguille(tampon);
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'anemometre a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}


	/**
	 * Retourne la vitesse indiquee sur l'instrument en m/s
	 * @return
	 */
	public float getVitesse() {
		return vitesse;
	}

	/**
	 * Definit la vitesse a afficher sur l'instrument
	 * @param vitesse m/s
	 */
	public void setVitesse(float vitesse) {
		
		float vitKt;
		
		//L'instrument affiche des vitesses en noeuds
		vitKt = vitesse * 1.9438f;
		
		if (vitesse <0) vitesse = 0;
		if (vitKt >vitesseMax) vitesse = vitesseMax * 0.51f;
		
		//Redessine l'instrument seulement si la valeur a changee
		if (vitesse != this.vitesse)
		{
			
			
			//les valeurs sont mises a jour
			this.vitesse = vitesse;
			
			
			this.aiguille.setValue(vitKt);
			this.repaint();
		}

	}
	
}
