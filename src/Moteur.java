/**
 * Cette classe decrit le fonctionnement de l'ensemble moteur helice de l'avion
 * @author GENETE MATHIEU 2008
 *
 */
public class Moteur {
	
	//Avion qui contient le moteur
	private Avion avion;
	
	//valeur max que peut prendre la manette des gazs
	private int valMaxGouvernes;
	
	//puissance max du moteur en watts
	private float puissanceMoteurWatts;
	
	//a X tours seconde  le moteur delivre la puissanceMoteurWatts
	private float moteurRps;
	
	//Regime maximum du moteur en RPM
	private float regimeMaxRPM;
	
	 // diametre de l'helice en metres
	private float diametreHelice;
	
	//coefficients de la courbe d'efficacite du moteur
	private float coefEfficaciteA;
	private float coefEfficaciteB;
	
	//permet d'affiner l'inertie du rendu des tours minute (rpm)
	//par defaut fixe a 20
	private float inertieMoteur;
	
	//variables relatives au moteur posManetteGazs varie de 0 a +valMaxGouvernes
	private int toursMinuteMoteur, posManetteGazs;
	
	/**
	 * Constructeur de la classe Moteur
	 * @param avion avion concerne
	 * @param puissanceMoteurCh puissance du moteur exprimee en chevaux
	 * @param moteurRps nombre de tours par seconde qui permet de delivrer la puissance donnee
	 * @param regimeMaxRPM regime maximum continu suporte par le moteur
	 * @param diametreHelice diemetre de l'helice en metres
	 * @param coefEfficaciteA coefficient d'efficacite a (voir documentation)
	 * @param coefEfficaciteB coefficient d'efficacite b (voir documentation)
	 * @param valMaxGouvernes valeur max que peux prendre la manette des gazs
	 */
	public Moteur(Avion avion, float puissanceMoteurCh,float moteurRps,float regimeMaxRPM,float diametreHelice,float coefEfficaciteA,float coefEfficaciteB,int valMaxGouvernes )
	{
		this.avion = avion;
		
		//converstion des chevaux en watts... 1cv = 746W
		this.puissanceMoteurWatts = puissanceMoteurCh * 746f;
		
		this.moteurRps = moteurRps;
		this.regimeMaxRPM = regimeMaxRPM;
		this.diametreHelice = diametreHelice;
		this.coefEfficaciteA = coefEfficaciteA;
		this.coefEfficaciteB = coefEfficaciteB;
		this.valMaxGouvernes = valMaxGouvernes;
		
		this.inertieMoteur = 20;
		
	}
	
	/**
	 * Met a jour les parametres moteurs.
	 *
	 */
	public void updateMoteur()
	{
		calculToursMinuteInertie();
	}

	
	/**
	 * Calcul du nombre de tours/minute (RPM) en fonction de la position de la manette des gazs
	 * a integrer dans la boucle principale, ajuster la valeur inertieMoteur en fonction de la
	 * duree de la boucle principale
	 */
	private void calculToursMinuteInertie()
	{
		int tmpRPM;
		
		tmpRPM = (int)((this.regimeMaxRPM/valMaxGouvernes) * this.posManetteGazs);
		
		//le nombre de RPM et incremente par la difference tmpRPM donnee par la fonction ci dessus et
		//this.toursMinuteMoteur pour donner un effet d'inertie au moteur.
		this.toursMinuteMoteur += (tmpRPM - this.toursMinuteMoteur)/inertieMoteur;

	}
	
	/**
	 * Calcul de la traction en Newton, en fonction du nombre de tours du moteur
	 * @return
	 */
	public float calculTractionN(float vitesse,float vitesseVerticale,float densiteAir)
	{
		float tmpT, tmpRPM, vtotal, omega, factor, advanceRatio;
		
		tmpRPM = this.toursMinuteMoteur;
		
		//vtotal correspond a la resultante de la vitesse et de la vitesse verticale
		vtotal = (float)Math.sqrt(Math.pow(vitesse , 2) + Math.pow(vitesseVerticale, 2));
		
	    //calcul le facteur de perte de puissance
		omega = densiteAir/1.225f;
		factor = (omega - 0.12f)/0.88f;

	    //calcul la traction
		advanceRatio = vtotal/(moteurRps*diametreHelice);
		
	    tmpT = (tmpRPM / (moteurRps*60f)) * factor*puissanceMoteurWatts*(coefEfficaciteA + coefEfficaciteB*advanceRatio*advanceRatio)/(moteurRps*diametreHelice); 

	    if (tmpT < 0) tmpT = 0;

		return tmpT;
	}

	//Methodes GET
	
	/**
	 * retourne la position de la manette
	 * @return
	 */
	public int getPosManetteGazs() {
		return posManetteGazs;
	}

	/**
	 * Retourne le nombre de tours minute du moteur
	 * @return
	 */
	public int getToursMinuteMoteur() {
		return toursMinuteMoteur;
	}

	
	//Methodes SET
	
	/**
	 * Cette fonction permet d'indiquer au moteur la position de la manette des gazs, passee en parametre
	 * entre 0 pour le minimum et 1000 pour le maximum
	 * @param posManette
	 */
	public void setPosManetteGazs(int posManette)
	{
		if (posManette > valMaxGouvernes) posManette = valMaxGouvernes;
		
		if (posManette < 0) posManette = 0;
		
		this.posManetteGazs = posManette;
		
	}
	
	/**
	 * definit le nombre de tours minute (RPM) du moteur
	 * @param toursMinuteMoteur
	 */
	public void setToursMinuteMoteur(int toursMinuteMoteur) {
		this.toursMinuteMoteur = toursMinuteMoteur;
	}

}
