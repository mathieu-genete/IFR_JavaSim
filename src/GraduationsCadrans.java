import java.awt.*;
import java.util.Vector;

/**
 * Cette classe permet de creer differents type de graduation en fonction des besoins
 * Une graduation posede un centre, un rayon, une valeur minimum et maximum, un interval angulaire d'action,
 * une echelle de graduation principale avec l'inscription des valeur et une echelle de graduation secondaire sans valeurs
 * 
 * @author GENETE MATHIEU 2008
 *
 */
public class GraduationsCadrans extends ObjetsCadrans {
	
	//Declaration des variables privees

	private float echelleGradPrinc;
	private float echelleGradSecon;
	
	//graduation statique
	private float angleDepartGradStat;
	private float angleArriveeGradStat;
	private int pasEnDegresGradStat;
	
	private Color couleurGradStat;
	
	private Color couleurFondCadranGradue;
	
	private boolean fondCadranVisible;
	private boolean afficheValeursAbsolu;
	private boolean afficheValeurEntiere;
	private boolean afficherPointsCardinaux;
	private boolean afficheMarqueursStatiques;
	
	private Vector ListeArcCouleur;
	
	/**
	 * Constructeur de la classe GraduationsCadrans
	 * @param rayon
	 * @param couleur
	 * @param valMin
	 * @param valMax
	 * @param valueAngle
	 * @param angleDepart
	 * @param echelleGradPrinc
	 * @param echelleGradSecon
	 */
	public GraduationsCadrans(int rayon,Color couleur,float valMin, float valMax, float valueAngle, float angleDepart,float echelleGradPrinc,float echelleGradSecon)
	{
		//Vecteur contenant les arcs de couleur a dessiner
		ListeArcCouleur = new Vector();
		
		//Le rayon ne peut etre nul ou negatif
		if (rayon<0 || rayon ==0) rayon = 1;
		
		//affecte aux variables de la class les valeurs du constructeur
		centreX = 0;
		centreY = 0;
		this.rayon = rayon;
		this.couleur = couleur;
		this.valMin = valMin;
		this.valMax = valMax;
		
		this.couleurFondCadranGradue = Color.black;
		
		this.fondCadranVisible = true;
		
		//une valeur d'angle ne peut etre >360
		//On utilise le modulo 361 pour avoir un domaine entre 0 et 300
		this.valueAngle = valueAngle % valModulo;
		this.angleDepart = (angleDepart - 90) % valModulo;
		
		this.echelleGradPrinc = echelleGradPrinc;
		this.echelleGradSecon = echelleGradSecon;

		
		//initialise les valeurs par defaut des marqueurs statiques
		this.angleDepartGradStat = 0;
		this.angleArriveeGradStat = 359;
		this.pasEnDegresGradStat=45;
		this.couleurGradStat = Color.orange;
		
		this.afficheValeursAbsolu = false;
		this.afficheValeurEntiere = true;
		this.afficherPointsCardinaux = false;
		this.afficheMarqueursStatiques = false;

	}
	
	
	//Methodes publiques

