import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.*;


/**
 * Classe qui contient la fonction main. Programme principal
 * @author   MATHIEU
 */
public class IFR_JavaSim {

	//Declaration	
	private static final String  NoVersion="1.1.2";
	private static String titreFenetre = "IFR JAVA SIM     Version: " + NoVersion + "        (C) 2008 GENETE Mathieu";
	
	//tableau qui contient la liste des fichiers .ini du repertoire de l'application.
	private static String [] tabListeFichiersIni;
	//nom du fichier a ouvrir
	private static String nomFichierIni;
	
	//Declare les formulaires
	private static Frame formTableauBord;
	private static Frame formCarteNav;
	
	//Taille des instruments
	private static final int tailleInstru = 190;
	
	//boucle qui va se repeter toute les x millisecondes pour la simulation
	private static BouclePrincipale boucleprincipale;
	
	//Multiplicateur pour la vitesse de simulation
	public static int vitesseSimulation = 1;
	
	//Declaration des instruments du tableau de bord
	public static Altimetre altimetre;
	public static Anemometre anemometre;
	public static Variometre variometre;
	public static CompteTour comptetour;
	public static IndicateurVirage indicVirage;
	public static HorizonArtificiel horizon;
	public static Vor vor1;
	public static Vor vor2;
	public static Adf adf;
	public static ManetteControle manettegaz;
	public static ManetteControle manetteVolets;
	public static BlocRadio radiovor1;
	public static BlocRadio radiovor2;
	public static BlocRadio radioadf;
	public static BlocRadio radiodme;
	public static CompasGyrodirectionnel compas;
	public static BoutonAvion btPiloteAuto;
	public static Chronometre chronometre;
	
	//Tableau de balise radio
	public static BalisesRadio baliseRadio[];
	
	public static CalculsRadioNavigation calculsRadioADF;
	public static CalculsRadioNavigation calculsRadioVOR1;
	public static CalculsRadioNavigation calculsRadioVOR2;
	public static CalculsRadioNavigation calculsRadioDME;

	
	//Avion en question dans le logiciel
	public static Avion avion;
	
	//Carte de navigation
	public static CarteNav carteNav;
	

	

