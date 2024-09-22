
/**
 * Cette classe decrit les parametres de l'avion sinsi que sont fonctionnement aerodynamique et mecanique
 * @author GENETE mathieu 2008
 *
 */
public class Avion {
	
	//atmosphere ou circule l'avion
	public Atmosphere atmosphere;
	
	//groupe motopropulseur de l'avion
	public Moteur moteur;
		
	//Aile de l'avion
	public Aile aile;
	
	//Stabilisateur (gouverne de profondeur)
	public Stab stab;
	
	//Trainee parasite due au fuselage et autre parties de l'avion
	private final float cXp = 0.030f;
	
	//Indique si les freins des roues sont actifs
	private boolean freinsActive;
	
	//indique si l'avion est au sol
	private boolean avionAuSol;
	
	//valeur max que peuvent prendre toutes les gouvernes (ailerons, gouverne de profondeur, manette des gazs)
	private final int valMaxGouvernes = 1000;
	
	
	
	private final float masseAVide = 736;
	
	private float masseCarburantKg;
	
	private float massePassagerBagagesKg;
	
	//masse de l'avion en kg
	private  float masseKg;
	
	/* variables ralatives a l'attitude et la position de l'avion:
	 * assiette en degres
	 * roulis en degres
	 * tauxVirage en degres par seconde
	 * vitesseVerticale en metres par seconde
	 * altitude en metres
	 * altitudePA en metres
	 * vitesse en metres par seconde
	 * posX en pixels (0.1 nautique)
	 * posY en pixels (0.1 nautique)
	 * cap en degres;
	 */
	private float assiette, roulis,tauxVirage ,vitesseVerticale, altitude, altitudePA ,vitesseTmp, vitesseGS,vitesseVp ,posX,posY,cap;


	//indique si le pilote auto pour l'altitude actuel est active
	private boolean piloteAutoAltiActive;
	
	//indique si l'avion s'est ecrase
	private boolean avionEcrase;
	
	//altitude du terrain sous l'avion en metres
	private float altitudeSol;
	
	//acceleration de l'apesenteur en m/s^2
	private final float g = 9.81f;

	
	
	/**
	 * Constructeur de la classe Avion, initialise l'avion avec les valeurs entrees
	 * @param posX
	 * @param posY
	 * @param massePassagerBagagesKg
	 * @param masseCarburantKg
	 * @param altitude
	 * @param vitesse
	 * @param cap
	 * @param piloteAutoAlt
	 */
	public Avion(float posX,float posY,float massePassagerBagagesKg, float masseCarburantKg, float altitude, float vitesse,float cap, boolean piloteAutoAlt)
	{
				
		atmosphere = new Atmosphere();
		
		/*puissance max du moteur 160ch		
		 * a 40 tours seconde (2400 rpm) le moteur delivre 160ch		
		 * Regime maximum continu du moteur en RPM = 2700
		 * diametre de l'helice en metres 1.905m		
		 * coefficients de la courbe d'efficacite du moteur a=1.83 b=-1.32
		 */
		moteur = new Moteur(this,160f,40.0f,2700,1.905f,1.83f,-1.32f,this.valMaxGouvernes);
		
		aile = new Aile(this,16.3f,10.9f,0.77f,0.0889f,-0.1f,0.178f,3.2f,16.0f,this.valMaxGouvernes);

		//a modifier lors de l'implementation du calcul du moment de tangage
		stab = new Stab(this,2.0f,3.45f,0.77f,0.0889f,-0.1f,0.178f,3.2f,16.0f,this.valMaxGouvernes);
		
		this.posX = posX;
		this.posY = posY;
		
		this.masseCarburantKg = masseCarburantKg;
		
		this.massePassagerBagagesKg = massePassagerBagagesKg;
		
		this.masseKg = massePassagerBagagesKg + masseAVide;
		
		this.altitude = altitude;
		
                this.vitesseTmp = vitesse;
                this.vitesseVp = vitesse;
                this.vitesseGS = vitesse;
                
		this.cap = cap;
		
		moteur.setPosManetteGazs(0);
		
		this.altitudePA = altitude;
		this.piloteAutoAltiActive = piloteAutoAlt;
		
		this.altitudeSol = 0;
		
		this.avionEcrase = false;
	}
	
