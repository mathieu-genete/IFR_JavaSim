/**
 * Cette classe decrit une aile d'avion
 * @author GENETE MATHIEU 2008
 *
 */
public class Aile extends SurfacePortante{
	
	//surface d'un aileron en m2
	private float surfaceAileronM2;
	
	//surface d'un volet en m2
	private float surfaceVoletM2;
	
	private float posVoletsDegres;
	
	private float voletCzEffet;
	
	private final float voletCzMax = 0.3f;
	
	private int posAileronG,posAileronD;
	
	/**
	 * Constructeur de la classe Aile
	 * @param avion
	 * @param surfaceAileM2
	 * @param largeurAile
	 * @param effAvion
	 * @param cZPente0
	 * @param cZPente1
	 * @param cZ0
	 * @param cZ1
	 * @param alphaCZMax
	 * @param valMaxGouvernes
	 */
	public Aile(Avion avion, float surfaceAileM2,float largeurAile, float effAvion,float cZPente0,float cZPente1,float cZ0,float cZ1,float alphaCZMax, int valMaxGouvernes)
	{
		super(avion, surfaceAileM2, largeurAile, effAvion, cZPente0, cZPente1, cZ0, cZ1, alphaCZMax, valMaxGouvernes);
	
		this.posAileronD = 0;
		this.posAileronG = 0;
		this.posVoletsDegres = 0;
		this.voletCzEffet = 0;
	}
	
	
	/**
	 *  Portance de l'aile (partie gauche + partie droite) en Newtons
	 * @param densiteAir
	 * @return
	 */
	public float getPortanceN(float densiteAir)
	{
		float Rz,Cz;
		
		Cz = this.getCz(avion.getAssiette());
		
		Cz += voletCzEffet;
				
		Rz = portanceAileGauche(Cz, densiteAir) + portanceAileDroite(Cz, densiteAir);
		
		return Rz;
	}
	
	/**
	 * Calcul la portance de l'aile gauche
	 * @param Cz
	 * @param densiteAir
	 * @return
	 */
	protected float portanceAileGauche(float Cz,float densiteAir)
	{
		float RzAileG;
		
		RzAileG = calculPortance(densiteAir, Cz,this.surfacePortanteM2/2);
		
		return RzAileG;
	}
	
	/**
	 * Calcul la postance de l'aile droite
	 * @param Cz
	 * @param densiteAir
	 * @return
	 */
	protected float portanceAileDroite(float Cz,float densiteAir)
	{
		float RzAileD;
		
		RzAileD = calculPortance(densiteAir, Cz,this.surfacePortanteM2/2);
		
		return RzAileD;
	}
	
	
	/**
	 * Incremente la position de l'aileron gauche de la valeur passee en parametre et 
	 * decremente l'aileron droit de cette meme valeur. Utilise pour un virage a gauche
	 * @param stepVal
	 */
	public void incAileronG_decAileronD(int stepVal)
	{
		this.posAileronG = this.posAileronG +  stepVal;
		this.posAileronD = this.posAileronD -  stepVal;
		
		if (this.posAileronG > valMaxGouvernes) this.posAileronG = valMaxGouvernes;
		if (this.posAileronD < -valMaxGouvernes) this.posAileronD = -valMaxGouvernes;
	}
	
	/**
	 *Incremente la position de l'aileron droit de la valeur passee en parametre et 
	 *decremente l'aileron gauche de cette meme valeur. Utilise pour un virage a droite
	 * @param stepVal
	 */
	public void incAileronD_decAileronG(int stepVal)
	{
		this.posAileronG = this.posAileronG -  stepVal;
		this.posAileronD = this.posAileronD +  stepVal;
		
		if (this.posAileronD > valMaxGouvernes) this.posAileronD = valMaxGouvernes;
		if (this.posAileronG < -valMaxGouvernes) this.posAileronG = -valMaxGouvernes;
	}

	/**
	 * retourne la position de l'aileron gauche
	 * @return
	 */
	public int getPosAileronG() {
		return posAileronG;
	}

	/**
	 * retourne la position de l'aileron droit
	 * @return
	 */
	public int getPosAileronD() {
		return posAileronD;
	}
	
	/**
	 * retourne la position des volets de sustentation
	 * @return
	 */
	public float getPosVoletsDegres() {
		return posVoletsDegres;
	}
	
	
	/**
	 * Definit la position des volets de sustentation en degres
	 * 0 = volets rentre
	 * 40 = volets complement deployes
	 * @param valeur
	 */
	public void setPosVoletsDegres(float valeur)
	{
		if (valeur > 40) valeur = 40;
		if (valeur < 0) valeur = 0;
		
		this.posVoletsDegres = valeur;
		
		//calcul le coefficient de portance des volets
		this.voletCzEffet = (this.voletCzMax/40f) * valeur;
		
		//calcul le coefficient de trainee des volets
		this.coefTmpCx = 0.001f * valeur;
		
	}
	
	/**
	 * Positionne l'aileron droit directement a la valeur entree en parametre. L'aileron gauche
	 * est automatiquement positionne de facon symatrique
	 * @param posAileronD
	 */
	public void setPosAileronD(int posAileronD) {
		if (posAileronD > this.valMaxGouvernes) posAileronD = this.valMaxGouvernes;
		if (posAileronD < -this.valMaxGouvernes) posAileronD = -this.valMaxGouvernes;
		
		this.posAileronD = posAileronD;
		this.posAileronG = -posAileronD;
	}

	/**
	 * Positionne l'aileron gauche directement a la valeur entree en parametre. L'aileron droit
	 * est automatiquement positionne de facon symatrique
	 * @param posAileronG
	 */
	public void setPosAileronG(int posAileronG) {
		if (posAileronG > this.valMaxGouvernes) posAileronG = this.valMaxGouvernes;
		if (posAileronG < -this.valMaxGouvernes) posAileronG = -this.valMaxGouvernes;
		
		this.posAileronG = posAileronG;
		this.posAileronD = -posAileronG;
	}

	
}