	/**
	 * Dessin des graduation dans le contexte graphique specifie en parametre
	 * @param gc
	 */
	public void dessineGraduations(Graphics gc)
	{
		//Declaration des variables
		String valeurAafficher;
		
		ArcCouleurCadrans tmpArc;
		
		float i,tmpAngle;
		
		int tmpRayon,correctionCoordonneesY,correctionCoordonneesX;
		
		int sizeCercle,j;
		
		//Sauvegarde la couleur utiliser par le context graphique
		Color ancienneCouleur;
		ancienneCouleur = gc.getColor();
		
		if (this.fondCadranVisible == true)
		{
			//Diametre du grand cercle au fond de l'instrument
			sizeCercle = rayon*2 + rayon/5;
			
			gc.setColor(this.couleurFondCadranGradue);
			gc.fillOval(this.centreX - sizeCercle/2,this.centreY - sizeCercle/2, sizeCercle,sizeCercle);

		}
		
		
		//verifier si il y a des arcs colore a dessiner
		if (ListeArcCouleur.size() > 0)
		{
			//dessine chaque arc
			for(j = 0; j < ListeArcCouleur.size();j++)
			{
				tmpArc = (ArcCouleurCadrans) ListeArcCouleur.elementAt(j);
				
				tmpArc.dessineArc(gc);
				
			}
		}
		
		//Dessin des graduations principales
		for(i=valMin; i<=valMax;i+=echelleGradPrinc)
		{		
			//Retourne l'angle pour une valeur donnee
			tmpAngle = CalculAngle(i);
			
			gc.setColor(this.couleur);

			gc.drawLine(coordPolX(this.centreX, tmpAngle , rayon),coordPolY(this.centreY, tmpAngle, rayon), coordPolX(this.centreX,tmpAngle, rayon-15),coordPolY(this.centreY, tmpAngle, rayon-15));
			
			//Affiche les valeur a "rayon / 1.3"
			tmpRayon = (int)(rayon / 1.4);
			
			//Affiche ou non les valeurs en valeurs absolues
			if (this.afficheValeursAbsolu == false)
			{
				valeurAafficher = String.valueOf(i);
			}
			else
			{
				valeurAafficher = String.valueOf(Math.abs(i));
			}
			
			//Affiche ou non uniquement la partie entiere des valeurs
			if (this.afficheValeurEntiere == true) 
			{
				valeurAafficher = valeurAafficher.substring(0, valeurAafficher.indexOf('.', 0));
			}
			
			//Affiche les points cardinaux aux valeurs 0, 9, 18, 27
			if (afficherPointsCardinaux == true)
			{
				valeurAafficher = valPointsCardinaux((int)i,valeurAafficher);
				
			}
			
			//correction a appliquer a l'affichage des valeur de graduation
			//calcule en fonction de la taille de la police et de la longueur de l'element a afficher
			correctionCoordonneesY =(gc.getFont().getSize()/4);
			correctionCoordonneesX = valeurAafficher.length()*gc.getFont().getSize()/4;
			
			gc.drawString(valeurAafficher,coordPolX(this.centreX,tmpAngle,tmpRayon )- correctionCoordonneesX,coordPolY(this.centreY, tmpAngle, tmpRayon)+ correctionCoordonneesY);
		
		}
		
		
		//Dessin des graduations secondaires
		for(i=valMin; i<valMax;i+=echelleGradSecon)
		{		
			tmpAngle = CalculAngle(i);
			gc.drawLine(coordPolX(this.centreX,  tmpAngle, rayon),coordPolY(this.centreY,  tmpAngle, rayon), coordPolX(this.centreX,tmpAngle, rayon-5),coordPolY(this.centreY, tmpAngle, rayon-5));
		}

		if (afficheMarqueursStatiques == true) dessineMarqueursStatiques(gc,this.angleDepartGradStat,this.angleArriveeGradStat,this.pasEnDegresGradStat,this.couleurGradStat);

		//restitue la couleur du context graphique
		gc.setColor(ancienneCouleur);
	}
	

	/**
	 * Permet d'ajouter des arcs de couleur aux graduations pour delimiter une zone
	 * @param instruBord
	 * @param angleDepart
	 * @param angleArrive
	 * @param rayon
	 * @param epaisseur
	 * @param couleurArc
	 */
	public void addArcCouleur(InstrumentDeBord instruBord, float angleDepart,float angleArrive,float rayon,float epaisseur,Color couleurArc)
	{
		ArcCouleurCadrans tmpArc;
		
		tmpArc = new ArcCouleurCadrans(instruBord, angleDepart, angleArrive, rayon, epaisseur, couleurArc,this.couleurFondCadranGradue);
		
		ListeArcCouleur.add(tmpArc);
	}
	
	/**
	 * Affiche les graduation statiques sur l'instrument, avec les valeurs entrees en parametres
	 * @param angleDepartGradStat
	 * @param angleArriveeGradStat
	 * @param pasEnDegresGradStat
	 * @param couleurGradStat
	 */
	public void AfficheMarqueursStatiques(float angleDepartGradStat, float angleArriveeGradStat, int pasEnDegresGradStat, Color couleurGradStat)
	{
		this.angleDepartGradStat = angleDepartGradStat % 360;
		this.angleArriveeGradStat = angleArriveeGradStat % 360;
		this.pasEnDegresGradStat = pasEnDegresGradStat % 360;
		this.couleurGradStat = couleurGradStat;
		
		this.afficheMarqueursStatiques = true;
		
	}

