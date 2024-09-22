import java.awt.*;


/**
 * La classe IndicateurVirage permet d'afficher graphiquement un indicateur de virage sur un formulaire ou un panel.
 * Le changement du taux de virage s'effectue via la methode setTauxVirage
 * @author   GENETE MATHIEU 2008
 */
public class IndicateurVirage extends InstrumentDeBord {

	private DessinIndicateurVirage dessinIndicVirage;
	float tauxVirage;
	int tailleInstrument;
	
	public IndicateurVirage(int tailleInstrument, Color couleurFond)
	{		
		super(tailleInstrument, couleurFond);
		
		dessinIndicVirage = new DessinIndicateurVirage(tailleInstrument);
		
		this.tailleInstrument = tailleInstrument;
	}
	
	public void paint(Graphics gc) {

		int sizeCercle,rayon;
		
		//Les coordonnees des differents objets sont tous relatifs a la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.

		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.clearRect(0,0,tailleInstrument, tailleInstrument);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument, tailleInstrument);
		
		//calcul de la taille du cercle noir au fond de l'instrument
		rayon = (int)(tailleInstrument / 2.4);
		sizeCercle = rayon*2 + rayon/5;

		tampon.setColor(Color.black);
		tampon.fillOval(centreX - sizeCercle/2,centreY - sizeCercle/2, sizeCercle,sizeCercle);
		
		dessinIndicVirage.DessineMaquette(tampon, tauxVirage * 10);
		
		tampon.setColor(Color.white);
		tampon.setFont(inscriptionFont);
		tampon.drawString("2 MIN", centreX-(5* inscriptionFont.getSize()/4), (int)(centreY+(dimInstrument.width /  3)));
		tampon.drawString("TURN COORDINATOR", centreX-(20* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  5)));

		tampon.drawString("L", centreX -(dimInstrument.width / 3) , (int)(centreY+(dimInstrument.width /  4)));
		tampon.drawString("R", centreX +(dimInstrument.width / 3) , (int)(centreY+(dimInstrument.width /  4)));

		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'indicateur de virage a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 *Definition du taux du virage en degres par seconde.
	 *Un virage standar correspond a 3 degres par seconde. 
	 * @param tauxVirage
	 */
	public void setTauxVirage(float tauxVirage)
	{
		
		if (tauxVirage > 4) tauxVirage = 4;
		if (tauxVirage < -4) tauxVirage = -4;
		
		if (tauxVirage != this.tauxVirage)
		{
			this.tauxVirage = tauxVirage;
			this.repaint();
		}
	}
	
	/**
	 * Retourne le taux du virage affiche par l'instrument
	 * @return
	 */
	public float getTauxVirage()
	{
		return this.tauxVirage;
	}
}
