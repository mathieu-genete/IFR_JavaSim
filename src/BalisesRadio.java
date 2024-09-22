import java.awt.*;


/**
 * Cette classe decrit un balise radio : soit un vor, vors dme ou ndb
 * @author GENETE mathieu 2008
 *
 */
public class BalisesRadio {
	
	public enum TypeBalise {VOR, VORDME, NDB}
	
	private TypeBalise typebalise;

	private String ident;
	
	private int posX;

	private int posY;

	private int altitude;

	private int frequence;
	
	//La balise est elle affichee sur le recepteur radio de l'avion ?
	private boolean baliseIsRadioTuned;
	
	//Radiale selectionnee sur le recepteur vor
	private int radialSelectRecepteur;
	
	/**
	 * Constructeur de la classe BalisesRadio
	 *
	 */
	public BalisesRadio()
	{
		this.typebalise = TypeBalise.VOR;

		this.ident = "";

		this.posX = 0;
		this.posY = 0;

		this.altitude = 0;

		this.baliseIsRadioTuned = false;
		
		this.frequence = 10800;
	}
	
	/**
	 * Surcharge du constructeur de la classe BalisesRadio
	 * @param typeBalise
	 * @param ident
	 * @param posX
	 * @param posY
	 * @param altitude
	 * @param frequence
	 */
	public BalisesRadio(TypeBalise typeBalise, String ident, int posX, int posY, int altitude, int frequence)
	{
		this.typebalise = typeBalise;
		
		this.ident = ident;
		
		this.posX = posX;
		this.posY = posY;
		
		this.altitude = altitude;
		
		this.baliseIsRadioTuned = false;
		
		this.frequence = frequence;
	}

	
	//METHODES GET
	
	/**
	 * retourne la position X de la balise
	 * @return
	 */
	public int getPositionX()
	{
		return this.posX;
	}
	
	/**
	 * retourne la position Y de la balise
	 * @return
	 */
	public int getPositionY()
	{
		return this.posY;
	}
	
	/**
	 * retourne l'identifiant de la balise
	 * @return
	 */
	public String getIdent()
	{
		return this.ident;
	}
	
	/**
	 * retourne la frequence de la balise
	 * @return
	 */
	public int getFrequence()
	{
		return this.frequence;
	}
	
	
	/**
	 * retourne l'altitude de la balise en m
	 * @return
	 */
	public int getAltitude()
	{
		return this.altitude;
	}
	
	/**
	 * retourne le type de la balise
	 * @return
	 */
	public TypeBalise getTypeBalise()
	{
		return this.typebalise;
	}
	
	/**
	 * Indique si la balise est affichee sur le recepteur radio de l'avion
	 * @return
	 */
	public boolean getBaliseIsRadioTuned() {
		return baliseIsRadioTuned;
	}
	
	
	/**
	 * Retourne la radiale selectionnee si la balise est affiche sur le recepteur radio
	 * @return
	 */
	public int getRadialSelectRecepteur() {
		return radialSelectRecepteur;
	}
	
	
	
	//METHODES SET
	
	
	/**
	 * Fixe la position de la balise sur la carte 1 pixel correspond a 0,1 mile nautique
	 * @param posX
	 * @param posY
	 */
	public void setPosition(int posX,int posY)
	{
		this.posX = posX;
		this.posY = posY;
	}
	
	/**
	 * definit l'identifiant de la balise
	 * @param ident
	 */
	public void setIdent(String ident)
	{
		this.ident = ident;
	}
	
	/**
	 * definit la fraquence de la balise
	 * @param frequence
	 */
	public void setFrequence(int frequence)
	{
		this.frequence = frequence;
	}
	
	
	/**
	 * definit l'altitude de la balise en m
	 * @param alt
	 */
	public void setAltitude(int alt)
	{
		this.altitude = alt;
	}
	
	/**
	 * definit le type de balise : VOR VORDME ou NDB
	 * @return
	 */
	public void setTypeBalise(TypeBalise typebal)
	{
		this.typebalise = typebal;
	}

	/**
	 * Definit si la balise est affichee sur le recepteur radio de l'avion
	 * @param isTrueOrFalse
	 */
	public void setBaliseIsRadioTuned(boolean isTrueOrFalse) {
		this.baliseIsRadioTuned = isTrueOrFalse;
	}


	/**
	 * definit la radiale selectionnee. Recuperer la valeur getRouteValue() sur l'instrument vor concerne
	 * @param radialSelectRecepteur
	 */
	public void setRadialSelectRecepteur(int radialSelectRecepteur) {
		this.radialSelectRecepteur = radialSelectRecepteur;
	}

}
