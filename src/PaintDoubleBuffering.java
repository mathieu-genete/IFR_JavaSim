import java.awt.Graphics;
import java.awt.Image;
import java.awt.*;

/**
 *Cette Class permet de realiser les graphismes dans une image en memoire. Cette image
 *est ensuite affichee dans le contexte graphique "reel". Cette technique permet
 *d'eviter les clignotements duent a la methode paint
 *Attention, pour un resultats parfait, il ne faut pas oublier d'ajouter cette methode:
 *
 *public void update(Graphics g)
 *{
 *		paint(g);
 *}
 *
 *dans la class ou vous avez declare un objet PaintDoubleBuffering
 * @author GENETE MATHIEU 2008
 *
 */
public class PaintDoubleBuffering {

	private Image imagecache;

	private Graphics tampon;

	private Graphics reelGc;

	/**
	 *Cette methode cree un contexte graphique "virtuel" d'apres les parametres
	 *entres. Elle retourne l'adresse du contexte graphique "virtuel".
	 * @param realGc
	 * @param gcWidth
	 * @param gcHeight
	 * @param canvas
	 * @return Graphics adresse du contexte graphique "virtuel"
	 */
	public Graphics setReelVersVirtuelGraphics(Graphics realGc, int gcWidth, int gcHeight, Canvas canvas) {
		
		this.reelGc = realGc;

		//verifie si tampon contient un lien vers un contexte graphique virtuel
		if (tampon == null) {

			this.imagecache = canvas.createImage(gcWidth, gcHeight);
			this.tampon = this.imagecache.getGraphics();
		
		}

		return this.tampon;
	}


	/**
	 * Cette methode permet d'afficher l'image "virtuel" dans le contexte graphique "reel"
	 */
	public void dessineVirtuelVersReelGraphics() {
		this.reelGc.drawImage(imagecache, 0, 0,null);
	}
	
	/**
	 *Cette methode permet d'afficher l'image "virtuel" dans le contexte graphique "reel".
	 *Redimensionne l'image en fonction des parametres entres
	 * @param width
	 * @param height
	 */
	public void dessineVirtuelVersReelGraphics(int width,int height) {
		this.reelGc.drawImage(imagecache, 0, 0,width,height,null);
	}

	/**
	 * retourne l'adresse du contexte graphique "virtuel"
	 * @return Graphics
	 */
	public Graphics getVirtuelGc() {
		return this.tampon;
	}

	/**
	 *reinitialise l'adresse du tampon
	 */
	public void resetDoubleBuffering() {
		this.tampon = null;
	}

}
