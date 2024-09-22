import java.awt.*;


/**
 * Cette classe permet de crer un ADF (instrument de bord)
 * @author mathieu
 *
 */
public class Adf  extends InstrumentsIFR{

	private float capAiguille, tmpCapAiguille;
	
	private Aiguille aiguilleAdf;
	
	private Font valeursGraduationsFont;
	
	/**
	 * Constructeur de la classe Adf
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public Adf(int tailleInstrument, Color couleurFond) {
		super(tailleInstrument, couleurFond);

		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/16);
		
		aiguilleAdf = new Aiguille((int)(tailleInstrument / 3),(int)(tailleInstrument / 30),new Color(255,209,5),0,359,359,0);
		aiguilleAdf.setStyleAiguille(Aiguille.typeAiguille.adf);
		aiguilleAdf.setCentre(centreX, centreY);
		
		graduations.AfficheMarqueursStatiques();
	
		capAiguille = 0;
		
		tmpCapAiguille = 0;
	}
	
	public void paint(Graphics gc)
	{
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, dimInstrument.width, dimInstrument.height, this);
		tampon.setFont(valeursGraduationsFont);
		tampon.clearRect(0,0,dimInstrument.width,dimInstrument.height);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,dimInstrument.width,dimInstrument.height);
		
		dessineInstruIFR(tampon);
		
		//Dessine l'aiguille
		aiguilleAdf.DessineAiguille(tampon);
				
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'ADF a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Definit le cap que l'aiguille doit indiquer sur l'instrument
	 * @param cap
	 */
	public void setCapAiguille(float cap)
	{
		cap = cap % 360;
		
		if (cap != this.capAiguille)
		{
			this.aiguilleAdf.setValue(cap);
			this.capAiguille = cap;
			
			this.repaint();
		}
	}
	/**
	 * Definit le cap que l'aiguille doit indiquer sur l'instrument. Cette methode permet de reproduire l'inertie de l'aiguille.
	 * Pour fonctionner correctement elle doit etre dans la boucle principale du programme. Dans les autres cas, utiliser setCapAiguille
	 * @param cap
	 */

	public void setCapAiguilleAvecInertie(float cap)
	{
		float tmp;
		
		cap = cap % 360;
		
		if (cap != this.capAiguille)
		{
						
			tmp = ((cap + 450) - this.capAiguille)%360;

			tmp = tmp - 90;
			
			tmpCapAiguille += tmp/5f;
			
			tmpCapAiguille = (tmpCapAiguille + 360)%360;
			
			this.aiguilleAdf.setValue(tmpCapAiguille);
			this.capAiguille = (tmpCapAiguille);
			
			this.repaint();
		}
	}
	
	/**
	 * retourne le cap affiche par l'aiguille
	 * @return
	 */
	public float getCapAiguille()
	{
		return this.capAiguille;
	}

}
