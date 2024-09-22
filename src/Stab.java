/**
 * Cette classe decrit le stabilisateur horizontal d'un avion
 * @author GENETE MATHIEU 2008
 *
 */
public class Stab extends SurfacePortante{

	//surface de la gouverne de profondeur en m2
	private float surfaceGouvProf;
	
	private int posGouverneProf;
	
	/**
	 * Constructeur de la classe Stab
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
	public Stab(Avion avion, float surfaceAileM2,float largeurAile,float effAvion,float cZPente0,float cZPente1,float cZ0,float cZ1,float alphaCZMax, int valMaxGouvernes)
	{
		super(avion, surfaceAileM2, largeurAile, effAvion, cZPente0, cZPente1, cZ0, cZ1, alphaCZMax, valMaxGouvernes);
	
		this.posGouverneProf = 0;
	}
	
	/**
	 * Incremente la position de la gouverne de profondeur de la valeur passee en parametre.
	 * Utilise pour augmenter l'assiette de l'avion
	 * @param stepVal
	 */
	public void incGouvProf(int stepVal)
	{
		this.posGouverneProf += stepVal;
		
		if (this.posGouverneProf > valMaxGouvernes) this.posGouverneProf = valMaxGouvernes;
		
	}
	
	/**
	 *Decremente la position de la gouverne de profondeur de la valeur passee en parametre.
	 *Utilise pour diminuer l'assiette de l'avion
	 * @param stepVal
	 */
	public void decGouvProf(int stepVal)
	{
		this.posGouverneProf -= stepVal;
		
		if (this.posGouverneProf < -valMaxGouvernes) this.posGouverneProf = -valMaxGouvernes;

	}
	
	/**
	 * Positionne la gouverne de profondeur directement a la valeur entree en parametre
	 * @param posGouverneProf
	 */
	public void setPosGouverneProf(int posGouverneProf) {
		if (posGouverneProf > this.valMaxGouvernes) posGouverneProf = this.valMaxGouvernes;
		if (posGouverneProf < -this.valMaxGouvernes) posGouverneProf = -this.valMaxGouvernes;
		
		this.posGouverneProf = posGouverneProf;
	}
	
	/**
	 * retourne la position de la gouverne de profondeur
	 * @return
	 */
	public int getPosGouverneProf() {
		return posGouverneProf;
	}
}
