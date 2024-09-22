import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Cette classe permet de creer un chronometre (instrument)
 * @author MATHIEU
 *
 */
public class Chronometre  extends InstrumentDeBord implements MouseListener{
	
	//Declarations
	private float floatSec;
	
	private Font chronometreFont;
	
	private int secondes,minutes,heures;
	
	private boolean pauseChrono;
	
	/**
	 * Constructeur de la classe Chronometre
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public Chronometre(int tailleInstrument, Color couleurFond)
	{
		super(tailleInstrument, couleurFond);
		
		chronometreFont= new Font("LUCIDA CONSOLE",Font.BOLD,tailleInstrument/9);
		
		pauseChrono = false;
		
		this.floatSec = 0;
		
		addMouseListener(this);
	}

	public void paint(Graphics gc)
	{
		int rayon,sizeCercle;
		
		String strHeure,strMinute,strSeconde;
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, dimInstrument.width, dimInstrument.height, this);
		tampon.clearRect(0,0,dimInstrument.width,dimInstrument.height);
		
		//calcul de la taille du cercle noir au fond de l'instrument
		rayon = (int)(tailleInstrument / 2.4);
		sizeCercle = rayon*2 + rayon/5;

		//Dessine le cercle centrale de l'instrument, avec un contour noir
		tampon.setColor(Color.gray);
		tampon.fillOval(centreX - sizeCercle/2,centreY - sizeCercle/2, sizeCercle,sizeCercle);
		tampon.setColor(Color.black);
		tampon.drawOval(centreX - sizeCercle/2,centreY - sizeCercle/2-1, sizeCercle,sizeCercle-1);
		
		//Affiche CHRONOMETER sur l'instrument
		tampon.setColor(Color.WHITE);
		tampon.setFont(inscriptionFont);
		tampon.drawString("CHRONOMETER", centreX-(11* inscriptionFont.getSize()/4), (int)(centreY-(dimInstrument.width /  5)));
		
		//Dessine un rectangle noir ou le temps sera affiche
		tampon.setColor(Color.black);
		tampon.fillRect(centreX - (int)(tailleInstrument /3), centreY - tailleInstrument/9 , (int)(tailleInstrument /1.5), tailleInstrument/7 );
		
		//affiche le temps
		tampon.setFont(chronometreFont);

		tampon.setColor(Color.red);
		
		//Converti les valeurs en chaines de caracteres
		strHeure = String.valueOf(heures);
		strMinute = String.valueOf(minutes);
		strSeconde = String.valueOf(secondes);
		
		if (strHeure.length() == 1) strHeure = "0" + strHeure;
		if (strMinute.length() == 1) strMinute = "0" + strMinute;
		if (strSeconde.length() == 1) strSeconde = "0" + strSeconde;
		
		tampon.drawString( strHeure + ":" + strMinute + ":" + strSeconde, centreX - (int)(tailleInstrument /3.5), centreY);

		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'ADF a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Met a jour le chronometre en fonction de l'interval de la boucle principale.
	 * methode a inserer dans la boucle
	 * @param dT en millisecondes
	 */
	public void UpdateChronometre(int dT)
	{
		//Si le chrono est en pause, on passe le chemin
		if (pauseChrono == false)
		{
			floatSec += (float)(dT/1000f);
			
			secondes = (int)floatSec % 60;
			
			minutes = (int)(floatSec / 60f)%60;
			
			heures = (int)(floatSec / 3600f);
		}
			this.repaint();
	}
	
	/**
	 * Met le crhonometre en pause
	 *
	 */
	public void pauseChrono()
	{
		this.pauseChrono = true;
	}
	
	/**
	 * Sort le chronometre du mode pause
	 *
	 */
	public void repriseChrono()
	{
		this.pauseChrono = false;
	}
	
	/**
	 * remet a zero le chronometre
	 *
	 */
	public void razChrono()
	{
		floatSec = 0;
		
		secondes = 0;
		
		minutes = 0;
		
		heures = 0;
		
	}
	
	//Gestion de la souris
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
	
		//Clic bouton gauche, pause/reprise du chronometre
		//Clic bouton droit, remise a zero du chronometre
		
		if (arg0.getButton() == MouseEvent.BUTTON1)
		{
			this.pauseChrono = !this.pauseChrono;
		}
		
		if (arg0.getButton() == MouseEvent.BUTTON3)
		{
			this.razChrono();
		}
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