	private void updateMasseAvion()
	{
		this.masseKg = this.massePassagerBagagesKg + this.masseAVide + this.masseCarburantKg;
	}
	
	/**
	 * Methode qui permet de mettre a jour les donnees de l'avion. Cette methode doit etre incluse dans la boucle
	 * principale du programme et executee a interval regulier. Cet interval (en millisecondes) doit etre passe 
	 * en parametre 
	 * @param dT
	 */
	public void updateAvionData(int dT)
	{
		/*Toutes les accelerations ont pour unite une vitesse (m/s) par seconde
		/*La boucle principale du programme se repete a intervel regulier inferieur a la seconde
		/*les acceleration sont divise par le facteurTemps pour corresprondres.
		*/
		float facteurTemps = (1000 / dT);
                
                float angleVent;
		
		//met a jour les parametres moteur
		moteur.updateMoteur();
		
		this.updateMasseAvion();
		
		this.calculAsiette();
		
		//Ajoute l'acceleration de la portance a la vitesse verticale
		this.vitesseVerticale += this.calculAccelerationPortanceMsecSec() / facteurTemps;
		//Retire l'acceleration de l'apesanteur
		this.vitesseVerticale -= this.calculAccelerationGraviteMsecSec() / facteurTemps;
		
		//Evite le depassement de capacite des variables 600 m/s semble une limite raisonnable
		if (this.vitesseVerticale < -600) this.vitesseVerticale = -600;
		if (this.vitesseVerticale > 600) this.vitesseVerticale = 600;
		       
                
		//Ajoute l'acceleration de la traction a la vitesse
		this.vitesseTmp += this.calculAccelerationTractionMSecSec() / facteurTemps;
		//retire l'acceleration de la trainee
		this.vitesseTmp -= this.calculAccelerationTraineeMSecSec() / facteurTemps;
              
		
		//Evite que la vitesse ne depasse 250 m/s
		if (this.vitesseTmp > 250) this.vitesseTmp = 250;
		if (this.vitesseTmp < 0) this.vitesseTmp = 0;
		
                angleVent = this.cap - this.atmosphere.getDirectionVent();
                
                this.vitesseGS = this.vitesseTmp - (float)(this.atmosphere.getVitesseVent_ms() * Math.cos(angleVent * (Math.PI)/180));
                
                if (this.vitesseGS < 0) this.vitesseGS =0;
                
                this.vitesseVp = this.vitesseGS + (float)(this.atmosphere.getVitesseVent_ms() * Math.cos(angleVent * (Math.PI)/180));
                      
		this.altitude += (this.vitesseVerticale)/ facteurTemps;
		
		
		//le cap est calcule en fonction du taux du virage en degres / secondes
		this.cap = this.cap + calculTauxVirageDegresParSec() / facteurTemps;
		this.cap   = (this.cap + 360)%360;

		
		this.piloteAutoAltitude();
		
		//Calcul de la position de l'avion sur le plan horizontal (posX et posY)
		calculPosAvionPixelsParSec(facteurTemps);
		
		//Si l'altitude est inferieure a 1 alors l'avion tente d'atterrir
		if ((this.altitude <= altitudeSol+1))
		{
			//Si l'assiette et le roulis de l'avion sont egal a +-10 degres et si la vitesse verticale n'est pas en dessous de -5 m/s
			//l'avion atterrit en toute securite
			if ((Math.abs(this.roulis) < 10) && (Math.abs(this.assiette) < 10) && (this.vitesseVerticale > -5))
			{
				this.avionEcrase = false;
				if (this.altitude < altitudeSol) this.altitude = altitudeSol;
				if (this.vitesseVerticale < 0) this.vitesseVerticale = 0;
				if (this.assiette < 0) this.assiette = 0;
			}
			else
			{	
				//Si l'avion s'ecrase, repositionne celui au sol.
				
				//flag indiquant que l'avion s'est ecrase est mis a true
				this.avionEcrase = true;
				
				this.moteur.setToursMinuteMoteur(0);
				
				this.altitude = 0;
				this.vitesseTmp = 0;
				this.roulis = 0;
				this.vitesseVerticale =0;
				this.aile.setPosAileronD(0);
				this.aile.setPosAileronG(0);
				this.stab.setPosGouverneProf(0);
			}
		}
		else
		{
			this.avionEcrase = false;
		}
		
		
		//L'avion est il au sol ou non
		//Important, ce test doit forcement etre place apres celui-ci dessus pour fonctionner
		if (this.altitude == altitudeSol)
		{
			this.avionAuSol = true;
		}
		else
		{
			this.avionAuSol = false;
		}
		
		
	}

	
	/**
	 * Cette fonction permet d'appliquer ou non le pilote auto pour l'altitude en fonction de la valeur
	 *de la variable piloteAutoAltiActive.
	 */
	private void piloteAutoAltitude()
	{
		if (this.piloteAutoAltiActive == true)
		{
			int tmp,maxTmp;
			
			//a ameliorer...
			tmp = ((int)((2000 - (this.vitesseVerticale*60f*3.281f))/800 *(this.altitudePA - this.altitude)*3.2808f));
			
			//Le PA ne depasse pas une assiette de + ou - 15 degres
			maxTmp = (int)((this.valMaxGouvernes * 15f)/90f);
			
			if (tmp > maxTmp) tmp = maxTmp;
			if (tmp < -maxTmp) tmp = -maxTmp;
			
			this.stab.setPosGouverneProf(tmp);
			
			this.calculAsiette();
			
			//Si la difference entre l'altitude avion et l'altitude cible est > 500 ft alor le PA se deconnecte.
			if (Math.abs((this.altitudePA - this.altitude)) > 500) this.piloteAutoAltiActive =false;
		}

	}
	
	
	
