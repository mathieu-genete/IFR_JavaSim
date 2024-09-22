import java.awt.*;

/**
 * Cette classe permet de definir et dessiner les differents composants d'un horizon artificiel
 * qui utilisent des coordonnees circulaires
 * 
 * @author GENETE MATHIEU 2008
 *
 */
public class DessinsHorizon extends ObjetsCoordonneesCirculaires{

	//declarations
	
	private Polygon polygon;
	private int centreSolX, centreSolY, largeurSol,longueurSol;

	private Font valeursGraduationsFont;
	
	private Color couleurSol;
	
	private int tailleInstrument;
	
	/**
	 * Constructeur de la classe DessinsHorizon
	 * dimension recoi la taille du canvas, couleurSol recoi la valeur permettant de definir la couleur du sol de l'horizon
	 * @param tailleInstrument
	 * @param couleurSol
	 */
	public DessinsHorizon(int tailleInstrument, Color couleurSol)
	{
		this.tailleInstrument = tailleInstrument;
	
		this.couleurSol =couleurSol;
		
		//Cree un nouvel objet polygon
		polygon = new Polygon();
		
		valeursGraduationsFont= new Font("LUCIDA CONSOLE",Font.PLAIN,tailleInstrument/18);
		
		centreX = (int)(tailleInstrument/2);
		centreY = (int)(tailleInstrument/2);
		
		//La largeur du sol equivaut a la taille de la diagonale du canvas
		largeurSol = (int) Math.sqrt((Math.pow(tailleInstrument, 2) + Math.pow(tailleInstrument, 2)));
		
		//La longueur du sol equivaut a 2 fois la taille en hauteur du canvas
		longueurSol = (int) (tailleInstrument*2);

	}
	
	/**
	 * Cette methode dessine le sol de l'horizon artificiel dans le contexte graphique specifie
	 * @param gc
	 */
	public void dessineSol(Graphics gc, float valueSolY, float valueSolZ,Color couleurCiel, float facteurAffichageAssietteSol)
	{
		//Declarations
		int aX,aY,bX,bY,cX,cY,dX,dY;	
		
		Color ancienneCouleur;
		
		//sauvegarde de la couleur du contexte graphique
		ancienneCouleur  = gc.getColor();
		
		valueSolZ =(valueSolZ - 90) % 360;
		
		//coordonnees du centre de la ligne a-b
		centreSolX = coordPolX(centreX, valueSolZ, valueSolY);
		centreSolY = coordPolY(centreY, valueSolZ, valueSolY);
		
		//Calcul des coordonnees des coins du rectangle
		aX = coordPolX(centreSolX, valueSolZ-90, largeurSol/2);
		aY = coordPolY(centreSolY, valueSolZ-90, largeurSol/2);
		
		bX = coordPolX(centreSolX, valueSolZ+90, largeurSol/2);
		bY = coordPolY(centreSolY, valueSolZ+90, largeurSol/2);
		
		cX = coordPolX(bX, valueSolZ-180, longueurSol);
		cY = coordPolY(bY, valueSolZ-180, longueurSol);
		
		dX = coordPolX(aX, valueSolZ-180, longueurSol);
		dY = coordPolY(aY, valueSolZ-180, longueurSol);
		
		//ajoute les 4 points au polygon avec les coordonnees calculees
		
		polygon.reset();
		polygon.addPoint(aX, aY);
		polygon.addPoint(bX, bY);
		polygon.addPoint(cX, cY); 
		polygon.addPoint(dX, dY);
			
		//dessine les graduations positives sur l'horizon
		gc.setColor(Color.white);
		dessineGraduationsAssiette(gc, valueSolZ, valueSolY, 10,0, 20, tailleInstrument/4, facteurAffichageAssietteSol, true);
		dessineGraduationsAssiette(gc, valueSolZ, valueSolY, 5, 0,25, tailleInstrument/8, facteurAffichageAssietteSol, false);
		
		//dessine un rectangle de la couleur du ciel en haut afin d'eviter au maximum
		//de superposer les graduations avec l'indication de roulis
		gc.setColor(couleurCiel);
		gc.fillRect(0,0,tailleInstrument,tailleInstrument/4);
		
		//dessine le polygone representant le sol
		gc.setColor(couleurSol);
		gc.fillPolygon(polygon);
		
		//dessine une ligne blanche sur l'arete a-b du polygone
		gc.setColor(Color.white);		
		gc.drawLine(aX, aY, bX, bY);
		
		//dessine les graduations negatives sur l'horizon
		dessineGraduationsAssiette(gc, valueSolZ, valueSolY, 10,-20, 0, tailleInstrument/4, facteurAffichageAssietteSol, true);
		dessineGraduationsAssiette(gc, valueSolZ, valueSolY, 5, -25,0, tailleInstrument/8, facteurAffichageAssietteSol, false);
		

		
		//Dessine les objets relatifs au roulis
		
		//Dessin des graduations
		gc.setColor(Color.white);
		dessineGraduationsRoulis(gc);
		
		//Dessin du triangle indiquant l'angle de roulis
		aX = coordPolX(centreX, valueSolZ, tailleInstrument/3);
		aY = coordPolY(centreY,valueSolZ , tailleInstrument/3);
		
		bX = coordPolX(centreX, valueSolZ-5, tailleInstrument/3.5f);
		bY = coordPolY(centreY,valueSolZ-5 , tailleInstrument/3.5f);
		
		cX = coordPolX(centreX, valueSolZ+5, tailleInstrument/3.5f);
		cY = coordPolY(centreY,valueSolZ+5 , tailleInstrument/3.5f);
		
		polygon.reset();
		polygon.addPoint(aX, aY);
		polygon.addPoint(bX, bY);
		polygon.addPoint(cX, cY); 
		//dessine le polygone
		gc.fillPolygon(polygon);
		//dessine un contour noir autour du triangle
		gc.setColor(Color.black);
		gc.drawPolygon(polygon);
		
		//restaure la couleur du contexte graphique
		gc.setColor(ancienneCouleur);
	}
	