	/**
	 * Affiche les graduation statiques sur l'instrument
	 */
	public void AfficheMarqueursStatiques()
	{
		this.afficheMarqueursStatiques = true;
	}
	
	/**
	 * Masque les graduation statiques sur l'instrument
	 */
	public void MasqueMarqueursStatiques()
	{
		this.afficheMarqueursStatiques = false;
	}
	
	/**
	 *Affiche ou non les quatres points cardinaux sur une graduation.
	 *dont les valeurs sont comprises entre 0 et 36 uniquement. 0 = N
	 *9 = E  18 = S  270 = O
	 * @param isTrueOrFalse
	 */
	public void setAfficherPointsCardinaux(boolean isTrueOrFalse)
	{
		this.afficherPointsCardinaux = isTrueOrFalse;
	}
	

	/**
	 * Indique si les valeurs affichees avec les graduations doivent
	 * comporter ou non la partie decimal
	 * @param isTrueOrFalse
	 */
	public void setAfficheValeurEntiere(boolean isTrueOrFalse)
	{
		this.afficheValeurEntiere = isTrueOrFalse;
	}
	
	/**
	 *Indique si les valeurs affichees avec les graduations doivent etre
	 *en valeurs absolues
	 * @param isTrueOrFalse
	 */
	public void setAfficheValeursAbsolu(boolean isTrueOrFalse)
	{
		this.afficheValeursAbsolu = isTrueOrFalse;
	}
		
	
	/*
	 * METHODES PRIVEES
	 */

	/**
	 * Cette methode dessine des graduations statiques dans le context graphique gc
	 * @param gc
	 * @param angleDepartGradStat
	 * @param angleArriveeGradStat
	 * @param pasEnDegresGradStat
	 * @param couleurGradStat
	 */
	private void dessineMarqueursStatiques(Graphics gc, float angleDepartGradStat, float angleArriveeGradStat, int pasEnDegresGradStat, Color couleurGradStat)
	{
		float tmpAngle;
		
		gc.setColor(couleurGradStat);
		
		for(float i=angleDepartGradStat; i<=angleArriveeGradStat;i+=pasEnDegresGradStat)
		{
			tmpAngle = i;

			gc.drawLine(coordPolX(this.centreX, tmpAngle , rayon),coordPolY(this.centreY, tmpAngle, rayon), coordPolX(this.centreX,tmpAngle, rayon-20),coordPolY(this.centreY, tmpAngle, rayon-20));		}
	}
	
	
	/**
	 * Cette methode permet d'afficher les quatres points cardinaux sur une graduation.
	 * Elle se base sur la valeur affichee sur le cadran: 0 pour le Nord, 9 pour l'EST
	 * 18 pour le SUD et 270 pour l'OUEST.
	 * @param valPtCardinaux
	 * @param valeurAafficher
	 * @return
	 */
	private String valPointsCardinaux(int valPtCardinaux, String valeurAafficher)
	{
		String pointCardinaux;
		
		pointCardinaux=valeurAafficher;
		
		switch (valPtCardinaux)
		{
		case 0:
			pointCardinaux="N";
		break;
		
		case 9:
			pointCardinaux="E";
		break;
		
		case 18:
			pointCardinaux="S";

		break;
		
		case 27:
			pointCardinaux="O";
		break;
		}
		
		return pointCardinaux;
	}

	/**
	 * retourne la couleur de fond du cadran gradue
	 * @return
	 */
	public Color getCouleurFondCadranGradue() {
		return couleurFondCadranGradue;
	}
	
	/**
	 * indique si le fond du cadran est visible ou non
	 * @return
	 */
	public boolean isFondCadranVisible() {
		return fondCadranVisible;
	}

	/**
	 * definit la couleur de fond du cadran gradue
	 * @param couleurFondCadranGradue
	 */
	public void setCouleurFondCadranGradue(Color couleurFondCadranGradue) {
		this.couleurFondCadranGradue = couleurFondCadranGradue;
	}

	/**
	 * definit si le fond du cadran doit etre visible ou non
	 * @param fondCadranVisible
	 */
	public void setFondCadranVisible(boolean fondCadranVisible) {
		this.fondCadranVisible = fondCadranVisible;
	}
	
	
}
