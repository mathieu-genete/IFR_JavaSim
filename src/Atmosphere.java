/**
 * Cette classe decrit l'atmosphere standard
 * @author GENETE MATHIEU 2008
 *
 */
public class Atmosphere {

	private float directionVent;
        
        private float vitesseVent_ms;
        
        public Atmosphere()
        {
            this.directionVent = 0;
            this.vitesseVent_ms = 0;
        }
        
	/**
	 * calcul de la masse volumique de l'air en Kg/m3 qui decroit avec l'altitude de l'avion
	 * @return
	 */
	public float getDensiteAir(float altitude)
	{
		float tmp;
		
		tmp = (float)(3.407*Math.pow(10, -9) * (altitude * altitude) - 1.150*Math.pow(10, -4) * altitude + 1.225);
		
		if (altitude < -1000) tmp = 1.347f;
		
		return tmp;
	}
	
	/**
	 * calcul la pression en pascal en fonction de l'altitude
	 * @param altitude
	 * @return
	 */
	public float getPression(float altitude)
	{
		float tmp;
		
		tmp = (float)(4.12*Math.pow(10, -4) * (altitude * altitude) - 1.16*Math.pow(10, 1) * altitude + 101389.9f);
		
		return tmp;
	}
	
	/**
	 * calcul la temperature en kelvin en fonction de l'altitude
	 * @param altitude
	 * @return
	 */
	public float getTemperature(float altitude)
	{
		float tmp;
		
		tmp = (float)(-0.00650f * altitude + 288.15f);
		
		return tmp;
	}
        
            public float getDirectionVent() {
        return directionVent;
    }

    public void setDirectionVent(float directionVent) {
        this.directionVent = Math.abs(directionVent % 360);
    }

    public float getVitesseVent_ms() {
        return vitesseVent_ms;
    }

    public void setVitesseVent_ms(float vitesseVent) {
       
        if (vitesseVent < 0 ) vitesseVent = 0;
        if (vitesseVent > 30 ) vitesseVent = 30;
                
        this.vitesseVent_ms = vitesseVent;
    }
}
