package Code;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import DataStructures.Graph;
import DataStructures.RAWAthlete;
import DataStructures.RAWSegment;
import javastrava.api.v3.model.StravaMapPoint;
import javastrava.api.v3.model.StravaSegment;
import javastrava.api.v3.model.StravaSegmentExplorerResponse;
import javastrava.api.v3.model.StravaSegmentExplorerResponseSegment;
import javastrava.api.v3.model.StravaSegmentLeaderboard;
import javastrava.api.v3.model.StravaSegmentLeaderboardEntry;
import javastrava.api.v3.service.Strava;

public class StravaHarvester 
	{
	///////////////
	// Atributes //
	///////////////
	// Informaci�n para generar grafo de Segmentos
	public ArrayList<RAWSegment> arraySegments;  // Guarda los ids y los nombres de los segmentos que analizamos (para poder acceder a ellos cuando queramos) y un array de atletas por cada segmento 
	public Hashtable<Integer,Integer> markedSegments;  // Nos indica qu� segmentos est�n ya guardados (para evitar duplicados) y su correspondiente posici�n en el array (para tener accesos constantes)
	// Informaci�n para generar grafo de Atletas
	public ArrayList<RAWAthlete> arrayAthletes;  // Guarda los ids, los nombres de los atletas que analizamos (para poder acceder a ellos cuando queramos) y un array de segmentos por cada atleta
	public Hashtable<Integer,Integer> markedAthletes;  // Nos indica qu� atletas est�n ya guardados (para evitar duplicados) y su correspondiente posici�n en el array (para tener accesos constantes)
	
	/////////////////
	// Constructor //
	/////////////////
	public StravaHarvester()
		{
		arraySegments=new ArrayList<RAWSegment>();
		markedSegments=new Hashtable<Integer,Integer>();	
		arrayAthletes=new ArrayList<RAWAthlete>();
		markedAthletes=new Hashtable<Integer,Integer>();
		}
	
	////////////////////////
	// Harvesting Methods //
	////////////////////////
	/* Estos m�todos tienen la finalidad de sacar la informaci�n del dataset de Strava y volcarlos a unas estructuras que a�n no
	 * son en formato de grafo. Sobre ellas haremos un procesado de los datos para obtener los grafos deseados
	 */
	void extract(StravaMapPoint sw, StravaMapPoint ne, int slices, int athletesInLeaderboard, Strava strava)
		{
		/* Recorre el �rea comprendida entre el punto 'sw' y el punto 'ne' buscando segmentos en las
		 * 'slices*slices' �reas de mismo tama�o comprendidas en su interior. 'slices' es un n�mero 
		 * entre 1 y 5. Si se pasa un valor menor que 1 se le asigna 1 y si se pasa un valor mayor 
		 * que 5 se le asigna 5. Cada �rea devolver� los 10 segmentos m�s importantes haciendo uso 
		 * de la API con llamadas a 'segmentExplore'. 'athletesInLeaderboard' nos indica el n�mero
		 * de atletas que vamos a sacar de cada segmento
		 * ------------------------------------------------------------------------------------------
		 * Un buen lugar para buscar coordenadas iniciales-> http://www.bufa.es/google-maps-latitud-longitud/
		 *
		 * Madrid: sw=(40.358008682847505f,-3.7787284090820816f)
		 * 		   ne=(40.51740578545436f,-3.563465042382863f)
		 *
		 * Barcelona: sw=(41.316256816050924f,2.024456100195282f)
		 * 			  ne=(41.48262260903047f,2.2874413296874696f)
		 * ------------------------------------------------------------------------------------------
		 */
		// Control del n�mero de cuadrantes (m�nimo 1x1, m�ximo 5x5)
		if (slices<1)
			slices=1;
		else if (slices>5)
			slices=5;
		// Avance para el siguiente cuadrante en longitud y latitud
		float deltaLon=(ne.getLongitude()-sw.getLongitude())/slices;
		float deltaLat=(ne.getLatitude()-sw.getLatitude())/slices;
		// StravaMapPoints iniciales (cuadrante [0,0])
		StravaMapPoint swFirst=sw;
		StravaMapPoint neFirst=new StravaMapPoint(sw.getLatitude()+deltaLat,sw.getLongitude()+deltaLon);
		// StravaMapPoints actuales
		StravaMapPoint swCurrent; float swCurrentLat,swCurrentLon;
		StravaMapPoint neCurrent; float neCurrentLat,neCurrentLon;
		// Recuperaci�n de los ids y los nombres de los segmentos
		for (int i=0; i<slices; i++)
			{
			swCurrentLon=swFirst.getLongitude()+(deltaLon*i);
			neCurrentLon=neFirst.getLongitude()+(deltaLon*i);
			for (int j=0; j<slices; j++)
				{	
				swCurrentLat=swFirst.getLatitude()+(deltaLat*j);
				neCurrentLat=neFirst.getLatitude()+(deltaLat*j);
				/*
				//----- DEBUG ----
				System.out.println("Cuadrante ["+i+","+j+"]: ");
				System.out.println("   sw: ["+swCurrentLat+","+swCurrentLon+"]");
				System.out.println("   ne: ["+neCurrentLat+","+neCurrentLon+"]");
				//---- /DEBUG ----
				*/
				swCurrent=new StravaMapPoint(swCurrentLat,swCurrentLon);
				neCurrent=new StravaMapPoint(neCurrentLat,neCurrentLon);
				segmentExploreExtended(swCurrent,neCurrent,athletesInLeaderboard,strava);
				}
			}
		}

	void segmentExploreExtended(StravaMapPoint sw, StravaMapPoint ne, int athletesInLeaderboard, Strava strava)
		{
		/* Explora la zona acotada por 'sw' y 'ne' y guarda en el 'arraySegments'/'arrayAthletes' y 'markedSegments'/'markedAthletes'
		 * los segmentos y atletas correspondientes evitando repeticiones de atletas por aparecer en distintos segmentos
		 */
		StravaSegmentExplorerResponse segmentExploreResponse=strava.segmentExplore(sw,ne,null,null,null);
		List<StravaSegmentExplorerResponseSegment> segmentsInArea=new ArrayList<StravaSegmentExplorerResponseSegment>();
		segmentsInArea=segmentExploreResponse.getSegments();
		for (int i=0 ; i<segmentsInArea.size(); i++)  // 10 m�ximo
			{
			// �OJO! HAY QUE TENER CUIDADO CON LA CANTIDAD DE PETICIONES QUE SE HACEN
			// LA INFORMACI�N COMPLETA DE UN SEGMENTO SE ACCEDE A TRAV�S DE UN OBJETO StravaSegment (1 segmento = 1 petici�n):
			//    StravaSegment ss=strava.getSegment(segmentsInArea.get(i).getId());
			//    ss.getName();
			// PARA LA INFORMACI�N B�SICA DE UN SEGMENTO StravaSegmentExplorerResponseSegment es m�s que suficiente (10 segmentos = 1 petici�n):
			int segmentId=segmentsInArea.get(i).getId();  // id del segmento
			String segmentName=segmentsInArea.get(i).getName();  // nombre del segmento
			StravaMapPoint segmentStartPoint=segmentsInArea.get(i).getStartLatlng();  // punto final del segmento
			if (!markedSegments.containsKey(segmentId))  // si el segmento no est� registrado...
				{
				markedSegments.put(segmentId,arraySegments.size());  // ...lo metemos en la hash de marcados 'markedSegments' junto con su posici�n en 'arraySegments' y...
				//RAWSegment segm=new RAWSegment(segmentId,segmentName);  // ...lo a�adimos en el array de segmentos 'arraySegments'
				RAWSegment segm=new RAWSegment(segmentId,segmentName,segmentStartPoint);  // ...lo a�adimos en el array de segmentos 'arraySegments'
				arraySegments.add(segm);
				leaderboardExplore(segmentId,athletesInLeaderboard,strava);
				}
			}
		}

	void leaderboardExplore(int segmentId, int athletesInLeaderboard, Strava strava)
		{
		/* Explora los 'athletesInLeaderboard' primeros atletas del segmento 'segmentId' y guarda en el 
		 * 'arrayAthletes'/'arraySegments' y 'markedAthletes'/'markedSegments' seg�n sea necesario evitando 
		 * repeticiones de atletas por aparecer en distintos segmentos. 'athletesInLeaderboard' es un valor
		 * entre 5 y 50, si se introduce uno mayor o menor lo ajusta respectivamente a 50 o a 5
		 */
		StravaSegmentLeaderboard segmentLeaderboard=strava.getSegmentLeaderboard(segmentId);
		List<StravaSegmentLeaderboardEntry> leaderboardEntries=new ArrayList<StravaSegmentLeaderboardEntry>();
		leaderboardEntries=segmentLeaderboard.getEntries();  // ArrayList de StravaSegmentLeaderboardEntry del segmento 'segmentId'
		StravaSegmentLeaderboardEntry actualLeaderboardEntry;
		if (athletesInLeaderboard>50)
			athletesInLeaderboard=50;
		if (athletesInLeaderboard<5)
			athletesInLeaderboard=5;
		for (int i=0 ; i<athletesInLeaderboard; i++)  // 50 m�ximo, 5 m�nimo
			{
			actualLeaderboardEntry=leaderboardEntries.get(i);
			int athleteId=actualLeaderboardEntry.getAthleteId();
			String athleteName=actualLeaderboardEntry.getAthleteName();
			if (!markedAthletes.containsKey(athleteId))  // si el atleta no est� registrado...
				{
				markedAthletes.put(athleteId,arrayAthletes.size());  // ...lo metemos en la hash de marcados 'markedAthletes' junto con su posici�n en 'arrayAthletes' y...
				RAWAthlete ath=new RAWAthlete(athleteId,athleteName);  // ...lo a�adimos en el array de atletas 'arrayAthletes'
				arrayAthletes.add(ath);
				}
			updateInnerArrays(segmentId,athleteId);  // Adem�s actualizamos los arrays interiores
			}
		}
	
	void updateInnerArrays(int segmentId, int athleteId)
		{
		/* Mantiene los array 'segmentsId'/'athletesId' de cada elemento de 'arraySegments'/'arrayAthletes' manteniendo
		 * as� las estructuras de datos necesarias para que se puedan crear grafos a partir de ellas.
		 */
		// A�adimos un 'athleteId' al 'segmentId' que estamos analizando
		int i=markedSegments.get(segmentId);
		arraySegments.get(i).athletesId.add(athleteId);
		// A�adimos un 'segmentId' al 'athleteId' que estamos analizando
		int j=markedAthletes.get(athleteId);
		arrayAthletes.get(j).segmentsId.add(segmentId);
		}	
	
	////////////////////////
	// System.out Methods //
	////////////////////////	
	/* Todos estos m�todos tienen intenci�n de sacar informaci�n por pantalla para hacer debug
	 * y as� poder detectar errores que podamos cometer a la hora de sacar la informaci�n que queremos
	 * de Strava
	 */	
	void printArraySegments()
		{
		System.out.println("*****************************************************");
		System.out.println("************** printArraySegments() *****************");
		for (int i=0; i<arraySegments.size(); i++)
			{
			int segmentId=arraySegments.get(i).id;
			String segmentName=arraySegments.get(i).name;
			System.out.println("Segmento <pos:"+i+",id:"+segmentId+",name:"+segmentName+">");
			for (int j=0; j<arraySegments.get(i).athletesId.size(); j++)
				{
				int athleteId=arraySegments.get(i).athletesId.get(j);
				String athleteName=arrayAthletes.get(markedAthletes.get(athleteId)).name;
				System.out.println("  Atleta <pos:"+j+",id:"+athleteId+",name:"+athleteName+">");
				}
			}
		System.out.println("************** /printArraySegments() ****************");
		System.out.println("*****************************************************");
		System.out.println();
		}
	
	void printArrayAthletes()
		{
		System.out.println("*****************************************************");
		System.out.println("************** printArrayAthletes() *****************");
		for (int i=0; i<arrayAthletes.size(); i++)
			{
			int athleteId=arrayAthletes.get(i).id;
			String athleteName=arrayAthletes.get(i).name;
			System.out.println("Atleta <pos:"+i+",id:"+athleteId+",name:"+athleteName+">");
			for (int j=0; j<arrayAthletes.get(i).segmentsId.size(); j++)
				{
				int segmentId=arrayAthletes.get(i).segmentsId.get(j);
				String segmentName=arraySegments.get(markedSegments.get(segmentId)).name;
				System.out.println("  Segmento <pos:"+j+",id:"+segmentId+",name:"+segmentName+">");
				}
			}
		System.out.println("************** /printArrayAthletes() ****************");
		System.out.println("*****************************************************");
		System.out.println();
		}
	
	}
