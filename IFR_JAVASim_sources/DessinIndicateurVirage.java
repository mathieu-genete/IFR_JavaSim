import java.awt.*;

/**
 * Dessine une maquette d'avion avec graduation au centre du contexte graphique specifie dans le constructeur
 * @author GENETE MATHIEU 2008
 *
 */
public class DessinIndicateurVirage extends ObjetsCoordonneesCirculaires {

	private int tailleInstrument;
	private Polygon polygonMaquetteAvion;
	
	/**
	 * Constructeur de la classe DessinIndicateurVirage.
	 * @param tailleInstrument correspond à la taille (longueur = largeur) du canvas recevant le dessin
	 */
	public DessinIndicateurVirage(int tailleInstrument)
	{
		this.tailleInstrument = tailleInstrument;
		
		centreX = (int)(tailleInstrument/2);
		centreY = (int)(tailleInstrument/2);
		polygonMaquetteAvion = new Polygon();
		
	}
	
	/**
	 * Dessine la maquette de l'avion a l'angle specifie, au centre du contexte graphique gc. Dessine également les 4 graduations
	 * de l'instrument.
	 * @param gc
	 * @param angleVirage
	 */
	public void DessineMaquette(Graphics gc, float angleVirage)
	{
		//Declaration des variables
		int dimCercle;
		
		//Coordonnees des 4 coins du rectangle a-b-c-d representant la maquette
		int aX,aY,bX,bY,cX,cY,dX,dY;
		
		int tmpX,tmpY;
		
		//variables relatives au dessin des 4 graduations.
		int lineAx,lineAy,lineBx,lineBy;
		int sizeCercle,rayon;
		
		//facteurs relatif a la maquette
		int facteurEpaisseur = (tailleInstrument/70);
		float facteurLongueur = tailleInstrument*0.30f;
		
		Color ancienneCouleur;
		
		//sauvegarde de la couleur du contexte graphique
		ancienneCouleur  = gc.getColor();
		
		//calcul de la taille du cercle noir au fond de l'instrument
		rayon = (int)(tailleInstrument / 2.4);
		sizeCercle = rayon*2 + rayon/5;
		
		
		gc.setColor(Color.white);

		//Dessin des 4 graduations. Les valeurs angulaires sont
		//stockées dans un tableau d'entiers.
		int tmpAngle[]={150 , 30 , 0 , 180};
		
		for(int i=0; i<4;i++)
		{
			lineAx = coordPolX(centreX, tmpAngle[i], sizeCercle/2);
			lineAy = coordPolY(centreY, tmpAngle[i], sizeCercle/2);
			
			lineBx = coordPolX(centreX, tmpAngle[i], sizeCercle/2.5f);
			lineBy = coordPolY(centreY, tmpAngle[i], sizeCercle/2.5f);
			
			gc.drawLine(lineAx, lineAy, lineBx, lineBy);
		}
		
		//Dessin de la maquette de l'avion en fonction de l'angle
		dimCercle = (int)(tailleInstrument/10);
		gc.fillOval(centreX -(dimCercle / 2) , centreY -(dimCercle / 2), dimCercle, dimCercle);

		tmpX = coordPolX(centreX, angleVirage-90, - facteurEpaisseur);
		tmpY = coordPolY(centreY, angleVirage-90, -facteurEpaisseur);
		
		aX = coordPolX(tmpX ,angleVirage, -facteurLongueur);
		aY = coordPolY(tmpY , angleVirage, -facteurLongueur);
		
		bX = coordPolX(tmpX , angleVirage, facteurLongueur);
		bY = coordPolY(tmpY ,angleVirage, facteurLongueur);
		
		tmpX = coordPolX(centreX, angleVirage+90, - facteurEpaisseur);
		tmpY = coordPolY(centreY, angleVirage+90, - facteurEpaisseur);
		
		cX = coordPolX(tmpX ,angleVirage, facteurLongueur);
		cY = coordPolY(tmpY , angleVirage, facteurLongueur);
		
		dX = coordPolX(tmpX , angleVirage, -facteurLongueur);
		dY = coordPolY(tmpY ,angleVirage, -facteurLongueur);
		
		polygonMaquetteAvion.reset();
		polygonMaquetteAvion.addPoint(aX, aY);
		polygonMaquetteAvion.addPoint(bX, bY);
		polygonMaquetteAvion.addPoint(cX, cY);
		polygonMaquetteAvion.addPoint(dX, dY);
	
		gc.fillPolygon(polygonMaquetteAvion);
		
		
		
		//restaure la couleur du contexte graphique
		gc.setColor(ancienneCouleur);
	}
}
