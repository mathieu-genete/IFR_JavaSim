/**
 * Cette classe decrit les proprietes d'une surface portante 
 * @author GENETE MATHIEU 2008
 *
 */
public class SurfacePortante {

	//Avion qui possede la surface portante
	protected Avion avion;
	
	//valeur max que peuvent prendre les gouvernes
	protected int valMaxGouvernes;
	
	//surface de l'aile en m2
	protected float surfacePortanteM2;
	
	//Largeur de l'aile en m
	protected float largeurSurfacePortante;
	
	//coefficient d'efficacite de la trainee induite
	protected float effAvion;
	
	//Parametres de l'equation du coefficient de portance
	protected float cZPente0 ;
	protected float cZ0;
	protected float cZPente1;
	protected float cZ1;
	protected float alphaCZMax;
	
	//Coefficient de portance
	protected float cZ;
	
	//coefficient a utiliser pour ajouter un trainee
	protected float coefTmpCx;
	
	//Portance en Newtons
	protected float portanceN;
	
	/**
	 * Construteur de la classe Surface Portante
	 * @param avion avion concerne par la surface portante
	 * @param surfacePortanteM2 surface portante en m2
	 * @param largeurAile largeur de l'aile en m
	 * @param effAvion coefficient d'efficacite de l'avion 0 <= effAvion <= 1
	 * @param cZPente0 pente de la droite Cz = f(angle d'attack) quand angle d'attack < alphaCZMax
	 * @param cZPente1 pente de la droite Cz = f(angle d'attack) quand angle d'attack > alphaCZMax
	 * @param cZ0 intersection a Cz = f(0) quand angle d'attack < alphaCZMax
	 * @param cZ1 intersection a Cz = f(0) quand angle d'attack > alphaCZMax
	 * @param alphaCZMax angle d'incidence maximum avant decrochage de la surface portante
	 * @param valMaxGouvernes valeur max que peuvent prendre les gouvernes
	 */
	public SurfacePortante(Avion avion, float surfacePortanteM2,float largeurAile,float effAvion,float cZPente0,float cZPente1,float cZ0,float cZ1,float alphaCZMax, int valMaxGouvernes)
	{
		this.avion = avion;
		this.surfacePortanteM2 = surfacePortanteM2;
		this.largeurSurfacePortante = largeurAile;
		this.effAvion = effAvion;
		this.cZPente0 = cZPente0;
		this.cZPente1 = cZPente1;
		this.cZ0 = cZ0;
		this.cZ1 = cZ1;
		this.alphaCZMax = alphaCZMax;
		
		this.valMaxGouvernes = valMaxGouvernes;
		
		this.coefTmpCx = 0;
		
	}

	
	/**
	 *  Retourne la portance en Newtons en fonction de la densite de l'air
	 * @param densiteAir
	 * @return
	 */
	public float getPortanceN(float densiteAir)
	{
		float Rz,Cz;
		
		Cz = this.getCz(this.avion.getAssiette());
		
		Rz = calculPortance(densiteAir, Cz, this.surfacePortanteM2);
		
		return Rz;
	}
	
	/**
	 * Calcul du coefficient de portance Cz qui depend de l'angle d'attaque
	 * @return
	 */
	public float getCz(float angleAttack)
	{
		float tmpCz;
		
		if (angleAttack < alphaCZMax)
		{
			tmpCz = cZPente0 * angleAttack + cZ0;
		}
		else
		{
			tmpCz = cZPente1 * angleAttack + cZ1;
		}
		
		return tmpCz;
	}
	
	/**
	 * Retourne le coefficient de trainee induite par la portance
	 * @return
	 */
	public float getCx(float angleAttack)
	{
		float tmpCx,tmpCz,aspectRatio;
		
		tmpCz = this.getCz(angleAttack);
		
	    aspectRatio = this.largeurSurfacePortante*this.largeurSurfacePortante/this.surfacePortanteM2;
	    
	    tmpCx = (float)(((tmpCz*tmpCz)/(Math.PI*aspectRatio*effAvion)));
	    
	    tmpCx += this.coefTmpCx;
	    
		return tmpCx;
	}
	
	
	/**
	 * Calcul de la portance en Newtons
	 * @return
	 */
	protected float calculPortance(float densiteAir,float Cz,float surfaceAileM2)
	{
		float tmpRz, vMs;
		
		vMs = avion.getVitesseVp();

		tmpRz = (float)(Cz * densiteAir * surfaceAileM2 * (Math.pow(vMs,2)/2f));
		
		return tmpRz;
	}


	/**
	 * Retourne la surface portante en m2
	 * @return
	 */
	public float getSurfacePortanteM2() {
		return surfacePortanteM2;
	}

	/**
	 * retourne la largeur de la surface portante
	 * @return
	 */
	public float getLargeurSurfacePortante() {
		return largeurSurfacePortante;
	}
	

	/**
	 * definit la surface portante en m2
	 * 
	 * @param surfacePortanteM2
	 */
	public void setSurfacePortanteM2(float surfacePortanteM2) {
		this.surfacePortanteM2 = surfacePortanteM2;
	}


	/**
	 * definit la largeur de la surface portante
	 * @param largeurSurfacePortante
	 */
	public void setLargeurSurfacePortante(float largeurSurfacePortante) {
		this.largeurSurfacePortante = largeurSurfacePortante;
	}








	


	
	
}