	/*
	 * Calculs relatifs aux axes de roulis et tanguage simplifie
	 */
	
	
	/**
	 * calcul de l'angle de roulis en degre en fonction de la position des ailerons de l'aile
	 * @return
	 */
	private float calculRoulis()
	{
		float tmpInc;
		
		tmpInc = 180 * (((float)aile.getPosAileronG() - (float)aile.getPosAileronD())/((float)valMaxGouvernes*2));
		
		this.roulis = tmpInc;
		
		return tmpInc;
	}
	
	/**
	 * calcul du taux du virage qui depends de l'angle du roulis et de la vitesse de l'avion.
	 * Un virage standard correspond a un rotation de 360 degres en 2 minutes soit 3 degres / secondes
	 * @return
	 */
	private float calculTauxVirageDegresParSec()
	{
		float vKt;
		float tmp = 0;
		
		vKt = this.vitesseTmp * 1.9438445f;
		
		//Attention ici a la division par 0
		if (this.vitesseTmp != 0)
		{
			tmp = -calculRoulis() * 3 / (vKt * 0.15f);
		}
		
		this.tauxVirage = tmp;
		
		return tmp;

	}
	
	/**
	 * calcul de l'assiette de l'avion en degres en fonction de la position de la gouverne de profondeur
	 * @return
	 */
	private float calculAsiette()
	{
		float tmpAsiette;
		
		tmpAsiette = 90f * ((float)this.stab.getPosGouverneProf() / (float)valMaxGouvernes);
		
		this.assiette = tmpAsiette;
		
		return tmpAsiette;
	}
	
	
	/*
	 * Calculs relatifs au cap et la position de l'avion
	 */
	
