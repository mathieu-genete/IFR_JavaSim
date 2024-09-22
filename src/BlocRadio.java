import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


/**
 * Cette classe permet de creer differents recepteurs radio sur le tableau de bord.
 * un recepteur VOR, ADF ou un recepteur DME
 * @author GENETE mathieu 2008
 *
 */
public class BlocRadio extends Canvas implements MouseListener, MouseMotionListener{

	public enum TypeRadio {recepteurVOR, recepteurADF, recepteurDME};
	
	private int tailleX,tailleY;
	private int frequence, stbyFrequence, addToFreq,tmpX;
	
	//variable contenant la distance dme
	private float distDME;
	
	//Determine le type du recepteur radio
	private TypeRadio typeRadio;
	
	//purement indicatif, permet d'afficher le numero de l'instrument
	private String instruNo;
	
	private Font affichageFont;
	
	protected PaintDoubleBuffering doubleBuffering;
	
	/**
	 * Constructeur de la classe BlocRadio
	 * @param taille
	 */
	public BlocRadio(int taille)
	{
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		doubleBuffering = new PaintDoubleBuffering();
		
		affichageFont= new Font("LUCIDA CONSOLE",Font.PLAIN,taille/16);
		
		this.tailleX = taille;
		this.tailleY = taille /4;
		addToFreq = 0;
		
		typeRadio = TypeRadio.recepteurVOR;
		
		frequence = 0;
		stbyFrequence = 0;
	
		distDME = 0;
		
		this.instruNo = "";
		
		this.setSize(tailleX, tailleY);
		
		addMouseListener(this);
		
		addMouseMotionListener(this);
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	/**
	 * Dessine l'instrument radio. on preferera la methode repaint()
	 */
	public void paint(Graphics gc)
	{
		String strFreq,strFreeStby,typeInstru;
		
		//Utilisation du double buffering pour eviter les clignotements lors de l'affichage
		Graphics tampon = doubleBuffering.setReelVersVirtuelGraphics(gc, tailleX, tailleY, this);
		tampon.setFont(affichageFont);
		tampon.clearRect(0,0,tailleX,tailleY);
		
		//dessin de l'arriere plan en rouge fonce
		tampon.setColor(new Color(10,0,0));
		tampon.fillRect(0, 0, tailleX, tailleY);
		
		//dessin des cadres blanc
		tampon.setColor(Color.white);
		tampon.drawRect(2, 2, tailleX-5, tailleY-5);
		tampon.drawLine(tailleX/2, 2, tailleX/2, tailleY-5);
		
		tampon.setColor(Color.red);
		
		//initialise les chaines de caractère
		strFreq ="";
		strFreeStby="";
		typeInstru="";
		
		//en fonction du type chaoisi, affiche les informations sur le module radio
		if(typeRadio != TypeRadio.recepteurDME)
		{
			if (typeRadio == TypeRadio.recepteurVOR)
			{
				strFreq = String.valueOf("ACT " + (float)this.frequence/100);			
				strFreeStby= String.valueOf("STBY " + (float)this.stbyFrequence/100);
				typeInstru="VOR";
			}
			
			if (typeRadio == TypeRadio.recepteurADF)
			{
				strFreq = String.valueOf("ACT " + this.frequence);			
				strFreeStby= String.valueOf("STBY " + this.stbyFrequence);
				typeInstru="ADF";
			}
			
			tampon.drawString(strFreq, tailleX/10, tailleY/2);
			tampon.drawString(strFreeStby, tailleX/2 + tailleX/15, tailleY/2);
		}
		else
		{
			strFreq = String.valueOf("ACT " + (float)this.frequence/100);
			tampon.drawString("DME " + distDME, tailleX/10, tailleY/2);
			tampon.drawString(strFreq, tailleX/2 + tailleX/15, tailleY/2);
			typeInstru="DME";
		}
		
		tampon.setColor(Color.white);
		tampon.drawString(typeInstru + " " + instruNo, 5, tailleY/2 + tailleY/3);
		//Dessine le vor a l'ecran
		doubleBuffering.dessineVirtuelVersReelGraphics();
	}
	

	/*
	 * gestion des evennements souris
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0) {
		//Si le bouton gauche est clique, incremente la frequence stand by
		if (arg0.getButton() == MouseEvent.BUTTON1)
		{
			if (arg0.getClickCount()>2)
			{
				this.augmenteStbyFreq(20);
			}
			else
			{
				this.augmenteStbyFreq(1);
			}
		}
			
//		Si le bouton droit est clique, decremente la frequence stand by
		if (arg0.getButton() == MouseEvent.BUTTON3)
		{
			if (arg0.getClickCount()>2)
			{
				this.diminueStbyFreq(20);
			}
			else
			{
				this.diminueStbyFreq(1);
			}
		}
		
		//Si le type instrument est DME, il n'y a pas de frequence stand by
		if (typeRadio == TypeRadio.recepteurDME)
		{
			frequence = stbyFrequence;
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
		tmpX = arg0.getX();
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		//permet de switcher entre la frequence stanby et la frequence utilise
		if ((Math.abs(tmpX - arg0.getX())>100) && (typeRadio != TypeRadio.recepteurDME))
			{
				this.switchStbyFreq();
				tmpX = arg0.getX();
			}
		
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Retourne la frequence sous forme d'entier
	 * @return
	 */
	public int getFrequence()
	{
		return frequence;
	}
	
	/**
	 * retourne le type radio selectionne
	 * @return
	 */
	public TypeRadio getTypeRadio()
	{
		return this.typeRadio;
	}
	
	/**
	 * definit la distance DME a afficher sur l'instrument DME
	 * @param distDME
	 */
	public void setDME(float distDME)
	{
		if (distDME != this.distDME)
		{
			this.distDME = (int)(distDME * 10)/10f;
			
			this.repaint();
		}

	}
	
	/**
	 * Permet de definir le type d'instrument. a aplacer apres avoir instancie l'objet
	 * @param typeRadio
	 * @param instruNo
	 */
	public void setTypeRadio(TypeRadio typeRadio, String instruNo)
	{
		
		this.typeRadio = typeRadio;
		this.instruNo = instruNo;
		
		if (typeRadio == TypeRadio.recepteurADF)
		{
			this.frequence = 190;
			this.stbyFrequence = 190;
		}
		else
		{
			this.frequence = 10800;
			this.stbyFrequence = 10800;
		}
		
	}
	
	/**
	 * Augmente la frequence stdby de la valeur indiquee
	 * @param valinc
	 */
	private void augmenteStbyFreq(int valinc)
	{
		if ((typeRadio== TypeRadio.recepteurVOR) || (typeRadio== TypeRadio.recepteurDME))
		{
			//la frequence des vors varie de 108 a 118
			addToFreq += valinc;
			
			if (addToFreq > 995) addToFreq = 0;
			if (addToFreq < 0) addToFreq = 995;
			
			stbyFrequence = 10800 + addToFreq;
		}
		
		if (typeRadio== TypeRadio.recepteurADF)
		{
			//la frequence de ndb varie de 190 a 1750
			addToFreq += valinc;
			
			if (addToFreq > 1560) addToFreq = 0;
			if (addToFreq < 0) addToFreq = 1560;
			
			stbyFrequence = 190 + addToFreq;
		}
		
		this.repaint();
	}
	
	/**
	 * Diminue la frequence stdby de la valeur indiquee
	 * @param valdec
	 */
	private void diminueStbyFreq(int valdec)
	{
		if ((typeRadio== TypeRadio.recepteurVOR) || (typeRadio== TypeRadio.recepteurDME))
		{
			//la frequence des vors varie de 108 a 118
			addToFreq -=valdec;
			
			if (addToFreq > 995) addToFreq = 0;
			if (addToFreq < 0) addToFreq = 995;
			
			stbyFrequence = 10800 + addToFreq;
		}
		
		if (typeRadio== TypeRadio.recepteurADF)
		{
			//la frequence de ndb varie de 190 a 1750
			addToFreq -= valdec;
			
			if (addToFreq > 1560) addToFreq = 0;
			if (addToFreq < 0) addToFreq = 1560;
			
			stbyFrequence = 190 + addToFreq;
		}
		
		this.repaint();
	}
	
	/**
	 * Permet de switcher les frequence entre standby et frequence utilisee
	 *
	 */
	private void switchStbyFreq()
	{
		int tmp;
		
		tmp = frequence;
		
		frequence = stbyFrequence;
		
		stbyFrequence = tmp;
		
		this.repaint();
	}
	
}
