import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Vector;



/**
 * Classe qui definit la carte de navigation contenant l'avion, le trace du chemin effectue par celui ci
 * et les differentes balises
 * @author mathieu
 *
 */
public class CarteNav extends Canvas implements MouseListener, MouseMotionListener{

	//Declarations
	
	private float tmpAlt,tmpCap;
	
	private int tmpClic;
	
	//On utilise "ObjetsCoordonneesCirculaires" juste pour les methodes de calcul de coordonnees circulaires
	private ObjetsCoordonneesCirculaires coordcirc;
	
	//Chaine de caracteres contenant des informations a afficher en haut de la carte
	private String messageInfo;
	
	private int sourisX,sourisY;
	
	//Police d'affichage pour les inscription de la carte
	private Font affichageFont;
	
	//double buffering : evite les scintillements sur l'image
	private PaintDoubleBuffering doubleBuffering;
	
	//largeur et hauteur de la zone de vol
	private int largeur, hauteur;
	
	private BalisesRadio [] tabBaliseRadio;

	private Avion avion;
	
	//variables de position temporaire, utilisee pour le dessin du chemin
	private int tmpPosX, tmpPosY;
	
	//utilise pour savoir si l'utilisateur a clique a proximite de l'avion
	private boolean clicProcheAvion;
	
	private boolean afficheRadials;
	
	private Color fondCarte, cheminAvion;
	
	//Vecteurs stockant les coordonnees de point du chemin a dessiner
	private Vector<Integer> ArrayPosX;
	private Vector<Integer> ArrayPosY;
	