	public static void main(String args[]) throws IOException
	{		
		//Definition des couleurs du tableau de bord
		Color couleurTabBord = new Color(142,122,105);
		
		//Couleur de fond de l'horizon artificiel
		Color couleurFondHorizon = new Color(97,82,71);
		
		//Instancie et initialise le formulaire contenant le tableau de bord
		formTableauBord = new Frame();
		formTableauBord.setTitle(titreFenetre);
		
		formCarteNav  = new Frame();
			
		
		//Ouvre le fichier d'initialisation choisi et charge les donnees et initialise l'avion, les balises et la carte
		
		tabListeFichiersIni = listeFichiersIni();
		
		//Si il n'y a pas de fichiers ini, alors affiche un message d'erreur et quitte le logiciel
		if (tabListeFichiersIni.length == 0) 
			{
				JOptionPane.showMessageDialog(null, "Aucun fichier INI trouve...", "Erreur", JOptionPane.WARNING_MESSAGE);
				 System.exit(0);
			}
		nomFichierIni = (String) JOptionPane.showInputDialog(null,"Choisissez la zone d'exercice","IFR_JavaSim V" + NoVersion, JOptionPane.PLAIN_MESSAGE,null,tabListeFichiersIni,tabListeFichiersIni[0]);
		
		//Si l'utilisateur a clique sur cancel, on quitte le programme.
		if (nomFichierIni == null) System.exit(0);
		
		OuvrirFichierInitialiser(nomFichierIni);
		
		
		
		
		
		//met a jour le titre de la fenetre de navigation
		formCarteNav.setTitle("IFR JavaSim - CARTE DE NAVIGATION -- [ " + nomFichierIni + " ]");
		
		
		//Cree la boucle principale
		boucleprincipale = new BouclePrincipale();
	
		//cree un nouveau panel pour la carte de navigation
		Panel cartePanel = new Panel(new BorderLayout());
		//Ajoute carteNav au Panel
		cartePanel.add(carteNav,BorderLayout.CENTER);
		
		//cree les differents panel qui vont contenir les instruments
		Panel mainPanel = new Panel(new BorderLayout());
		Panel mainPanelLeft = new Panel(new GridLayout(3,4));
		Panel mainPanelRight = new Panel();
		
		mainPanelLeft.setBackground(couleurTabBord);
		mainPanel.setBackground(couleurTabBord);
		mainPanelRight.setBackground(Color.darkGray);
		
		mainPanel.add(mainPanelLeft, BorderLayout.CENTER);
		mainPanel.add(mainPanelRight, BorderLayout.EAST);
		
		//instancie les instruments
		altimetre = new Altimetre(tailleInstru,couleurTabBord);
		anemometre = new Anemometre(tailleInstru,couleurTabBord,200,0,127.5f,65,187.5f,187,240,240,242);
		variometre = new Variometre(tailleInstru,couleurTabBord,2000);
		compas = new CompasGyrodirectionnel(tailleInstru,couleurTabBord);
		horizon = new HorizonArtificiel(tailleInstru,couleurFondHorizon);
		indicVirage = new IndicateurVirage(tailleInstru,couleurTabBord);
		comptetour = new CompteTour(tailleInstru,couleurTabBord,3000,10,65,65,67);
		
		manettegaz = new ManetteControle(tailleInstru,couleurTabBord,Color.red,Color.black,"GAZS");
		
		manetteVolets = new ManetteControle(tailleInstru,Color.darkGray,Color.gray,Color.lightGray,"VOLETS");
		manetteVolets.setAffichage("- 40", "- 20", "-  0");
		
		btPiloteAuto = new BoutonAvion(tailleInstru/3,"ALT AUTO");
		
		chronometre = new Chronometre(tailleInstru,couleurTabBord);
		
		//Cree les objets permettant les calculs pour la radionavigation
		calculsRadioADF = new CalculsRadioNavigation(avion, baliseRadio,false);
		calculsRadioVOR1 = new CalculsRadioNavigation(avion, baliseRadio,false);
		calculsRadioVOR2 = new CalculsRadioNavigation(avion, baliseRadio,false);
		calculsRadioDME = new CalculsRadioNavigation(avion, baliseRadio,true);
		
		vor1 = new Vor(tailleInstru,couleurTabBord);
		vor2 = new Vor(tailleInstru,couleurTabBord);
		adf = new Adf(tailleInstru,couleurTabBord);
		
		radiovor1 = new BlocRadio(tailleInstru);
		radiovor2 = new BlocRadio(tailleInstru);
		radioadf = new BlocRadio(tailleInstru);
		radiodme = new BlocRadio(tailleInstru);

		
		//Configure les instruments radio
		radiovor1.setTypeRadio(BlocRadio.TypeRadio.recepteurVOR, "1");
		radiovor2.setTypeRadio(BlocRadio.TypeRadio.recepteurVOR, "2");
		radioadf.setTypeRadio(BlocRadio.TypeRadio.recepteurADF, "1");
		radiodme.setTypeRadio(BlocRadio.TypeRadio.recepteurDME,"1");
		
		
		//
		//Mise en place du tableau de bord
		//
		
		//1ere ligne
		mainPanelLeft.add(anemometre);
		mainPanelLeft.add(horizon);
		mainPanelLeft.add(altimetre);
		mainPanelLeft.add(vor1);
		
		//2eme ligne
		mainPanelLeft.add(indicVirage);
		mainPanelLeft.add(compas);
		mainPanelLeft.add(variometre);
		mainPanelLeft.add(vor2);
		
		//3eme ligne
		mainPanelLeft.add(chronometre);
		mainPanelLeft.add(comptetour);
		mainPanelLeft.add(manettegaz);
		mainPanelLeft.add(adf);
		

		//Cree une "boite verticale" pour placer les instruments radio
		Box boxVert = Box.createVerticalBox();
		
		mainPanelRight.add(boxVert);
		boxVert.add(radiovor1);
		boxVert.add(Box.createVerticalStrut(10));
		boxVert.add(radiovor2);
		boxVert.add(Box.createVerticalStrut(10));
		boxVert.add(radioadf);
		boxVert.add(Box.createVerticalStrut(10));
		boxVert.add(radiodme);
		
		boxVert.add(Box.createVerticalStrut(50));
		boxVert.add(btPiloteAuto);
		boxVert.add(Box.createVerticalStrut(25));
		boxVert.add(manetteVolets);
		
		
		//Ajoute le paneau principale a la fenetre tableau de bord
		formTableauBord.add(mainPanel);
	
		//Ajout le panneau de la carte sur la fenetre formCarteNav
		formCarteNav.add(cartePanel);
		
		if (avion.getVitesseVp() > 0)
		{
			//fixe la valeur de la manette des gazs pour un regime d'environ 2400 RPMs
			manettegaz.setValManette(900);
			//fixe le nombre de RPMs de depart
			avion.moteur.setToursMinuteMoteur(2400);
			//Le pilote automatique est active
			avion.setPiloteAutoAltiActive(true);
		}
		
		//Initialise le bouton du pilote auto
		btPiloteAuto.setBoutonActive(avion.isPiloteAutoAltiActive());
		
		//Demarre la boucle principale du simulateur
		boucleprincipale.start();
		
		
		//Gestion du focus de formTableauBord
		formTableauBord.addFocusListener(new FocusListener(){

			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void focusLost(FocusEvent arg0) {
				
				//Si le formulaire contenant la carte est actif, formTableauBord ne recherche pas a reprendre le focus
				if (formCarteNav.requestFocusInWindow() == false) formTableauBord.requestFocus();	
			}
			
		});
		
		//Gestion des evenements clavier
		formTableauBord.addKeyListener(new KeyListener(){

			public void keyPressed(KeyEvent arg0) {
				
				//tout est dans la fonction pour alleger le code en visibilite
				gestionKeyPressed(arg0);
				
			}

			public void keyReleased(KeyEvent arg0) {
				
			}

			public void keyTyped(KeyEvent arg0) {
				
			}
			
		});
		
		
		formTableauBord.addWindowListener(new WindowListener(){

			public void windowOpened(WindowEvent arg0) {
				
			}

			public void windowClosing(WindowEvent arg0) {
				//Quand on quitte le programme, arret de la boucle principale
				questionQuitter();
				
			}

			public void windowClosed(WindowEvent arg0) {
				
			}

			public void windowIconified(WindowEvent arg0) {
				
			}

			public void windowDeiconified(WindowEvent arg0) {
				
			}

			public void windowActivated(WindowEvent arg0) {

			}

			public void windowDeactivated(WindowEvent arg0) {
		
			}

		});

		
		
		//Ajuste automatiquement la taille du formulaire
		formCarteNav.pack();
		//affiche le formulaire contenant la carte
		formCarteNav.show();
		
		//Ajuste automatiquement la taille du formulaire
		formTableauBord.pack();
		
		//le formulaite du tableau de bord n'est pas redimensionnable
		formTableauBord.setResizable(false);
		
		//affiche le formulaire du tableau de bord
		formTableauBord.show();
	
		
	}
	
