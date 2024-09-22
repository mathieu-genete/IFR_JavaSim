import java.awt.*;
import java.awt.event.WindowEvent;


/**
 * Cette classe permet de creer un VOR (instrument de bord)
 * @author GENETE mathieu 2008
 *
 */
public class Vor extends InstrumentsIFR {

	public enum Flag {TO, OFF, FROM}
	
	private float deviationRoute;
	
	private float tmpDeviation;
	
	private float echelleGradDev;
	
	private Flag flagVor;
	
	private Font valeursGraduationsFont;
	
	/**
	 * Conctructeur de la classe Vor
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public Vor(int tailleInstrument, Color couleurFond) {
		super(tailleInstrument, couleurFond);
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		graduations.AfficheMarqueursStatiques(90, 270, 180, Color.yellow);
		
		flagVor = Flag.OFF;
		
		echelleGradDev = (tailleInstrument / 5)/10;
		
		deviationRoute = 0;
		
		tmpDeviation = 0;
		
	}

	public void paint(Graphics gc)
	{
		int calculDev;
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, dimInstrument.width, dimInstrument.height, this);
		tampon.setFont(valeursGraduationsFont);
		tampon.clearRect(0,0,dimInstrument.width,dimInstrument.height);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,dimInstrument.width,dimInstrument.height);
		
		dessineInstruIFR(tampon);
		
		tampon.setColor(Color.darkGray);
		
		tampon.drawLine(centreX, (int)(centreY-tailleInstrument / 10), centreX, (int)(centreY+tailleInstrument /10));
		
		for(int i = centreX - (int)(tailleInstrument / 5); i <= centreX + (int)(tailleInstrument / 5);i+=echelleGradDev*2)
		{
			tampon.drawLine(i, (int)(centreY-tailleInstrument / 40), i, (int)(centreY+tailleInstrument /40));
		}
		
		tampon.setColor(Color.white);
		
		tampon.setFont(inscriptionFont);
		tampon.drawString(String.valueOf(this.flagVor), centreX+ (int)(tailleInstrument / 8), centreY-(int)(tailleInstrument /  10));
		
		calculDev = (int)(deviationRoute * echelleGradDev);
		tampon.drawLine(centreX + calculDev, (int)(centreY-tailleInstrument / 5), centreX + calculDev, (int)(centreY+tailleInstrument /5));
		
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine le vor a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Deviation en degres a afficher. L'instrument n'affiche que les deviations inferieurs a 10 degres
	 * et superieur a -10 degres
	 * @param devDegres
	 */
	public void setDeviation(float devDegres)
	{
		if (devDegres > 10) devDegres = 10;
		if (devDegres < -10) devDegres = -10;
		
		if (devDegres != this.deviationRoute)
		{
			this.deviationRoute = devDegres;
			
			this.repaint();
		}
	}
	
	/**
	 * Deviation en degres a afficher. L'instrument n'affiche que les deviations inferieurs a 10 degres
	 * et superieur a -10 degres. Cette methode permet de reproduire l'inertie de l'aiguille. Pour fonctionner correctement
	 * elle doit etre dans la boucle principale du programme. Dans les autres cas, utiliser setDeviation
	 * @param devDegres
	 */
	public void setDeviationAvecInertie(float devDegres)
	{
		if (devDegres > 10) devDegres = 10;
		if (devDegres < -10) devDegres = -10;
		
		if (devDegres != this.deviationRoute)
		{
			tmpDeviation = this.deviationRoute;
			this.deviationRoute += (devDegres - tmpDeviation) / 5f;
			
			this.repaint();
		}
	}
	
	/**
	 * retourne la deviation affichee par l'instrument
	 * @return flottant entre -10 et 10
	 */
	public float getDeviation()
	{
		return this.deviationRoute;
	}
	
	/**
	 * Definit le flag a afficher sur l'instrument TO, OFF ou FROM
	 * @param flag
	 */
	public void setFlagVor(Flag flag)
	{
		this.flagVor = flag;
		
		this.repaint();
	}
	
	/**
	 * retourne le flag affiche par l'instrument
	 * @return
	 */
	public Flag getFlagVor()
	{
		return this.flagVor;
	}


}
