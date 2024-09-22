import javax.swing.JOptionPane;

/**
 * Cette classe contient la boucle de calcul effectuee a interval regulier
 * @author GENETE mathieu 2008
 *
 */
public class BouclePrincipale extends Thread{

	//La boucle est effectuee toute les 50 millisecondes
	private  final int dT = 50;
	
	
	public  void run()
	{
		int i=0;
		long valTimeMS;
		int realDt = dT;
		int nbrMsgErr = 0;
		
		
		this.setPriority(MAX_PRIORITY);
		
		//Boucle infinie
		do
		{
			//met le temps system (en milliseconde) dans la variable valTimeMS
			valTimeMS = System.currentTimeMillis();
			
			try {
				sleep(dT);
			} catch (InterruptedException e) {
				// TODO Bloc catch auto-généré
				e.printStackTrace();
			}
			
			//realInterval ne doit pas etre egal ou inferieur a 0
			if (realDt < 1) realDt = dT;
			
			if (realDt > (dT + 100) * IFR_JavaSim.vitesseSimulation) realDt= dT;
			
			IFR_JavaSim.avion.updateAvionData(realDt);
			
			if (nbrMsgErr > 0) nbrMsgErr--;
			
			//Si l'avion s'ecrase, affiche un message
			if (IFR_JavaSim.avion.isAvionEcrase() == true)
			{

				IFR_JavaSim.manettegaz.setValManette(0);
				IFR_JavaSim.btPiloteAuto.setBoutonActive(false);
				IFR_JavaSim.vitesseSimulation = 1;
				IFR_JavaSim.reinitialiseTitreFenetre();
				
				JOptionPane.showMessageDialog(null, "Votre avion s'est ecrase. La simulation va reprendre.", "Avion ecrase", JOptionPane.INFORMATION_MESSAGE);

				//Detecte si il y a deux crash succecives donc quitte le logiciel pour
				//eviter que l'utilisateur ne puisse plus l'arreter
				//ce phenomeme est suceptible d'arriver
				if (nbrMsgErr == 1)
				{
					JOptionPane.showMessageDialog(null, "ERREUR Le simulateur va quitter", "Avion ecrase", JOptionPane.INFORMATION_MESSAGE);
					System.exit(0);
				};
				
				nbrMsgErr +=2;

			}
			
                        
			
			IFR_JavaSim.avion.moteur.setPosManetteGazs(IFR_JavaSim.manettegaz.getValManette());
			
			IFR_JavaSim.avion.aile.setPosVoletsDegres((IFR_JavaSim.manetteVolets.getValManette()*40)/1000);
			
			
			//met a jour les instruments de bord
			IFR_JavaSim.anemometre.setVitesse(IFR_JavaSim.avion.getVitesseVp()/((1 + IFR_JavaSim.avion.getAltitude()/60000)));
			
			IFR_JavaSim.horizon.setAngleAssiette(IFR_JavaSim.avion.getAssiette());
			IFR_JavaSim.horizon.setAngleRoulis(IFR_JavaSim.avion.getRoulis());
			
			IFR_JavaSim.altimetre.setAltitude(IFR_JavaSim.avion.getAltitude());
			
			IFR_JavaSim.compas.setHeadingValue((int)IFR_JavaSim.avion.getCap());
			
			IFR_JavaSim.variometre.setVitesseVerticale(IFR_JavaSim.avion.getVitesseVerticale());
			
			IFR_JavaSim.comptetour.setRpm(IFR_JavaSim.avion.moteur.getToursMinuteMoteur());
			
			IFR_JavaSim.indicVirage.setTauxVirage(IFR_JavaSim.avion.getTauxVirage());
			
			IFR_JavaSim.chronometre.UpdateChronometre(realDt);
			
			
			//Gestion du bouton du pilote automatique
			if (IFR_JavaSim.btPiloteAuto.isBoutonActive() != IFR_JavaSim.avion.isPiloteAutoAltiActive())
			{
				IFR_JavaSim.avion.setPiloteAutoAltiActive(IFR_JavaSim.btPiloteAuto.isBoutonActive());
			}
			
			
			IFR_JavaSim.btPiloteAuto.setBoutonActive(IFR_JavaSim.avion.isPiloteAutoAltiActive());
			
			//remet a false setBaliseIsRadioTuned pour toutes les balises
			for(i = 0; i < IFR_JavaSim.baliseRadio.length;i++)
			{
				IFR_JavaSim.baliseRadio[i].setBaliseIsRadioTuned(false);
			}
			
			//Calculs Adf
			IFR_JavaSim.calculsRadioADF.setFrequence(IFR_JavaSim.radioadf.getFrequence());
			IFR_JavaSim.adf.setCapAiguilleAvecInertie(IFR_JavaSim.calculsRadioADF.getDifferenceCapVersBalise_CapParam(IFR_JavaSim.avion.getCap()));
			
			//Calculs vor1
			IFR_JavaSim.calculsRadioVOR1.setFrequence(IFR_JavaSim.radiovor1.getFrequence());
			IFR_JavaSim.calculsRadioVOR1.calculVors(IFR_JavaSim.vor1);
			
			//Calculs vor2
			IFR_JavaSim.calculsRadioVOR2.setFrequence(IFR_JavaSim.radiovor2.getFrequence());
			IFR_JavaSim.calculsRadioVOR2.calculVors(IFR_JavaSim.vor2);
			
			//Calculs DME
			IFR_JavaSim.calculsRadioDME.setFrequence(IFR_JavaSim.radiodme.getFrequence());
			IFR_JavaSim.radiodme.setDME(IFR_JavaSim.calculsRadioDME.calculDME());
			
			IFR_JavaSim.carteNav.repaint();
			
			
			
			//Calcul le temps reellement mis par la boucle et multiplie par la vitesse de simulation
			realDt = (int)(System.currentTimeMillis() - valTimeMS)* IFR_JavaSim.vitesseSimulation;

			
		} while(i==i);
				
		
	}
	
	
	
}