	/**
	 * Calcul la position X et Y de l'avion en fonction de la vitesse horizontale et du cap de celui-ci
	 * Dans ce calcul, 1 pixel correspond a 0.1 nautique
	 * @param facteurTemps
	 */
	private void calculPosAvionPixelsParSec(float facteurTemps)
	{
		float vr,vv,cap, capVent, vKt;
		
		cap =(this.cap - 90f) % 360;
                
		capVent =(this.atmosphere.getDirectionVent() - 90f ) % 360;
                
		vKt = this.vitesseTmp * 1.9438445f;
		
		//dans la foamule, 10 correspond au nombre de pixels pour 1 nautique
		vr = (vKt *(10f/3600f)) / facteurTemps;
                
		vv = -(this.atmosphere.getVitesseVent_ms()* 1.943844f *(10f/3600f)) / facteurTemps;
                
		this.posX = this.posX + (float)(vr * Math.cos(cap * (Math.PI / 180)));
		
		this.posY = this.posY + (float)(vr * Math.sin(cap * (Math.PI / 180)));
	
                this.posX = this.posX + (float)(vv * Math.cos(capVent * (Math.PI / 180)));
		
		this.posY = this.posY + (float)(vv * Math.sin(capVent * (Math.PI / 180)));
		
	}
	
	
	/*
	 * Calculs relatifs a la portance
	 */
	
	
	/**
	 * Calcul de la portance resultante en Newtons, qui tiens compte de l'inclinaison en roulis
	 * @return
	 */
	private float portanceN()
	{
		float tmpRzprime,Rz;
		
		Rz = aile.getPortanceN(this.atmosphere.getDensiteAir(this.altitude));
		
		tmpRzprime = (float)(Rz * Math.cos(this.roulis * (Math.PI/180)) * Math.cos(this.assiette * (Math.PI/180)));
		
		return tmpRzprime;
	}
	
	/**
	 * Calcul de l'acceleration verticale en m/sec/sec.
	 * @return
	 */
	private float calculAccelerationPortanceMsecSec()
	{
		float tmpAcc;
		
		tmpAcc = (this.portanceN()/this.masseKg);
		
		return tmpAcc;
	}
	
	
	/*
	 * Calculs relatifs au poids de l'avion
	 */
	
	/**
	 * Acceleration de l'apesanteur en m/sec/sec
	 * @return
	 */
	private float calculAccelerationGraviteMsecSec()
	{
		float tmpAcc;
		
		tmpAcc = this.g;
		
		return tmpAcc;
	}
	
	
	/*
	 * calculs relatifs a la trainee
	 */
	
	/**
	 * Calcul de la trainee en Newtons
	 * @return
	 */
	public float traineeN()
	{
		float tmpRx,vMs, cX;
		
		cX = aile.getCx(this.assiette) + this.cXp;
		
		
		if (this.avionAuSol == true) 
		{
			//Trainee due au roulage au sol
			cX += 0.05f;
		
			//Augmente la trainee si le frein est active
			if (this.freinsActive == true) 
			{
				cX += 30f/this.vitesseTmp;
				this.freinsActive = false;
			}
		}
		
		vMs = this.getVitesseVp();
		
		tmpRx = (float)( cX * this.atmosphere.getDensiteAir(this.altitude) * aile.getSurfacePortanteM2() * (Math.pow(vMs,2)/2f));
		
		return tmpRx;
	}
	
	
	/**
	 * Calcul de l'acceleration de la trainee en m/sec/sec
	 * @return
	 */
	private float calculAccelerationTraineeMSecSec()
	{
		float tmpAcc;
		
		tmpAcc = (float)(traineeN() / this.masseKg);
			
		return tmpAcc;
	}
	
	
	/*
	 * calculs relatifs a la traction
	 */

	
	/**
	 * Calcul de l'aceleration de la traction en m/sec/sec
	 * @return
	 */
	private float calculAccelerationTractionMSecSec()
	{
		float tmpAcc, tractionN;
		
		tractionN = moteur.calculTractionN(this.vitesseTmp, this.vitesseVerticale, this.atmosphere.getDensiteAir(this.altitude));
		
		tmpAcc = (float)(((tractionN - (this.portanceN() * Math.sin(Math.abs(this.assiette) * (Math.PI/180)))) / this.masseKg));
	
		
		return tmpAcc;
	}
	
	
	//Methodes GET
	
	/**
	 * retourne l'altitude de l'avion en m
	 * @return
	 */
	public float getAltitude() {
		return altitude;
	}

	/**
	 * retourne le cap de l'avion en degres
	 * @return
	 */
	public float getCap() {
		return cap;
	}

	/**
	 * retourne la valeur d'angle en degre du roulis
	 * @return
	 */
	public float getRoulis() {
		return roulis;
	}

	/**
	 * retourne le taux du virage en degres par seconde
	 * @return
	 */
	public float getTauxVirage() {
		return tauxVirage;
	}


