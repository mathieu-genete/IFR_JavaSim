import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Cette classe permet de creer une manette de controle sur le tableau de bord. Il est possible de controler la manette a la souris
 * la valeur minimum est 0 et la valeur maximum 1000
 * @author GENETE MATHIEU 2008
 *
 */
public class ManetteControle extends InstrumentDeBord implements MouseListener, MouseMotionListener{

	private int valManette;
	private int tmpVal, tmpY;
	private final int valueMax = 1000;
	
	private String titreManette, strValMax, strValCentre, strValMin;
	
	private Color couleurManette, couleurTextes;
	
	/**
	 * Constructeur de la classe ManetteControle
	 * @param tailleInstrument
	 * @param couleurFond
	 * @param couleurManette
	 * @param couleurTextes
	 * @param titreManette
	 */
	public ManetteControle(int tailleInstrument, Color couleurFond, Color couleurManette,Color couleurTextes,String titreManette)
	{
		
		super(tailleInstrument, couleurFond);
		
		this.couleurManette = couleurManette;
		this.couleurTextes = couleurTextes;
		
		this.valManette=0;
		this.titreManette = titreManette;
		
		this.strValMax ="- 100 %";
		this.strValCentre = "- 50 %";
		this.strValMin = "- 0 %";
		
		addMouseListener(this);
		
		addMouseMotionListener(this);
	}

	
	public void paint(Graphics gc)
	{
		int coordValMin, coordValMax,coordValue;
		//Les coordonnees des differents objets sont tous relatifs a la taille de l'instrument afin d'avoir un affichage identique quelque
		//soit la taille.
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleInstrument, tailleInstrument, this);
		tampon.clearRect(0,0,tailleInstrument,tailleInstrument);
		
		//definit la couleur du fond
		tampon.setColor(couleurFond);
		tampon.fillRect(0,0,tailleInstrument,tailleInstrument);

		coordValMax =centreY - (tailleInstrument/4);
		coordValMin =  coordValMax + tailleInstrument/2;
		
		coordValue = (int)((((float)coordValMax - (float)coordValMin)/valueMax) * this.valManette) + coordValMin;
		
		tampon.setColor(Color.darkGray);
		tampon.fillRect((centreX - tailleInstrument/7) + 3, coordValue + 3, tailleInstrument/6, tailleInstrument/20);
		
		tampon.setColor(Color.black);
		tampon.fillRect(centreX - tailleInstrument/10, coordValMax, tailleInstrument/10, tailleInstrument/2);
				
		tampon.setColor(this.couleurManette);
		tampon.fillRect(centreX - tailleInstrument/7, coordValue, tailleInstrument/6, tailleInstrument/20);
		
		tampon.setColor(Color.black);
		if (this.couleurManette == Color.black)
		{
			tampon.setColor(Color.darkGray);
		}
		tampon.drawRect(centreX - tailleInstrument/7, coordValue, tailleInstrument/6, tailleInstrument/20);
		
		//Affiche les infos sur le cote de l'instrument
		
		tampon.setColor(couleurTextes);
		
		tampon.drawString(titreManette, centreX-(titreManette.length()* inscriptionFont.getSize()/4), tailleInstrument/8);
		
		tampon.drawString(strValMax,centreX + tailleInstrument/20, coordValMax);
		tampon.drawString(strValCentre,centreX + tailleInstrument/20, coordValMin + (coordValMax - coordValMin)/2);
		tampon.drawString(strValMin,centreX + tailleInstrument/20, coordValMin);
		//dessine les vis sur l'instrument
		dessineVis(tampon,10);
		
		//Dessine l'instrument a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * Definit la valeur de la manette
	 * @param value 0 pour minimum et 1000 pour maximum
	 */
	public void setValManette(int value)
	{
		if (value <0) value = 0;
		if (value > this.valueMax) value = valueMax;
		
		if (value != this.valManette)
		{
			this.valManette = value;
			
			this.repaint();
		}
	}
	
	/**
	 * retourne la valeur de la manette
	 * @return entier entre 0 et 1000
	 */
	public int getValManette()
	{
		return this.valManette;
	}
	
	/**
	 * Definit les textes a afficher pour la valeur maximum, la valeur minimum et la valeur du milieu
	 * @param strValMax
	 * @param strValCentre
	 * @param strValMin
	 */
	public void setAffichage(String strValMax, String strValCentre, String strValMin)
	{
		this.strValMax = strValMax;
		
		this.strValCentre = strValCentre;
		
		this.strValMin = strValMin;
		
	}

	/*
	 * Gestion des evennements souris
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}


	public void mouseEntered(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}


	public void mouseExited(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}


	public void mousePressed(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		tmpVal = this.valManette;
		tmpY = e.getY();
	}


	public void mouseReleased(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}


	public void mouseDragged(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		setValManette( tmpVal + (tmpY - e.getY())*4);
	}


	public void mouseMoved(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}

	


}
