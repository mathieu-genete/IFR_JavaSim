import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Dessine un bouton avec un intitule. Quand le bouton est actif, l'intitule est rouge, sinon l'intitule est gris
 * @author GENETE MATHIEU 2008
 *
 */
public class BoutonAvion  extends Canvas implements MouseListener{
	
	private int taille;
	
	private String texteBouton;
	
	private boolean BoutonActive;
	
	private Font affichageFont;
	
	private Color couleurBordure;
	
	private PaintDoubleBuffering doubleBuffering;
	
	/**
	 * Constructeur de la classe BoutonAvion
	 * @param taille
	 * @param texteBouton seul les 9 premiers caracteres seront affiche
	 */
	public BoutonAvion(int taille, String texteBouton)
	{
		
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		doubleBuffering = new PaintDoubleBuffering();
		
		affichageFont= new Font("LUCIDA CONSOLE",Font.BOLD,(int)(taille/6.4));
		
		this.BoutonActive = false;
		
		//La taille du texte ne peut etre superieur a 9
		int longText = texteBouton.length();
		
		if (longText > 9) longText = 9;
		
		this.texteBouton = texteBouton.substring(0, longText);
		
		this.couleurBordure = new Color(80,80,80);
		
		this.taille = taille;
		
		this.setSize(this.taille,this.taille);
		
		addMouseListener(this);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics gc)
	{
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, taille, taille, this);
		tampon.setFont(affichageFont);
		tampon.clearRect(0,0,taille,taille);
		
		tampon.setColor(Color.black);
		tampon.fillRect(0, 0, taille, taille);
		

		if (this.BoutonActive == true)
		{
			//Dessin du relief du bouton
			tampon.setColor(Color.gray);
			tampon.drawRect(-2, -2, taille, taille);
			tampon.setColor(couleurBordure);
			tampon.drawRect(1, 1, taille, taille);
			
			//Couleur du texte du bouton
			tampon.setColor(Color.red);

		}
		else
		{	
			//Dessin du relief du bouton
			tampon.setColor(Color.gray);
			tampon.drawRect(1, 1, taille, taille);
			tampon.setColor(couleurBordure);
			tampon.drawRect(-2, -2, taille, taille);
			
			//Couleur du texte du bouton
			tampon.setColor(Color.darkGray);

		}
		
		tampon.drawString(texteBouton, taille/2 - ((texteBouton.length()+2)* affichageFont.getSize()/4), taille/2);
		
		tampon.setColor(Color.black);
		tampon.drawRect(0, 0, taille-1, taille-1);
		
		//Dessine le bouton a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}

	/**
	 * Indique si le bouton est active
	 * @return
	 */
	public boolean isBoutonActive() {
		return BoutonActive;
	}

	/**
	 * Active ou desactive le bouton
	 * @param boutonActive
	 */
	public void setBoutonActive(boolean boutonActive) {
		BoutonActive = boutonActive;
		this.repaint();
	}

	
	//Evenements relatifs a la souris
	
	public void mouseClicked(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		this.setBoutonActive(!this.isBoutonActive());
		
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
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Raccord de méthode auto-généré
		
	}

}