        /**
         * retourne la vitesse sol de l'avion en m/s
         * @return
         */
        public float getVitesseGS() {
        return vitesseGS;
        }
        
        /**
         * retourne la vitesse indiquée de l'avion
         * @return
         */
        public float getVitesseVp()
        {
        return vitesseVp;
        }
        
	/**
	 * retourne la vitesse verticale de l'avion en m/s
	 * @return
	 */
	public float getVitesseVerticale() {
		return vitesseVerticale;
	}

	/**
	 * Retourne l'altitude selectionnee au pilote automatique (m)
	 * @return
	 */
	public float getAltitudePA() {
		return altitudePA;
	}

	/**
	 * retourne l'angle d'assiette en degres
	 * @return
	 */
	public float getAssiette() {
		return assiette;
	}

	/**
	 * retourne la position X de l'avion en pixels (1 pixel = 0,1 nautique)
	 * @return
	 */
	public float getPosX() {
		return posX;
	}

	/**
	 * retourne la position Y de l'avion en pixels (1 pixel = 0,1 nautique)
	 * @return
	 */
	public float getPosY() {
		return posY;
	}



	/**
	 * retourne si le pilote automatique est active ou non
	 * @return
	 */
	public boolean isPiloteAutoAltiActive() {
		return piloteAutoAltiActive;
	}
	
	/**
	 * retourne true si l'avion s'est ecrase
	 * @return
	 */
	public boolean isAvionEcrase() {
		return avionEcrase;
	}
	
	/**
	 * indique si les freins des roues sont active
	 * @return
	 */
	public boolean isFreinsActive() {
		return freinsActive;
	}
	
	/**
	 * Indique si l'avion est au sol ou non
	 * @return
	 */
	public boolean isAvionAuSol() {
		return avionAuSol;
	}
	
	
	
	//Methodes SET
	

	/**
	 * Active ou non le pilote automatique (PA) pour l'altitude. si active alors l'altitude entree pour le PA
	 * est l'altitude courante de l'avion
	 * @param piloteAutoAltiActive
	 */
	public void setPiloteAutoAltiActive(boolean piloteAutoAltiActive) {
		this.piloteAutoAltiActive = piloteAutoAltiActive;
		this.altitudePA = this.altitude;
	}

	/**
	 * Definit la position X en pixels de l'avion (1 pixel = 0,1 nautique)
	 * @param posX
	 */
	public void setPosX(float posX) {
		this.posX = posX;
	}

	/**
	 * Definit la position Y en pixels de l'avion (1 pixel = 0,1 nautique)
	 * @param posY
	 */
	public void setPosY(float posY) {
		this.posY = posY;
	}

	/**
	 * Definit le cap de l'avion en degres
	 * @param cap
	 */
	public void setCap(float cap) {
		this.cap = cap;
	}

	/**
	 * Definit l'altitude de l'avion en m
	 * @param altitude
	 */
	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}

	/**
	 * Redefinit l'altitude selectionne au pilote automatique (m)
	 * @param altitudePA
	 */
	public void setAltitudePA(float altitudePA) {
		this.altitudePA = altitudePA;
	}


	/**
	 * definit la vitesse de l'avion en noeuds (m/s)
	 * @param vitesse
	 */
	public void setVitesse(float vitesse) {
		this.vitesseTmp = vitesse;
                this.vitesseVp = vitesse;
                this.vitesseGS = vitesse;
	}

	/**
	 * definit la vitesse verticale en pieds par minute (m/s)
	 * @param vitesseVerticale
	 */
	public void setVitesseVerticale(float vitesseVerticale) {
		this.vitesseVerticale = vitesseVerticale;
	}

	/**
	 * Definit l'altitude du terrain sous l'avion en m
	 * @param altitudeSol
	 */
	public void setAltitudeSol(float altitudeSol) {
		this.altitudeSol = altitudeSol;
	}


	/**
	 * active ou non les freins des roues du train d'aterrissage
	 * @param freinsActive
	 */
	public void setFreinsActive(boolean freinsActive) {
		this.freinsActive = freinsActive;
	}

	
	
}