	/**
	 * Cette fonction permet de dessiner les graduations sur l'horizon artificiel
	 * @param gc
	 * @param valueSolZ
	 * @param valueSolY
	 * @param pasEnDegres
	 * @param valMax
	 * @param largeurGraduations
	 * @param facteurAffichageAssietteSol
	 * @param afficheValeurs
	 */
	private void dessineGraduationsAssiette(Graphics gc,float valueSolZ,float valueSolY, int pasEnDegres,int valMin, int valMax,int largeurGraduations,float facteurAffichageAssietteSol, boolean afficheValeurs)
	{
		//facteur indiquant x pixel = 1 degre d'assiette
		
		int i,lineAx, lineAy, lineBx, lineBy, centreLineX,centreLineY;

		
		for(i=valMin; i<valMax + pasEnDegres;i+=pasEnDegres)
		{
			//coordonnees du centre de la ligne a-b
			centreLineX = coordPolX(centreX, valueSolZ, valueSolY + i * facteurAffichageAssietteSol);
			centreLineY = coordPolY(centreY, valueSolZ, valueSolY + i * facteurAffichageAssietteSol);
			
			lineAx = coordPolX(centreLineX, valueSolZ-90, largeurGraduations/2);
			lineAy = coordPolY(centreLineY, valueSolZ-90, largeurGraduations/2);
			
			lineBx = coordPolX(centreLineX, valueSolZ+90, largeurGraduations/2);
			lineBy = coordPolY(centreLineY, valueSolZ+90, largeurGraduations/2);
			
			if (i!=0)
			{
				gc.drawLine(lineAx, lineAy, lineBx, lineBy);
				
				if (afficheValeurs == true)
				{
					gc.setFont(valeursGraduationsFont);
					
					String valGrad = String.valueOf(Math.abs(i));
					
					gc.drawString(valGrad, lineAx-(valGrad.length()* valeursGraduationsFont.getSize()), lineAy);
					gc.drawString(valGrad, lineBx+(valGrad.length()/2* valeursGraduationsFont.getSize()), lineBy);
				}
			}
		}
	}
	
	/**
	 * Dessin des graduations representant l'angle en roulis
	 * @param gc
	 */
	private void dessineGraduationsRoulis(Graphics gc)
	{
		int i,lineAx, lineAy, lineBx, lineBy;
		
		for(i=-40;i <50;i+=10)
		{
			lineAx = coordPolX(centreX,i-90 , tailleInstrument/3);
			lineAy = coordPolY(centreY,i-90 , tailleInstrument/3);
			
			lineBx = coordPolX(centreX, i-90, tailleInstrument/2.6f);
			lineBy = coordPolY(centreY,i-90 , tailleInstrument/2.6f);
			
			gc.drawLine(lineAx, lineAy, lineBx, lineBy);
		}
	}
}
