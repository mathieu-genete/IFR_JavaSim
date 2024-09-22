
/**
 * Cette classe permet de creer un objet capable d'effectuer des calculs pour la radionavigation
 * @author GENETE mathieu 2008
 *
 */
public class CalculsRadioNavigation {
	
	//Declarations
	private Avion avion;
	
	private BalisesRadio [] tabBalisesRadio;
	
	private BalisesRadio baliseActive;
	
	private int frequence;
	
	private float avionX,avionY;
	
	private float capA_B;
	
	private boolean baliseEstActive;
	
	private boolean isDMEOnly;
	
	/**
	 * Constructeur de la classe CalculsRadioNavigation
	 * @param avion
	 * @param tabBalisesRadio
	 * @param isDMEOnly seulement pour les instruments n'affichant rien d'autre que la DME
	 */
	public CalculsRadioNavigation(Avion avion, BalisesRadio[] tabBalisesRadio, boolean isDMEOnly)
	{
		this.avion = avion;
		
		this.tabBalisesRadio = tabBalisesRadio;
		
		this.frequence = 0;
		
		this.isDMEOnly = isDMEOnly;
		
		this.baliseEstActive = false;
	}
	

	/**
	 * Calcul le cap vers la balise active (indiquer la frequence avec la methode setFrequence)
	 * @return
	 */
	public float getCapVersBalise()
	{
		updateCalculsRadioNavigation();
		
		return (this.capA_B + 360) % 360;
	}
	
	/**
	 * Calcul la difference entre le cap vers la balise active et un cap entre en parametre
	 * @param cap
	 * @return
	 */
	public float getDifferenceCapVersBalise_CapParam(float cap)
	{
		cap = cap % 360f;
		
		updateCalculsRadioNavigation();
		
		if (baliseActive != null)
		{
			return ((this.capA_B + 360) - cap)%360;
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Calcul la deviation pour le vor entree en parametre, sur la balise active
	 * @param vor
	 */
	public void calculVors(Vor vor)
	{
		if (isBaliseEstActive() == true)
		{
			float tmpval;

			//Met dans la variable setRadialSelectRecepteur, la radiale selecte sur l'instrument vor
			this.baliseActive.setRadialSelectRecepteur(vor.getRouteValue());
			
			tmpval = getDifferenceCapVersBalise_CapParam(vor.getRouteValue());
								
			if ((tmpval < 90) || (tmpval > 270))
			{
				vor.setFlagVor(Vor.Flag.TO);
				vor.setDeviationAvecInertie((getDifferenceCapVersBalise_CapParam(vor.getRouteValue()) + 90)%360 - 90);
			}
			else
			{
				vor.setFlagVor(Vor.Flag.FROM);
				vor.setDeviationAvecInertie(270 - (getDifferenceCapVersBalise_CapParam(vor.getRouteValue()) + 90)%360);
			}
		}
		else
		{
			//Si pas de balise active, le flag est mis sur off, ainsi que la deviation
			vor.setFlagVor(Vor.Flag.OFF);
			vor.setDeviationAvecInertie(0);
			
		}
	}
	
	
	/**
	 * Met a jour les calculs pour un usage interne a la classe
	 *
	 */
	private void updateCalculsRadioNavigation()
	{
		
		avionX = avion.getPosX();
		avionY = avion.getPosY();
		
		chercheBaliseFrequence();
		
		if (baliseActive != null)
		{
			this.capA_B =  calculCapPtA_PtB(avionX, avionY, baliseActive.getPositionX(), baliseActive.getPositionY());
		}
	}
	
	/**
	 * Definit la balise active en fonction de la frequence
	 *
	 */
	private void chercheBaliseFrequence()
	{
		int i;
		
		baliseActive = null;
		baliseEstActive = false;
		
		for(i=0; i < tabBalisesRadio.length;i++)
		{
			if (tabBalisesRadio[i].getFrequence() == this.frequence)
			{
				baliseActive = tabBalisesRadio[i];
				if (this.isDMEOnly == false ) tabBalisesRadio[i].setBaliseIsRadioTuned(true);
			}
			
		}
		
		if (baliseActive != null) baliseEstActive = true;
	}
	
	/**
	 * Calcul le cap d'un point A vers un point B (voir la doc pour plus de details)
	 * @param aX
	 * @param aY
	 * @param bX
	 * @param bY
	 * @return
	 */
	private float calculCapPtA_PtB(float aX, float aY, float bX, float bY)
	{
		float resultat,dX,dY,r,a1,a2,a3,a4;
		int z;
		
		dX = bX - aX;
		dY = bY - aY;
		
		if ((dX < 0) || (dY < 0))
		{
			z=-1;
		}else
		{
			z=1;
		}
		
		r = z * (float)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2));
		
		a1 = (float)(Math.asin((aY - bY)/r) * (180 / Math.PI) + 90) % 360f;
		
		a2 = (float)(Math.acos((aX - bX)/r) * (180 / Math.PI) + 90) % 360f;
		
		a3 = 360-a1;
		
		a4 = 360-a2;
		
		resultat = 0;
		
		if ((dX >=0) && (dY > 0)) resultat = a4;
		if ((dX <0) && (dY >= 0)) resultat = a2;
		if ((dX >=0) && (dY <= 0)) resultat = a1;
		if ((dX <0) && (dY < 0)) resultat = a3;
		return resultat;
	}
	
	/**
	 * Calcul la distance DME en miles nautiques vers la balise active. La distance represente la diagonale partant de l'avion,
	 * jusqu'a la balise au sol.
	 * @return
	 */
	public float calculDME()
	{
		float resultat, altiBalise, altiAvion, dAlt, tmp;
		int aX,aY,  bX,  bY;
		int dX,dY;
		
		resultat = 0;
		
		//Existe t-il une balise active
		if(baliseActive !=null)
		{
			aX = (int)avion.getPosX();
			aY = (int)avion.getPosY();
			
			bX = baliseActive.getPositionX();
			bY = baliseActive.getPositionY();
			
			//Calcul de la difference d'altitude entre la balise et l'avion
			altiBalise = baliseActive.getAltitude();
			altiAvion = avion.getAltitude();
			
			//dAlt est transforme en miles nautiques
			dAlt = (altiAvion - altiBalise) * 0.0005399568f;
			
			dX = (int)Math.abs(bX - aX);
			dY = (int)Math.abs(bY - aY);
			
			//La balise active est elle equipee d'un DME ?
			if (baliseActive.getTypeBalise() == BalisesRadio.TypeBalise.VORDME)
			{
				//Bon vieu theoreme de Phytagore
				tmp = (float)Math.sqrt(Math.pow(dX, 2) + Math.pow(dY, 2)) / 10f;
				resultat = (float)Math.sqrt(Math.pow(tmp, 2)+Math.pow(dAlt, 2));
			}
		}
		return resultat;
	}

	/**
	 * retourne la frequence active
	 * @return
	 */
	public int getFrequence() {
		return frequence;
	}

	/**
	 * definit la frequence active
	 * @param frequence
	 */
	public void setFrequence(int frequence) {
		this.frequence = frequence;
		
		updateCalculsRadioNavigation();
	}

	/**
	 * Indique si il y a une balise active
	 * @return
	 */
	public boolean isBaliseEstActive() {
		return baliseEstActive;
	}


	/**
	 * Revoie la balise active
	 * @return BaliseRadio
	 */
	public BalisesRadio getBaliseActive() {
		return baliseActive;
	}

	/**
	 * Indique si la classe concerne un instrument uniquement DME
	 * @return
	 */
	public boolean getIsDMEOnly() {
		return isDMEOnly;
	}


}
