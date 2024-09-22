import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;



/**
 * Classe abstraite qui decrit les proprietes communesinstruments de vol IFR
 * @author GENETE mathieu 2008
 *
 */
abstract class InstrumentsIFR extends InstrumentDeBord implements MouseListener, MouseMotionListener{

	protected GraduationsCadrans graduations;
	
	protected int routeValue,tmpVal,tmpX;

	/**
	 * Conctructeur de la classe InstrumentsIFR
	 * @param tailleInstrument
	 * @param couleurFond
	 */
	public InstrumentsIFR(int tailleInstrument, Color couleurFond) 
	{
		super(tailleInstrument, couleurFond);
		
		graduations = new GraduationsCadrans((int)(tailleInstrument / 2.4),Color.white,0,35.9f,359,0,3,0.5f);
		
		graduations.setAfficherPointsCardinaux(true);
		
		routeValue = 0;
		
		addMouseListener(this);
		
		addMouseMotionListener(this);
		
	}
	
	/**
	 * Dessine la base des instruments IFR dans le contexte graphique specifie
	 * @param gc
	 */
	protected void dessineInstruIFR(Graphics gc)
	{
		//Dessine les graduations
		graduations.setCentre(dimInstrument.width/2,dimInstrument.height/2);
		graduations.dessineGraduations(gc);
	}

	/**
	 * Definit le cap a afficher sur l'instrument
	 * @param routeValue
	 */
	public void setRouteValue(int routeValue)
	{
				
		routeValue = (routeValue +1800) % 360;

		//Redessine l'instrument seulement si la valeur a changee
		if(routeValue != this.routeValue)
		{
			this.routeValue = routeValue;
			
			graduations.angleDepart = 270 - routeValue;
			
			this.repaint();
		}
	}
	
	/**
	 * Retourne le cap affiche sur l'instrument
	 * @return
	 */
	public int getRouteValue()
	{
		return this.routeValue;
	}
	
	
	/*
	 * Gestion des evennements de la souris
	 * 
	 */
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
		tmpVal = routeValue;
		tmpX = arg0.getX();
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		setRouteValue( tmpVal + (arg0.getX() - tmpX)/2);
	}

	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	
}
