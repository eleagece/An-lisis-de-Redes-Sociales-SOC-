package Code;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import DataStructures.CityPoints;
import DataStructures.GraphFormattedAthlete;
import DataStructures.GraphFormattedSegment;
import javastrava.api.v3.auth.AuthorisationService;
import javastrava.api.v3.auth.impl.retrofit.AuthorisationServiceImpl;
import javastrava.api.v3.auth.model.Token;
import javastrava.api.v3.auth.model.TokenResponse;
import javastrava.api.v3.model.StravaAthlete;
import javastrava.api.v3.model.StravaMapPoint;
import javastrava.api.v3.model.StravaSegment;
import javastrava.api.v3.model.StravaSegmentExplorerResponse;
import javastrava.api.v3.model.StravaSegmentExplorerResponseSegment;
import javastrava.api.v3.model.StravaSegmentLeaderboard;
import javastrava.api.v3.model.StravaSegmentLeaderboardEntry;
import javastrava.api.v3.rest.API;
import javastrava.api.v3.rest.AuthorisationAPI;
import javastrava.api.v3.service.Strava;

public class Principal 
	{
	public static void main(String[] args) throws FileNotFoundException 
		{
		System.out.println("******************************************");
		System.out.println("**************** CONEXIÓN ****************");
		AuthorisationService service = new AuthorisationServiceImpl();
		Token token = service.tokenExchange(9276,"9da82d69f06d0f090afafa8d57101c5754bc3228","c5f6ae2744e8a29106661f91cf9c032a11696cc1");
		Strava strava = new Strava(token);
		System.out.println("**************** /CONEXIÓN ****************");
		System.out.println("*******************************************"); 
		System.out.println();
		
		System.out.println("*********************************************");
		System.out.println("************** CSV GENERATOR ****************");
		float laSW, loSW, laNE, loNE;
		String nameCity;
		ArrayList<CityPoints> arrayCityPoints = null;
		ExtractMapPoints extractMP = new ExtractMapPoints("coordenadasMapas.txt");
		arrayCityPoints = extractMP.extractInfoCityPoints();
		for(int i = 0; i < arrayCityPoints.size(); i++)
			{
			// Recogemos del objeto CityPoints almacenado en el ArrayList los puntos del mapa
			nameCity = arrayCityPoints.get(i).getCiudad();
			laSW = Float.valueOf(arrayCityPoints.get(i).getLaSW());
			loSW = Float.valueOf(arrayCityPoints.get(i).getLoSW());
			laNE = Float.valueOf(arrayCityPoints.get(i).getLaNE());
			loNE = Float.valueOf(arrayCityPoints.get(i).getLoNE());
			// System.out.println("ciudad : "+ nameCity+" con coordenadas "+ laSW +", "+ loSW +", "+ laNE +", "+ loNE +"");
			StravaMapPoint swPoint=new StravaMapPoint(laSW,loSW);
			StravaMapPoint nePoint=new StravaMapPoint(laNE,loNE);
			// StravaHarvester saca los datos de la ciudad del dataset de Strava pasándole los puntos
			StravaHarvester sh=new StravaHarvester();
			sh.extract(swPoint, nePoint, 3, 5, strava);  // 3x3 de cuadrícula = 9x~10 = ~90 segmentos (pueden ser menos porque algún área tenga menos de 10 segmentos)
														 // 5 atletas por segmento = 5x~90 = ~450 atletas (son menos por los que se repiten en distintos segmentos)
			System.out.println(sh.arraySegments.size()+" segmentos y "+sh.arrayAthletes.size()+" atletas");  // Comprobamos que el número de datos se parezcan a los predichos
			sh.printArraySegments(); 
			sh.printArrayAthletes();
			// GephiFeeder crea los grafos y genera los .csv para Gephi a partir de ellos
			GephiFeeder gf=new GephiFeeder(sh.arraySegments.size(),sh.arrayAthletes.size());
			gf.fillGraphs(sh.arraySegments, sh.markedSegments, sh.arrayAthletes, sh.markedAthletes);
			gf.printNodes(); gf.printEdges(); gf.printWeights();
			gf.toCSV(nameCity +"_segmentsNODES.csv",nameCity+"_segmentsEDGES.csv",nameCity+"_athletesNODES.csv",nameCity+"_athletesEDGES.csv");
			}
		System.out.println("************** /CSV GENERATOR ***************");
		System.out.println("*********************************************"); 
		System.out.println();
		
		/* A partir de aquí están comentados ALGUNOS TESTS que nos han servido durante el desarrollo para probar las distintas funcionalidades
		 * creadas. Están puestos en orden, empezando por los tests más antiguos:
		 * -SEGMENT LEADERBOARD: teniendo el id de un segmento, se comprueba que devuelve el top50 de esfuerzos imprimiendo el nombre de los atletas
		 * 						 y se comprueba mirándolo en la propia web de Strava
		 * -SEGMENT EXPLORER: teniendo dos puntos (sw y ne) del mapa definidos por latitud y longitud, se comprueba que devuelve los 10 segmentos 
		 * 					  más populares en el área contenida imprimiendo su nombre. Se comprueba en la funcionalidad "Segment Explorer" de la 
		 * 					  web de Strava
		 * -AVG SPEED: calcula la velocidad media de los esfuerzos de un segmento con el máximo del top50 y el mínimo del top1. Se comprueba mirando
		 * 			   el segmento en la web de Strava y haciendo los cálculos a mano
		 * -DISTANCE TO: calcula la distancia entre dos segmentos de Strava usando la fórmula de Haversine. Se comprueba mirando la distancia que
		 *               nos indica Google Maps
		 * -HARVESTER: teniendo dos puntos sw y ne volcamos los segmentos y atletas recuperados a unas estructuras de datos que aún no están en for-
		 * 			   mato de grafo y las imprimimos para verlas. Se comprueba viendo si son consistentes con consultas directas a la web de Strava
		 * -CSV GENERATOR OLD: prueba a generar los grafos de Madrid y Barcelona a partir de las estructuras de datos obtenidas con el harvester. Una
		 * 					   vez rellenandas las estructuras de grafos, imprimie por pantalla sus datos para comprobar que son consistentes y saca
		 *					   los .csv necesarios para que Gephi pueda trabajar. Con Gephi comprobamos que son correctos
		 */
		/*
		System.out.println("*****************************************************");
		System.out.println("**************** SEGMENT LEADERBOARD ****************");
		int idAvdaComplutense=9939289;
		StravaSegmentLeaderboard segmentLeaderboard=strava.getSegmentLeaderboard(idAvdaComplutense);
		List<StravaSegmentLeaderboardEntry> leaderboardEntries=new ArrayList<StravaSegmentLeaderboardEntry>();  // ArrayList de StravaSegmentLeaderboardEntry vacío
		leaderboardEntries=segmentLeaderboard.getEntries();  // ArrayList de StravaSegmentLeaderboardEntry de la Avenida Complutense
		StravaSegmentLeaderboardEntry actualLeaderboardEntry;
		for (int i=0 ; i<leaderboardEntries.size(); i++)  // 50 por defecto
			{
			actualLeaderboardEntry=leaderboardEntries.get(i);
			System.out.println("Atleta "+i+": "+actualLeaderboardEntry.getAthleteName());
			}
		System.out.println("**************** /SEGMENT LEADERBOARD ****************");
		System.out.println("******************************************************"); 
		System.out.println();
		
		System.out.println("*****************************************************");
		System.out.println("**************** SEGMENT EXPLORER *******************");
		// Para saber latitud (eje y) y longitud (eje x) de los sitios -> http://www.bufa.es/google-maps-latitud-longitud/
		StravaMapPoint casaDeCampoSW=new StravaMapPoint(40.3969f,-3.7849f);
		StravaMapPoint casaDeCampoNE=new StravaMapPoint(40.4499f,-3.7197f);
		StravaSegmentExplorerResponse segmentExploreResponse=strava.segmentExplore(casaDeCampoSW,casaDeCampoNE,null, null, null);
		List<StravaSegmentExplorerResponseSegment> segmentsInArea=new ArrayList<StravaSegmentExplorerResponseSegment>();
		segmentsInArea=segmentExploreResponse.getSegments();
		for (int i=0 ; i<segmentsInArea.size(); i++)  // 10 máximo
			{
			//StravaSegment ss=strava.getSegment(segmentsInArea.get(i).getId());  // Esto da información completa del segmento en formato StravaSegment
			//ss.getName();
			String nombre=segmentsInArea.get(i).getName();  // Esto sólo da información simplificada del segmento en formato StravaSegmentExplorerResponseSegment
			//String nombre=segmentsInArea.get(i).get
			System.out.println("Segmento "+i+": "+nombre);
			}
		System.out.println("**************** /SEGMENT EXPLORER ******************");
		System.out.println("*****************************************************"); 
		System.out.println();
		
		System.out.println("*****************************************************");
		System.out.println("**************** AVG SPEED **************************");
		StravaSegmentExtended sse=new StravaSegmentExtended(9939289,strava);
		sse.avgSpeed(5,strava);
		System.out.println("**************** /AVG SPEED *************************");
		System.out.println("*****************************************************"); 
		System.out.println();
		
		System.out.println("*****************************************************");
		System.out.println("**************** DISTANCE TO ************************");
		StravaSegmentExtended sse1=new StravaSegmentExtended(9939289,strava);  // Avenida Complutense (asfalto)
		StravaSegmentExtended sse2=new StravaSegmentExtended(1030185,strava);  // Subida al faro de moncloa
		StravaSegmentExtended sse3=new StravaSegmentExtended(3271181,strava);  // Subida Garabitas
		StravaSegmentExtended sse4=new StravaSegmentExtended(5813080,strava);  // Hipódromo (rotonda -> calle cortada)
		StravaSegmentExtended sse5=new StravaSegmentExtended(8776114,strava);  // Valle Chico (subida)
		sse1.distanceTo(sse2);
		sse2.distanceTo(sse1);
		System.out.println("**************** /DISTANCE TO ***********************");
		System.out.println("*****************************************************"); 
		System.out.println();
				
		System.out.println("*********************************************");
		System.out.println("**************** HARVESTER ******************");
		StravaMapPoint swMAD=new StravaMapPoint(40.358008682847505f,-3.7787284090820816f);
		StravaMapPoint neMAD=new StravaMapPoint(40.51740578545436f,-3.563465042382863f);
		StravaHarvester shMAD=new StravaHarvester();
		shMAD.extract(swMAD, neMAD, 2, strava);
		shMAD.printArraySegments();
		shMAD.printArrayAthletes();
		StravaMapPoint swBAR=new StravaMapPoint(41.316256816050924f,2.024456100195282f);
		StravaMapPoint neBAR=new StravaMapPoint(41.48262260903047f,2.2874413296874696f);
		Harvester shBAR=new Harvester();
		shBAR.extract(swBAR, neBAR, 2, strava);
		shBAR.printArraySegments();
		shBAR.printArrayAthletes();
		System.out.println("**************** /HARVESTER *****************");
		System.out.println("*********************************************"); 
		System.out.println();
		
		System.out.println("*********************************************");
		System.out.println("************ CSV GENERATOR OLD **************");		
		////////////
		// MADRID //
		////////////
		// Puntos del mapa de Madrid
		StravaMapPoint swMAD=new StravaMapPoint(40.358008682847505f,-3.7787284090820816f);
		StravaMapPoint neMAD=new StravaMapPoint(40.51740578545436f,-3.563465042382863f);
		// StravaHarvester saca los datos de Madrid del dataset de Strava pasándole los puntos
		StravaHarvester shMAD=new StravaHarvester();
		shMAD.extract(swMAD, neMAD, 3, 5, strava);  // 3x3 de cuadrícula = 9x10 = 90 segmentos 
													//		>  5 atletas por segmento =  5x90 = ~450 atletas (son menos por los que se repiten en distintos segmentos)
													// 		> 10 atletas por segmento = 10x90 = ~900 atletas (son menos por los que se repiten en distintos segmentos)
													//		> 20 atletas por segmento = 20x90 = ~1800 atletas (son menos por los que se repiten en distintos segmentos)
		System.out.println(shMAD.arraySegments.size()+" segmentos y "+shMAD.arrayAthletes.size()+" atletas");
		shMAD.printArraySegments();	shMAD.printArrayAthletes();
		// GephiFeeder crea los grafos y genera los .csv para Gephi a partir de ellos
		GephiFeeder gfMAD=new GephiFeeder(shMAD.arraySegments.size(),shMAD.arrayAthletes.size());
		gfMAD.fillGraphs(shMAD.arraySegments, shMAD.markedSegments, shMAD.arrayAthletes, shMAD.markedAthletes);
		gfMAD.printNodes(); 
		gfMAD.printEdges(); 
		gfMAD.printWeights();
		gfMAD.toCSV("c:/hlocal/madrid_segmentsNODES.csv","c:/hlocal/madrid_segmentsEDGES.csv","c:/hlocal/madrid_athletesNODES.csv","c:/hlocal/madrid_athletesEDGES.csv");
		///////////////
		// BARCELONA //
		///////////////
		// Puntos del mapa de Barcelona
		StravaMapPoint swBAR=new StravaMapPoint(41.316256816050924f,2.024456100195282f);
		StravaMapPoint neBAR=new StravaMapPoint(41.48262260903047f,2.2874413296874696f);
		// StravaHarvester saca los datos de Barcelona del dataset de Strava pasándole los puntos
		StravaHarvester shBAR=new StravaHarvester();
		shBAR.extract(swBAR, neBAR, 3, 5, strava);  // 3x3 de cuadrícula = 9x10 = 90 segmentos 
													//		>  5 atletas por segmento =  5x90 = ~450 atletas (son menos por los que se repiten en distintos segmentos)
													// 		> 10 atletas por segmento = 10x90 = ~900 atletas (son menos por los que se repiten en distintos segmentos)
													//		> 20 atletas por segmento = 20x90 = ~1800 atletas (son menos por los que se repiten en distintos segmentos)
		System.out.println(shBAR.arraySegments.size()+" segmentos y "+shBAR.arrayAthletes.size()+" atletas");
		shBAR.printArraySegments();	shBAR.printArrayAthletes();
		// GephiFeeder crea los grafos y genera los .csv para Gephi a partir de ellos
		GephiFeeder gfBAR=new GephiFeeder(shBAR.arraySegments.size(),shBAR.arrayAthletes.size());
		gfBAR.fillGraphs(shBAR.arraySegments, shBAR.markedSegments, shBAR.arrayAthletes, shBAR.markedAthletes);
		gfBAR.printNodes(); 
		gfBAR.printEdges(); 
		gfBAR.printWeights();
		gfBAR.toCSV("c:/hlocal/barcelona_segmentsNODES.csv","c:/hlocal/barcelona_segmentsEDGES.csv","c:/hlocal/barcelona_athletesNODES.csv","c:/hlocal/barcelona_athletesEDGES.csv");
		System.out.println("************ /CSV GENERATOR OLD *************");
		System.out.println("*********************************************"); 
		System.out.println();
		*/
		}
	}

