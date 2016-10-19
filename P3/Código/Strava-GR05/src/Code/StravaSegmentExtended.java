package Code;

import java.util.ArrayList;
import java.util.List;
import javastrava.api.v3.model.StravaSegment;
import javastrava.api.v3.model.StravaSegmentLeaderboard;
import javastrava.api.v3.model.StravaSegmentLeaderboardEntry;
import javastrava.api.v3.service.Strava;

public class StravaSegmentExtended 
	{
	///////////////
	// Atributes //
	///////////////
	private int avgNumber;
	private float avgSpeed;
	private StravaSegment ss;
	
	/////////////////
	// Constructor //
	/////////////////
	public StravaSegmentExtended(int id, Strava strava)
		{
		avgNumber=0;
		avgSpeed=0;
		ss=strava.getSegment(id);
		}	
	
	/////////////
	// Getters //
	/////////////
	public StravaSegment getStravaSegment()
		{
		return ss;
		}
	
	/////////////
	// Methods //
	/////////////
	public double distanceTo(StravaSegmentExtended other)
		{
		/* Calcula la distancia desde el final del StravaSegmentExtended actual (this) al inicio del
		 * StravaSegmentExtended pasado por parámetro (other). Información sacada de:
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		int earthRadius=6371000;
		// Point 1
		double latThisDegree=this.getStravaSegment().getEndLatlng().getLatitude();
		double latThisRad=Math.toRadians(latThisDegree);
		double longThisDegree=this.getStravaSegment().getEndLatlng().getLongitude();
		double longThisRad=Math.toRadians(longThisDegree);
		String pointAString="["+latThisDegree+","+longThisDegree+"]";
		// Point 2
		double latOtherDegree=other.getStravaSegment().getStartLatlng().getLatitude();
		double latOtherRad=Math.toRadians(latOtherDegree);
		double longOtherDegree=other.getStravaSegment().getStartLatlng().getLongitude();
		double longOtherRad=Math.toRadians(longOtherDegree);
		String pointBString="["+latOtherDegree+","+longOtherDegree+"]";	
		// Delta points
		double deltaLat=Math.toRadians(latOtherDegree-latThisDegree);
		double deltaLong=Math.toRadians(longOtherDegree-longThisDegree);
		// Haversine formula
		double a=Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
		         Math.cos(latThisDegree) * Math.cos(latOtherDegree) *
		         Math.sin(deltaLong/2) * Math.sin(deltaLong/2);
		double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		double d=earthRadius*c;
		// Console output
		System.out.println("De "+ss.getName()+""+pointAString+" a "+other.getStravaSegment().getName()+""+pointBString+":");
		System.out.println("   Distancia: "+d);
		return d;
		}

	public float avgSpeed(int top, Strava strava)
		{
		/* Calcula la velocidad media de los 'top' máximos esfuerzos en el segmento. Los esfuerzos se
		 * sacan del ránking del segmento. 'top' tiene que estar entre 1 y 50. Si es menor se queda a 1
		 * y si es mayor se queda a 50
		 */
		float averageSpeed=0f;
		StravaSegmentLeaderboard segmentLeaderboard=strava.getSegmentLeaderboard(ss.getId());
		List<StravaSegmentLeaderboardEntry> leaderboardEntries=new ArrayList<StravaSegmentLeaderboardEntry>();  // ArrayList de StravaSegmentLeaderboardEntry vacío
		leaderboardEntries=segmentLeaderboard.getEntries();  // ArrayList de StravaSegmentLeaderboardEntry del segmento
		float space=(ss.getDistance())/1000;  // En km
		System.out.println("Distancia del segmento: "+space+" m");
		for (int i=0 ; i<Math.max(1,(Math.min(top,50))); i++)  // 50 por defecto, si metemos más sólo calcula hasta 50
			{
			StravaSegmentLeaderboardEntry actualLeaderboardEntry=leaderboardEntries.get(i);
			int time=actualLeaderboardEntry.getMovingTime();  // En s
			float speed=(space/time)*3600;  // En km/s
			System.out.println("Atleta "+i+": "+actualLeaderboardEntry.getAthleteName());
			System.out.println("   Tiempo: "+time+" s");
			System.out.println("   Velocidad: "+speed+" km/h");
			averageSpeed=averageSpeed+speed;
			}
		averageSpeed=averageSpeed/Math.max(1,(Math.min(top,50)));
		System.out.println("La velocidad media de los "+Math.max(1,(Math.min(top,50)))+" primeros esfuerzos es "+averageSpeed);
		return averageSpeed;
		}
	}