	/**
	 * Pour eviter de surcharger le code de la fonction main, la gestion des touches est transfere
	 * dans cette methode
	 * @param arg0
	 */
	private static void gestionKeyPressed(KeyEvent arg0)
	{
		//Touche s du clavier : augmente la vitesse de simulation
		if (arg0.getKeyCode()==KeyEvent.VK_S) vitesseSimulation = (vitesseSimulation% 10) + 1;
		
		//Si la vitesse de simulation est differente de 1 alors affiche la vitesse dans la barre de titre
		if (vitesseSimulation != 1)
		{
			formTableauBord.setTitle(titreFenetre + "         VITESSE DE SIMULATION X " + String.valueOf(vitesseSimulation));
		}
		else
		{
			reinitialiseTitreFenetre();
		}
		
		//Touche r : rends visible ou non les radials sur la carte
		if (arg0.getKeyCode()==KeyEvent.VK_R) carteNav.setAfficheRadials(!carteNav.isAfficheRadials());
		
		//Touche a et q : gestion de la manette des gazs au clavier
		if (arg0.getKeyCode()==KeyEvent.VK_A) manettegaz.setValManette(manettegaz.getValManette() + 2);
		if (arg0.getKeyCode()==KeyEvent.VK_Q) manettegaz.setValManette(manettegaz.getValManette() - 2);
		
		//Touche p : active ou desactive le pilote automatique
		if (arg0.getKeyCode()==KeyEvent.VK_P) btPiloteAuto.setBoutonActive(!btPiloteAuto.isBoutonActive());
		
		//Definit la position des volets de sustentation
		if (arg0.getKeyCode() == KeyEvent.VK_G) manetteVolets.setValManette(manetteVolets.getValManette() + 10);
		if (arg0.getKeyCode() == KeyEvent.VK_F) manetteVolets.setValManette(manetteVolets.getValManette() - 10);
			
		if (arg0.getKeyCode() == KeyEvent.VK_B) avion.setFreinsActive(true);			
		
		if (arg0.getKeyCode() == KeyEvent.VK_W) avion.atmosphere.setDirectionVent(avion.atmosphere.getDirectionVent() + 1);
                if (arg0.getKeyCode() == KeyEvent.VK_X) avion.atmosphere.setDirectionVent(avion.atmosphere.getDirectionVent() - 1);
                
                if (arg0.getKeyCode() == KeyEvent.VK_C) avion.atmosphere.setVitesseVent_ms(avion.atmosphere.getVitesseVent_ms() + 0.5144f);
                if (arg0.getKeyCode() == KeyEvent.VK_V) avion.atmosphere.setVitesseVent_ms(avion.atmosphere.getVitesseVent_ms() - 0.5144f);
                
		//Touche haut,bas,droite,gauche : gestion de la direction verticale et horizontale de l'avion
		if (arg0.getKeyCode()== KeyEvent.VK_UP) avion.stab.decGouvProf(1);
		if (arg0.getKeyCode()== KeyEvent.VK_DOWN) avion.stab.incGouvProf(1);
		
		if (arg0.getKeyCode()== KeyEvent.VK_LEFT) avion.aile.incAileronG_decAileronD(1);
		if (arg0.getKeyCode()== KeyEvent.VK_RIGHT) avion.aile.incAileronD_decAileronG(1);
	}
	
