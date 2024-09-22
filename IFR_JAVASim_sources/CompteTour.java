import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


/**
 * La classe CompteTour permet d'affiche graphiquement un compte tour sur un formulaire ou un panel.
 * La vitesse en tour minute affichee sur l'instrument est defini avec la methode setRpm
 * @author GENETE MATHIEU 2008
 *
 */
public class CompteTour extends InstrumentDeBord {

	//declaration des variables privees
	
	private Aiguille aiguille;
	private GraduationsCadrans graduations;
	
	private Font valeursGraduationsFont;
	
	private int rpmValue;
	private int rpmValMax;
	
	/**
	 * Constructeur de la classe CompteTour
	 * @param tailleInstrument
	 * @param couleurFond
	 * @param rpmValMax
	 * @param angleDepartArcVert zone d'utilisation normale
	 * @param angleFinArcVert
	 * @param angleDepartArcRouge zone d'utilisation non continue
	 * @param angleFinArcRouge
	 */
	public CompteTour(int tailleInstrument, Color couleurFond, int rpmValMax,float angleDepartArcVert,float angleFinArcVert,float angleDepartArcRouge,float angleFinArcRouge)
	{
		super(tailleInstrument, couleurFond);
		
		/*
		 * Cree les differents objets dont l'altimetre a besoin. Tout depend de la taille de l'intrument
		 * entree en parametre
		 */
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,0,rpmValMax,290,180,500,100);
		aiguille = new Aiguille((int)(tailleInstrument / 2.8),(int)(tailleInstrument / 20),Color.white,0,rpmValMax,290,180);
		
		//ajoute les arcs colores
		graduations.addArcCouleur(this, angleDepartArcVert, angleFinArcVert, (int)(tailleInstrument / 2.5), (int)(tailleInstrument / 35), new Color(0,113,0));
		graduations.addArcCouleur(this, angleDepartArcRouge, angleFinArcRouge, (int)(tailleInstrument / 2.9), (int)(tailleInstrument / 10), new Color(202,0,37));
		
		this.rpmValMax = rpmValMax;
		
	}
	
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
		
		//Affiche l'inscription "rpm" sur le compte tour
		//la police de caractere est souvegardee avant modification
		//pour etre restituee
		tampon.setColor(Color.white);
		ancienneFont = tampon.getFont();
		
		tampon.setFont(inscriptionFont);
		tampon.drawString("RPM", centreX-(3* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  10)));
		
		tampon.setFont(ancienneFont);
		
		//Dessine l'aiguille
		aiguille.setCentre(centreX,centreY);
		aiguille.DessineAiguille(tampon);
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine le compte tour a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Retourne la valeur affichee sur le compte tour
	 * @return
	 */
	public int getRpm()
	{
		return this.rpmValue;
	}
	
	/**
	 * Definit la valeur a afficher sur le compte tour
	 * @param valRpm
	 */
	public void setRpm(int valRpm)
	{
		if (valRpm > rpmValMax) valRpm = rpmValMax;
		if(valRpm <0) valRpm = 0;
		
		//Redessine l'instrument seulement si la valeur a changee
		if (valRpm != this.rpmValue)
		{
			this.rpmValue = valRpm;
			
			aiguille.setValue(this.rpmValue);
			
			this.repaint();
		}

	}
	
}
