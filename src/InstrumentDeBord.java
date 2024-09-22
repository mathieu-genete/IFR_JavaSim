import java.awt.*;
/**
 * Classe abstraite qui definit les parametres communs aux differents instruments de bord
 * @author GENETE MATHIEU 2008
 *
 */
abstract class InstrumentDeBord extends Canvas {

	//Declaration des variables
	protected Font ancienneFont;
	protected Font inscriptionFont;
	protected Dimension dimInstrument;
	
	protected int centreX,centreY;
	
	protected int tailleInstrument;
	
	protected Color couleurFond;
	
	protected PaintDoubleBuffering doubleBuffering;
	
	/**
	 * Constructeur de la classe InstrumentDeBord
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public InstrumentDeBord(int tailleInstrument, Color couleurFond)
	{
		dimInstrument = new Dimension();
		/*Definit les deux polices de caractere la 1ere pour les inscription sur l'instrument
		 * la seconde pour les valeurs des graduations
		*/
		inscriptionFont = new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/20);
		
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		doubleBuffering = new PaintDoubleBuffering();
		
		//La taille du canvas est ajuste a la taille entree
		this.dimInstrument.width = tailleInstrument;
		this.dimInstrument.height = tailleInstrument;
		this.setSize(dimInstrument);
		
		this.tailleInstrument = tailleInstrument;
		
		centreX = tailleInstrument/2;
		centreY = tailleInstrument/2;
		
		//La couleur de fond est initialisee
		this.couleurFond = couleurFond;
	}
	
	/**
	 *methode indispensable pour le bon fonctionnement
	 *du double buffering
	 */
	public void update(Graphics g)
	{
		paint(g);
	}
	
	/**
	 * Dessine l'instrument dans le contexte graphique specifie. La methode repaint() est cependant recommandee
	 * pour rafraichir l'affichage de l'instrument.
	 * @param gc
	 */
	public void paint(Graphics gc)
	{
	}
	
	
	//METHODES PROTEGEES
	
	/**
	 * Cette fonction dessine 4 vis cruciforme dans le contexte graphique gc
	 * @param gc
	 * @param facteurXY
	 */
	protected void dessineVis(Graphics gc, int facteurXY)
	{
		//declarations
		Color ancienneCouleur;
		
		int centreVisX,centreVisY, tailleVis, facteurTaille;

		//la couleur du contexte graphique est sauvegardee
		ancienneCouleur = gc.getColor();
		
		//facteur de taille pour la vis. Plus il est petit, plus la vis est grande
		facteurTaille = 15;
	
		tailleVis = (int)(tailleInstrument/facteurTaille);
		centreVisX = (int)(tailleInstrument/facteurXY + tailleVis/2);
		centreVisY = (int)(tailleInstrument/facteurXY + tailleVis/2);
		
		//dessine les quatres vis
		this.visCruciforme(gc, tailleVis, centreVisX, centreVisY, tailleInstrument);
		
		centreVisX = (int)(tailleInstrument - (tailleInstrument/facteurXY + tailleVis/2));
		
		this.visCruciforme(gc, tailleVis, centreVisX, centreVisY, tailleInstrument);
		
		centreVisY = (int)(tailleInstrument - (tailleInstrument/facteurXY + tailleVis/2));
		
		this.visCruciforme(gc, tailleVis, centreVisX, centreVisY, tailleInstrument);
		
		centreVisX = (int)(tailleInstrument/facteurXY + tailleVis/2);
		
		this.visCruciforme(gc, tailleVis, centreVisX, centreVisY, tailleInstrument);
		
		//restitue sa couleur au contexte graphique
		gc.setColor(ancienneCouleur);
	}
	
	//METHODES PRIVEES
	
	
	/**
	 * Dessine une vis cruciforme
	 */
	private void visCruciforme(Graphics gc, int tailleVis,int centreVisX, int centreVisY,int tailleInstrument)
	{

		//Dessine l'ombre de la vis
		gc.setColor(Color.black);
		gc.fillOval(centreVisX+1-tailleVis/2, centreVisY+1-tailleVis/2, tailleVis ,tailleVis);
		
		//Dessine la vis
		gc.setColor(Color.darkGray);
		gc.fillOval(centreVisX-tailleVis/2, centreVisY-tailleVis/2, tailleVis ,tailleVis);
		
		gc.setColor(Color.lightGray);
		gc.drawLine(centreVisX+tailleVis/4, centreVisY+tailleVis/4, centreVisX-tailleVis/4, centreVisY-tailleVis/4);
		gc.drawLine(centreVisX-tailleVis/4, centreVisY+tailleVis/4, centreVisX+tailleVis/4, centreVisY-tailleVis/4);
		
		centreVisX++;
		gc.drawLine(centreVisX+tailleVis/4, centreVisY+tailleVis/4, centreVisX-tailleVis/4, centreVisY-tailleVis/4);
		gc.drawLine(centreVisX-tailleVis/4, centreVisY+tailleVis/4, centreVisX+tailleVis/4, centreVisY-tailleVis/4);
		

	}
	
}