	/**
	 * Liste tous les fichiers ini du repertoire de l'application
	 * @return retourne un tableau contenant les noms de fichier
	 */
	private static String[] listeFichiersIni()
	{
		String tabFile[],returnTab[];
		int i;
		Vector<String> tabFinal;
		
		tabFinal = new Vector();
		int nbrIniFile = 0;
            
		//recupere le repertoire ou se trouve l'application
		File repertoire = new File(System.getProperty("user.dir"));
		
		//tabFile contient tous les noms de fichier du repertoire
		tabFile = repertoire.list();
		
		//tabFinal contient que les fichiers ini
		for (i = 0; i < tabFile.length;i++)
		{
			if(tabFile[i].toUpperCase().contains(".INI"))
			{
				tabFinal.add(tabFile[i]);
			}
		}
		
		//Converti le vecteur en tableau
		returnTab = new String[tabFinal.size()];
		
		for (i = 0; i < tabFinal.size();i++)
		{
			returnTab[i] = new String();
			returnTab[i] = tabFinal.get(i);
		}
		
		return returnTab;
	}
	
	/**
	 * Methode qui permet de charger le fichier de configuration et d'initialiser l'avion, les balises et la carte
	 * @param nomFichierIni
	 * @throws IOException
	 */
	private static void OuvrirFichierInitialiser(String nomFichierIni)
	{
		//Declarations
		String chaine;
		
		int largeur, hauteur, nbrBalises;
		
		int i, indexDebut;
		
		//Try catch pour gestion des erreur lecture
		try {	
			//Buffer pour lire le fichier de configuration
			BufferedReader fR;
			
			//Ouverture du fichier
	
				fR = new BufferedReader(new FileReader(new File(nomFichierIni)));
			
			//Try catch pour la gestion des erreurs de format de fichier	
			try
			{
				//Recupere la largeur de la zone de vol
				largeur = Integer.parseInt(fR.readLine());
				
				//Recupere la hauteur de la zone de vol
				hauteur = Integer.parseInt(fR.readLine());
				
				//Recupere le nombre de balises
				nbrBalises  = Integer.parseInt(fR.readLine());
				
				//Instancie le tableau avec le nombre de balises;
				baliseRadio = new BalisesRadio[nbrBalises];
				
				//Initialisation des balises
				for(i = 0; i < nbrBalises;i++)
				{
					//Declarations
					BalisesRadio.TypeBalise typeBalise;
					String ident;
					int posX, posY, altitude, frequence,indexBalise;
					
					//la ligne est stockee dans chaine
					chaine = fR.readLine();
					
					//Se place au 1er caractere de la chaine
					indexDebut=0;
					
					//Recupere le type de la balise sour forme d'un index 0 pour VOR, 1 pour VORDME et 2 pour NDB
					indexBalise = Integer.parseInt(chaine.substring(indexDebut, chaine.indexOf('\t')));
					indexDebut = chaine.indexOf('\t') + 1;
					
					//Recupere l'indentifiant de la balise
					ident = chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut));
					indexDebut = chaine.indexOf('\t',indexDebut) + 1;
					
					//Recupere la position X de la balise
					posX = Integer.parseInt(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
					indexDebut = chaine.indexOf('\t',indexDebut) + 1;
					
					//Recupere la position Y de la balise
					posY = Integer.parseInt(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
					indexDebut = chaine.indexOf('\t',indexDebut) + 1;
					
					//Recupere l'altitude de la balise en m
					altitude = Integer.parseInt(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
					indexDebut = chaine.indexOf('\t',indexDebut) + 1;
					
					//Recupere la frequence de la balise
					frequence = Integer.parseInt(chaine.substring(indexDebut,chaine.length()));
					
					//Par defaut, la balise est un VOR
					typeBalise = BalisesRadio.TypeBalise.VOR;
					
					//En fonction de l'indexBalise, le type de la balise est determine
					switch(indexBalise)
					{
					case 0:
						typeBalise = BalisesRadio.TypeBalise.VOR;
						break;
					case 1:
						typeBalise = BalisesRadio.TypeBalise.VORDME;
						break;
					case 2:
						typeBalise = BalisesRadio.TypeBalise.NDB;
						break;
					}
					
					//Initialise avec les parametres
					baliseRadio[i] = new BalisesRadio(typeBalise, ident, posX, posY, altitude, frequence);
				}
				
				
				
				//Initialisation de l'avion
				
				//Declarations
				int posX,posY;
				float altitude, vitesse,cap, dirVent, vitVent;
				
				chaine = fR.readLine();
		
				indexDebut=0;
				//Position X de depart
				posX = Integer.parseInt(chaine.substring(indexDebut, chaine.indexOf('\t')));
				indexDebut = chaine.indexOf('\t') + 1;
				
				//Position Y de depart
				posY = Integer.parseInt(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
				indexDebut = chaine.indexOf('\t',indexDebut) + 1;
				
				//Altitude de depart converti en m
				altitude = 0.3048f * Float.parseFloat(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
				indexDebut = chaine.indexOf('\t',indexDebut) + 1;
				
				//vitesse de depart converti en m/s
				vitesse = 0.51f * Float.parseFloat(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
				indexDebut = chaine.indexOf('\t',indexDebut) + 1;
				
                                //cap de depart
				cap = Integer.parseInt(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
				indexDebut = chaine.indexOf('\t',indexDebut) + 1;
                                
                                //direction du vent
                                dirVent = Float.parseFloat(chaine.substring(indexDebut,chaine.indexOf('\t',indexDebut)));
				indexDebut = chaine.indexOf('\t',indexDebut) + 1;
				
                                //vitesse du vent
				vitVent = Float.parseFloat(chaine.substring(indexDebut,chaine.length()));
				
				//Initialise l'avion avec les parametres
				avion = new Avion(posX,posY,100,200,altitude, vitesse,cap,false);
				
                                avion.atmosphere.setDirectionVent(dirVent);
                                
                                avion.atmosphere.setVitesseVent_ms(vitVent * 0.5144f);
                                
				carteNav = new CarteNav(largeur,hauteur,baliseRadio,avion);
		
				formCarteNav.setSize(largeur+20, hauteur+50);
				
			} catch (StringIndexOutOfBoundsException e)
			{
		
				JOptionPane.showMessageDialog(null, "ERREUR : format du fichier " + nomFichierIni + " incorrect...","ERREUR", JOptionPane.WARNING_MESSAGE);
			} catch (NumberFormatException e )
			{
				JOptionPane.showMessageDialog(null, "ERREUR : format du fichier " + nomFichierIni + " incorrect...","ERREUR", JOptionPane.WARNING_MESSAGE);

			}
			
			//Fermeture du fichier
			fR.close();
		
		//Gestion des exceptions
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//retourne le message d'erreur dans un messageBox
			JOptionPane.showMessageDialog(null, "ERREUR : " + e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//retourne le message d'erreur dans un messageBox
			JOptionPane.showMessageDialog(null, "ERREUR : " + e.toString());
		}

	}
	
	/**
	 * Pose la question avant de quitter le logiciel...
	 *
	 */
	private static void questionQuitter()
	{
		int rep;
		
		rep = JOptionPane.showConfirmDialog(null, "Voulez vous quitter IFR JavaSim ?", "Quitter...", JOptionPane.YES_NO_OPTION);
	
		if (rep == 0)
		{
			boucleprincipale.stop();
			
			//sortie du programme
			System.exit(0);
		}
		
	}
	
	/**
	 * remet le titre d'origine a la fenetre formTableauBord
	 */
	public static void reinitialiseTitreFenetre()
	{
		formTableauBord.setTitle(titreFenetre);
	}

	
}
