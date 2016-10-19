package DataStructures;

import javastrava.api.v3.model.StravaMapPoint;

public class GraphFormattedSegment 
	{
	public int id;
	public String name;
	public StravaMapPoint startPoint;
	
	public GraphFormattedSegment(int id, String name)
		{
		this.id=id;
		this.name=name;
		}
	
	public GraphFormattedSegment(int id, String name, StravaMapPoint startPoint)
		{
		this.id=id;
		this.name=name;
		this.startPoint=startPoint;
		}
	
	}