	/**
	 * Constructeur de la classe CarteNav
	 * @param largeur
	 * @param hauteur
	 * @param tabBaliseRadio
	 * @param avion
	 */
	public CarteNav(int largeur, int hauteur, BalisesRadio [] tabBaliseRadio, Avion avion )
	{
		
		affichageFont= new Font("LUCIDA CONSOLE",Font.PLAIN,10);
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		doubleBuffering = new PaintDoubleBuffering();
		
		coordcirc = new ObjetsCoordonneesCirculaires();
		
		fondCarte = new Color(230,230,230);
		cheminAvion  = new Color(0,180,0);
		
		ArrayPosX = new Vector();
		ArrayPosY = new Vector();

		this.afficheRadials = false;
		
		this.messageInfo ="";
		
		this.tmpPosX = 0;
		this.tmpPosY = 0;
		
		this.largeur = largeur;
		this.hauteur = hauteur;
		
		this.tabBaliseRadio = tabBaliseRadio;
		
		this.avion = avion;
		
		this.clicProcheAvion = false;
		
		this.setSize(largeur, hauteur);
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	/**
	 * Dessin de la carte
	 */
	public void paint(Graphics gc)
	{
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, this.largeur, this.hauteur, this);
		tampon.setFont(affichageFont);
		tampon.clearRect(0,0,this.largeur, this.hauteur);
		
		//Le fond de la carte est blanc
		tampon.setColor(fondCarte);
		tampon.fillRect(0, 0, this.largeur, this.hauteur);
		tampon.setColor(Color.black);
		tampon.drawRect(0, 0, this.largeur-1, this.hauteur-1);
		
		//Dessine les balises radio
		for(int i =0; i < tabBaliseRadio.length;i++)
		{
			this.dessineIconeBalise(tampon, tabBaliseRadio[i]);
		}
		
		//dessine la maquette de l'avion
		dessineAvion(tampon,avion);
		
		tampon.drawString(this.messageInfo,10,10);
		
		repositionAvionSiSortZone();
		
		//Dessine la carte a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	
	/**
	 * fonction qui dessine l'avion dans le contexte graphique specifie
	 * @param gc
	 * @param avion
	 */
	private void dessineAvion(Graphics gc, Avion avion)
	{
		//Declarations
		int centreAvX,centreAvY,cap;
		Color ancienneCouleur;
		
		//sauvegarde de la couleur du context graphique
		ancienneCouleur = gc.getColor();
		
		//Calcul du cap pour l'affichage sur la carte
		cap = ((int)avion.getCap() - 90) % 360;
		
		//la position de l'avion est transferee dans les variables centreAvX et centreAvY
		centreAvX = (int)avion.getPosX();
		centreAvY = (int)avion.getPosY();
		
		//Ajoute un point au chemin seulement si l'avion a parcouru une distance > a 1 pixel
		//evite de stocker inutilement des points similaires
		if ( getDistanceA_B(tmpPosX, tmpPosY, (int)avion.getPosX(), 	(int)avion.getPosY()) > 1)
		{			
			ArrayPosX.add(centreAvX);
			ArrayPosY.add(centreAvY);
			
			tmpPosX = centreAvX;
			tmpPosY = centreAvY;
		}
		

		//Dessin de l'avion
		gc.setColor(Color.black);
		gc.drawLine(coordcirc.coordPolX(centreAvX, cap, -10), coordcirc.coordPolY(centreAvY, cap, -10), coordcirc.coordPolX(centreAvX, cap, 5), coordcirc.coordPolY(centreAvY, cap, 5));
		gc.drawLine(coordcirc.coordPolX(centreAvX, cap + 90, 5), coordcirc.coordPolY(centreAvY, cap+90, 5), coordcirc.coordPolX(centreAvX, cap-90, 5), coordcirc.coordPolY(centreAvY, cap-90, 5));

		//Affiche la vitesse en noeuds et l'altitude en pieds a cote de l'avion
		gc.setColor(Color.gray);

		int avionAltFt, avionVitesseKt;
		avionAltFt = (int)(avion.getAltitude()*3.28f);
		
		avionVitesseKt = (int)(avion.getVitesse()*1.94);
		gc.drawString("V:"+ String.valueOf(avionVitesseKt) + "kt  Alt:" + String.valueOf(avionAltFt) + "ft  cap:" + String.valueOf((int)avion.getCap()), centreAvX+8, centreAvY+8);

		//Dessine le chemin de l'avion
		if (ArrayPosX.size() > 2)
		{
			gc.setColor(cheminAvion);
			for(int i =0; i < ArrayPosX.size()-1;i++)
			{
				//Dessine que les segments < a 5 pixels de long.
				if (getDistanceA_B(ArrayPosX.elementAt(i), ArrayPosY.elementAt(i), ArrayPosX.elementAt(i+1), ArrayPosY.elementAt(i+1))<= 5)
				{
					gc.drawLine(ArrayPosX.elementAt(i), ArrayPosY.elementAt(i), ArrayPosX.elementAt(i+1), ArrayPosY.elementAt(i+1));
				}
			}
		}
		
		//restaure la couleur du contexte graphique
		gc.setColor(ancienneCouleur);
	}

	
	/**
	 * Fonction qui dessine les balises dans le contexte graphique specifie
	 * @param gc
	 * @param balise
	 */
	private void dessineIconeBalise(Graphics gc, BalisesRadio balise)
	{
		Color ancienneCouleur;
		String freqStr,ident;
		int taille = 10;
		int dX,dY, rayonRadial;
		
		ancienneCouleur = gc.getColor();
		
		//En fonction du type de la balise, la frequence n'a pas la meme unite et la balise a une couleur differente
		if (balise.getTypeBalise() == BalisesRadio.TypeBalise.NDB)
		{
			freqStr = String.valueOf(balise.getFrequence()) + " kHz";
			
			//Dessine deux cercles concentriques representant la NDB
			gc.setColor(Color.red);
			
			//Si la balise est active, alors l'icone est dessine en vert
			if (balise.getBaliseIsRadioTuned() == true) gc.setColor(Color.green);
			
			gc.drawOval(balise.getPositionX()-taille/2, balise.getPositionY()-taille/2, taille, taille);
			gc.drawOval(balise.getPositionX()-taille/3, balise.getPositionY()-taille/3, (int)(taille/1.5f), (int)(taille/1.5f));
		}
		else
		{
			
			if ((balise.getBaliseIsRadioTuned() == true) && (this.afficheRadials == true))
			{
				rayonRadial = (int)Math.sqrt(Math.pow(largeur, 2) + Math.pow(hauteur, 2));
				
				//Radial FROM - balise arriere				
				gc.setColor(Color.magenta);
				gc.drawLine(coordcirc.coordPolX(balise.getPositionX(), balise.getRadialSelectRecepteur() - 90,rayonRadial ), coordcirc.coordPolY(balise.getPositionY(), balise.getRadialSelectRecepteur() - 90, rayonRadial),balise.getPositionX(),balise.getPositionY());
				
				//Radial TO - vers la balise
				gc.setColor(Color.green);
				gc.drawLine(coordcirc.coordPolX(balise.getPositionX(), balise.getRadialSelectRecepteur() - 90,-rayonRadial ), coordcirc.coordPolY(balise.getPositionY(), balise.getRadialSelectRecepteur() - 90, -rayonRadial),balise.getPositionX(),balise.getPositionY());

			}
			
			freqStr = String.valueOf(balise.getFrequence()/100f) + " MHz";
			
			//Dessine un cercle encadre d'un carre representant le VOR
			gc.setColor(Color.blue);
			
			//Si la balise est active, alors l'icone est dessine en vert
			if (balise.getBaliseIsRadioTuned() == true) gc.setColor(Color.green);
			
			gc.drawRect(balise.getPositionX()-taille/2, balise.getPositionY()-taille/2, taille, taille);
			gc.drawOval(balise.getPositionX()-taille/3, balise.getPositionY()-taille/3, (int)(taille/1.5f), (int)(taille/1.5f));
			

			
		}
		

		
		//Affiche les informations relative a la balise. Si le vor est DME, affiche (DME) sur la carte
		gc.setColor(Color.black);
		
		ident = balise.getIdent();
		
		if (balise.getTypeBalise() == BalisesRadio.TypeBalise.VORDME) ident += " (DME)";
		
		gc.drawString(ident, balise.getPositionX() + taille/2 + 3, balise.getPositionY() + taille/2);
		
		//Calcul la position de la souris par rapport a la balise
		dX = Math.abs(sourisX - balise.getPositionX());
		dY = Math.abs(sourisY - balise.getPositionY());
		
		//Si la souris est proche de la balise, messageInfo contient les donnees relatives a la balise
		if (( dX < 5)&& (dY < 5))
		{
			messageInfo = "Info balise : " +  balise.getTypeBalise()+ " " + balise.getIdent() + " " + freqStr;
		}
		
		//restaure l'ancienne couleur du contexte graphique
		gc.setColor(ancienneCouleur);
	}
	
	/**
	 * Efface le chemin de l'avion
	 *
	 */
	private void effaceChemin()
	{
		ArrayPosX.clear();
		ArrayPosY.clear();
	}
	
	/**
	 * Calcul la distance en pixels entre un point A et un point B
	 * @param aX
	 * @param aY
	 * @param bX
	 * @param bY
	 * @return
	 */
	private float getDistanceA_B(int aX,int aY,int bX,int bY)
	{
		float tmp;
		int dX,dY;
		
		dX = Math.abs(aX - bX);
		dY = Math.abs(aY - bY);
		
		tmp = (float)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
		
		return tmp;
	}

	/**
	 * Si l'avion sort de la zone il est immediatement repositionne dans la zone
	 *
	 */
	private void repositionAvionSiSortZone()
	{
		if (avion.getPosX() > largeur) avion.setPosX(0);

		if (avion.getPosY() > hauteur) avion.setPosY(0);

		
		if (avion.getPosX() < 0) avion.setPosX(largeur);

		if (avion.getPosY() < 0) avion.setPosY(hauteur);

		
	}
	
	/*
	 * Gestion des evenements souris
	 * 
	 */
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		//Si double clique sur le bouton gauche de la souris, alors efface le chemin
		if ((arg0.getButton() == MouseEvent.BUTTON1) && (arg0.getClickCount() ==2))
		{
			effaceChemin();
		}


	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		tmpAlt = avion.getAltitude();
		tmpCap = avion.getCap();
		tmpClic = arg0.getX();
			
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		//Si la souris est proche de l'avion
		if (clicProcheAvion == true)
		{
						
			if (arg0.getModifiers() == 4)
			{
				//Clique droit, changement du cap de l'avion en deplacant la souris
				avion.setCap((tmpCap + (arg0.getX() - tmpClic)) % 360);
			}
		

			if (arg0.getModifiers() == 16)
			{
				//Clique gauche, change la position de l'avion
				avion.setPosX(arg0.getX());
				avion.setPosY(arg0.getY());
				
				effaceChemin();
			}
			
			if (arg0.getModifiers() == 20)
			{
				//Clic gauche + droit, change l'altitude de l'avion
				avion.setAltitude(tmpAlt + (arg0.getX() - tmpClic));
				avion.setAltitudePA(avion.getAltitude());
			}

		}

	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		sourisX = arg0.getX();
		sourisY = arg0.getY();
		
		//Si la souris se trouve pres de l'avion, clicProcheAvion = true
		clicProcheAvion = ((Math.abs(arg0.getX() - avion.getPosX())< 10) && (Math.abs(arg0.getY() - avion.getPosY())< 10));
		
		
		messageInfo ="";
		
		//Si la souris est proche de l'avion, change l'icone de la souris en main
		if (clicProcheAvion ==true)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			messageInfo = "BG: deplace l'avion -- BD: change le cap -- B G+D : change l'altitude";
		}else
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	/**
	 * Retourne si les radials des vors sont affiche sur la carte
	 * @return
	 */
	public boolean isAfficheRadials() {
		return afficheRadials;
	}

	/**
	 * Definit si les radials des vors doivent etre affiche sur la carte
	 * @param afficheRadials
	 */
	public void setAfficheRadials(boolean afficheRadials) {
		this.afficheRadials = afficheRadials;
	}
}
