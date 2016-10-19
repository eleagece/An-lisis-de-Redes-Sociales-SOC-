package Code;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javastrava.api.v3.model.StravaMapPoint;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import DataStructures.Graph;
import DataStructures.GraphFormattedAthlete;
import DataStructures.GraphFormattedSegment;
import DataStructures.RAWAthlete;
import DataStructures.RAWSegment;

public class GephiFeeder 
	{
	///////////////
	// Atributes //
	///////////////
	private Graph<GraphFormattedSegment> segmentsGraph;
	private Graph<GraphFormattedAthlete> athletesGraph;
	
	/////////////////
	// Constructor //
	/////////////////
	public GephiFeeder(int numSegments, int numAthletes)
		{
		segmentsGraph=new Graph<GraphFormattedSegment>(numSegments,null);
		athletesGraph=new Graph<GraphFormattedAthlete>(numAthletes,null);
		}	
	
	public GephiFeeder(int numSegments, int numAthletes, GraphFormattedSegment gfs, GraphFormattedAthlete gfa)
		{
		segmentsGraph=new Graph<GraphFormattedSegment>(numSegments,gfs);
		athletesGraph=new Graph<GraphFormattedAthlete>(numAthletes,gfa);
		}
	
	////////////////////////////
	// Creating Graph Methods //
	////////////////////////////
	/* Estos métodos se encargan de usar los datos contenidos en 'arraySegments'/'arrayAthletes' y 'markedSegments'/'markedAthletes' para
	 * rellenar los grafos 'segmentsGraph'/'athletesGraph'
	 */
	public void fillGraphs(ArrayList<RAWSegment> arraySegments, Hashtable<Integer,Integer> markedSegments,
						   ArrayList<RAWAthlete> arrayAthletes, Hashtable<Integer,Integer> markedAthletes)
		/* Hace las llamadas necesarias a los métodos privados para rellenar los grafos 'segmentsGraph' y 'athletesGraph'
		 * correctemente 
		 */
		{
		// Primero rellenamos los nodos
		fillGraphsNodes(arraySegments,arrayAthletes);
		// Después rellenamos las aristas y los pesos
		fillGraphsEdges(arraySegments,markedSegments,arrayAthletes,markedAthletes);
		}
	
	private void fillGraphsNodes(ArrayList<RAWSegment> arraySegments, ArrayList<RAWAthlete> arrayAthletes)
		{
		/* Rellena los arrays de la estructura de grafo que corresonponden a los nodos. Lo hace para los dos grafos
		 * 'segmentsGraph' y 'athletesGraph' 
		 */
		/*
		//----- DEBUG ----
		System.out.println("Escribiendo "+arraySegments.size()+" nodos de 'arraySegments':");
		//---- /DEBUG ----
		*/
		for (int i=0; i<arraySegments.size(); i++)
			{
			int idSegment=arraySegments.get(i).id;
			String nameSegment=arraySegments.get(i).name;
			StravaMapPoint startPointSegment=arraySegments.get(i).startPoint;
			GraphFormattedSegment gfs=new GraphFormattedSegment(idSegment,nameSegment,startPointSegment);
			// el arrayList<GraphFormattedSegment> de Graph tiene los elementos en el mismo orden que el arrayList<RAWSegment> de StravaHarvester
			segmentsGraph.setNodeValue(i,gfs);
			/*
			//----- DEBUG ----
			System.out.println("  Segmento <pos:"+i+",id:"+gfs.id+",name:"+gfs.name+">");
			//---- /DEBUG ----
			*/
			}
		/*
		//----- DEBUG ----
		System.out.println("Escribiendo "+arrayAthletes.size()+" nodos de 'arrayAthletes':");
		//---- /DEBUG ----
		*/
		for (int i=0; i<arrayAthletes.size(); i++)
			{
			int idAthlete=arrayAthletes.get(i).id;
			String nameAthlete=arrayAthletes.get(i).name;
			GraphFormattedAthlete gfa=new GraphFormattedAthlete(idAthlete,nameAthlete);
			// el arrayList<GraphFormattedAthlete> de Graph tiene los elementos en el mismo orden que el arrayList<RAWAthlete> de StravaHarvester
			athletesGraph.setNodeValue(i,gfa);
			/*
			//----- DEBUG ----
			System.out.println("  Atleta <pos:"+i+",id:"+gfa.id+",name:"+gfa.name+">");
			//---- /DEBUG ----
			*/
			}		
		}
	
	private void fillGraphsEdges(ArrayList<RAWSegment> arraySegments, Hashtable<Integer,Integer> markedSegments,
			   					 ArrayList<RAWAthlete> arrayAthletes, Hashtable<Integer,Integer> markedAthletes)
		{
		/* Rellena las matrices de la estructura de grafo que corresonponden a las aristas y los pesos. Lo hace para 
		 * los dos grafos 'segmentsGraph' y 'athletesGraph'. 
		 * Aunque en realidad no es necesario escribir a los dos lados de la diagonal replicando datos para luego 
		 * borrar una mitad era interesante a la hora de depurar porque las matrices de aristas impresas en conso-
		 * la podían ser muy grandes
		 */
		for (int i=0; i<segmentsGraph.getNumNodes(); i++)  // Recorro los segmentos
			{
			int segmentPos=i;  // Pos del segmento en el grafo, que corresponde al que tiene en 'arraySegments'
			int segmentId=segmentsGraph.getNodeValue(i).id;  // Id original del segmento
			for (int j=0; j<arraySegments.get(i).athletesId.size(); j++)  // Recorro los atletas del segmento (j)
				{
				int athleteIdA=arraySegments.get(i).athletesId.get(j);  // Id original del atleta 'A' relacionado con el segmento 'i'
				int athletePosA=markedAthletes.get(athleteIdA);  // Pos en 'arrayAthletes' del atleta 'A' relacionado con el segmento 'i'
				for (int k=j+1; k<arraySegments.get(i).athletesId.size(); k++)  // Recorro los atletas del segmento (j+1=k)
					{
					int athleteIdB=arraySegments.get(i).athletesId.get(k);  // Id original del atleta 'B' relacionado con el segmento 'i'
					int athletePosB=markedAthletes.get(athleteIdB);  // Pos en 'arrayAthletes' del atleta 'B' relacionado con el segmento 'i'
					// Acualización de las matrices de pesos y aristas del grafo de atletas 'athletesGraph'
					athletesGraph.setEdgeValue(athletePosA,athletePosB,1);
					athletesGraph.setEdgeValue(athletePosB,athletePosA,1);
					athletesGraph.setWeightValuePlus1(athletePosA,athletePosB);
					athletesGraph.setWeightValuePlus1(athletePosB,athletePosA);
					}
				}
			}
		for (int i=0; i<athletesGraph.getNumNodes(); i++)  // Recorro para los nodos
			{
			int athletePos=i;  // Pos del atleta en el grafo, que corresponde al que tiene en 'arrayAthletes'
			int athleteId=athletesGraph.getNodeValue(i).id;  // Id original del atleta
			for (int j=0; j<arrayAthletes.get(i).segmentsId.size(); j++)  // Recorro los segmentos del atleta (j)
				{
				int segmentIdA=arrayAthletes.get(i).segmentsId.get(j);  // Id original del segmento 'A' relacionado con el atleta 'i'
				int segmentPosA=markedSegments.get(segmentIdA);  // Pos en 'arraySegments' del segmento 'A' relacionado con el atleta 'i'
				for (int k=j+1; k<arrayAthletes.get(i).segmentsId.size(); k++)  // Recorro los segmentos del atleta (j+1=k)
					{
					int segmentIdB=arrayAthletes.get(i).segmentsId.get(k);  // Id original del segmento 'B' relacionado con el atleta 'i'
					int segmentPosB=markedSegments.get(segmentIdB);  // Pos en 'arraySegments' del segmento 'B' relacionado con el atleta 'i'
					// Acualización de las matrices de pesos y aristas del grafo de segmentos 'segmentsGraph'
					segmentsGraph.setEdgeValue(segmentPosA,segmentPosB,1);
					segmentsGraph.setEdgeValue(segmentPosB,segmentPosA,1);
					segmentsGraph.setWeightValuePlus1(segmentPosA,segmentPosB);
					segmentsGraph.setWeightValuePlus1(segmentPosB,segmentPosA);
					}
				}
			}
		keepUndirected(segmentsGraph);
		keepUndirected(athletesGraph);
		}
	
	private void keepUndirected(Graph graph)
		{
		/* Deja ambos grafos no dirigidos eliminando la mitad superior de la diagonal (al crearlos están en modo espejo)
		 */
		for (int i=0; i<graph.getNumNodes(); i++)
			for (int j=0; j<graph.getNumNodes(); j++)
				if (i>=j)
					{
					graph.setEdgeValue(i, j, 0);
					graph.setWeightValue(i, j, 0);
					}
		}
	
	////////////////////////
	// System.out Methods //
	////////////////////////	
	/* Todos estos métodos tienen intención de sacar información de los grafos por pantalla para hacer debug
	 * y así poder detectar errores que podamos haber cometido a la hora de meter los datos en ellos
	 */		
	public void printNodes()
		{
		System.out.println("*****************************************************");
		System.out.println("****************** printNodes() *********************");	
		System.out.println("Nodos de los segmentos");
		for (int i=0; i<segmentsGraph.getNumNodes(); i++)
			{
			int pos=i;
			int id=segmentsGraph.getNodeValue(i).id;
			String name=segmentsGraph.getNodeValue(i).name;
			System.out.println("  Segmento <pos:"+i+",id:"+id+",name:"+name+">");
			}		
		System.out.println("*****************************************************");
		System.out.println("Nodos de los atletas");
		for (int i=0; i<athletesGraph.getNumNodes(); i++)
			{
			int pos=i;
			int id=athletesGraph.getNodeValue(i).id;
			String name=athletesGraph.getNodeValue(i).name;
			System.out.println("  Atleta <pos:"+i+",id:"+id+",name:"+name+">");
			}
		System.out.println("****************** /printNodes() ********************");
		System.out.println("*****************************************************");
		System.out.println();
		}
	
	public void printEdges()
		{
		System.out.println("*****************************************************");
		System.out.println("****************** printEdges() *********************");
		System.out.println("Aristas de los segmentos");
		segmentsGraph.printEdges();
		System.out.println("*****************************************************");
		System.out.println("Aristas de los atletas");
		athletesGraph.printEdges();
		System.out.println("****************** /printEdges() ********************");
		System.out.println("*****************************************************");
		System.out.println();
		}
		
	public void printWeights()
		{
		System.out.println("*****************************************************");
		System.out.println("***************** printWeights() ********************");
		System.out.println("Pesos de los segmentos");
		segmentsGraph.printWeights();
		System.out.println("*****************************************************");
		System.out.println("Pesos de los atletas");
		athletesGraph.printWeights();
		System.out.println("***************** /printWeights() *******************");
		System.out.println("*****************************************************");
		System.out.println();		
		}
	
	
	//////////////////////////
	// Graph to CSV Methods //
	//////////////////////////
	public void toCSV(String pathNodesSegments, String pathEdgesSegments, String pathNodesAthletes, String pathEdgesAthletes)
		{
		/* Este método se encarga de sacar los CSV que serán la entrada para Gephi. Podemos indicar la ruta y el
		 * nombre donde se dejarán los archivos
		 */
		try 
			{
			/////////////////////////////////
			// CSVs del grafo de segmentos //
			/////////////////////////////////
	        CSVFormat csvNextLine=CSVFormat.DEFAULT.withRecordSeparator("\n");  // Indica el formato del salto de línea en el CSV
	        Object [] nodesHeader={"Id","Label","Latitude","Longitude"};  // Cabecera del CSV de nodos
	        Object [] edgesHeader={"Source","Target","Type","Id","Label","Weight"};  // Cabecera del CSV de aristas
			// Writing nodes file
			FileWriter nodesFile=new FileWriter(pathNodesSegments);  // Archivo en el que se va a escribir
	        CSVPrinter printerNodes=new CSVPrinter(nodesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerNodes.printRecord(nodesHeader);
	        for (int i=0; i<segmentsGraph.getNumNodes(); i++)
	        	{
	        	GraphFormattedSegment gfs=segmentsGraph.getNodeValue(i);
	        	ArrayList nodeRowData=new ArrayList();
	        	nodeRowData.add(gfs.id);  // Id
	        	nodeRowData.add(gfs.name);  // Label
	        	nodeRowData.add(gfs.startPoint.getLatitude());
	        	nodeRowData.add(gfs.startPoint.getLongitude());
	        	printerNodes.printRecord(nodeRowData);
		        }
	        nodesFile.flush();
	        nodesFile.close();
	        printerNodes.close();
			// Writing edges file
			FileWriter edgesFile=new FileWriter(pathEdgesSegments);  // Archivo en el que se va a escribir
	        CSVPrinter printerEdges=new CSVPrinter(edgesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerEdges.printRecord(edgesHeader);
	        int id=0;
	        for (int i=0; i<segmentsGraph.getNumNodes(); i++)
	        	{
	        	GraphFormattedSegment gfsA=segmentsGraph.getNodeValue(i);
	        	for (int j=0; j<segmentsGraph.getNumNodes(); j++)
	        		{
		        	GraphFormattedSegment gfsB=segmentsGraph.getNodeValue(j);
	        		if (segmentsGraph.getEdgeValue(i,j)==1)
		        		{
			    	    ArrayList edgeRowData=new ArrayList();
			    	    edgeRowData.add(gfsA.id);  // Source
			    	    edgeRowData.add(gfsB.id);  // Target
			    	    edgeRowData.add("Undirected");  // Type
			    	    edgeRowData.add(id);  // Id (se calcula secuencialmente cada vez que se añade una arista)
			    	    edgeRowData.add("");  // Label (las aristas no tienen nombre)
			    	    edgeRowData.add(segmentsGraph.getWeightValue(i,j));	 // Weight
			    	    printerEdges.printRecord(edgeRowData);
		        		id++;
		        		}
	        		}
		        }
	        edgesFile.flush();
	        edgesFile.close();
	        printerEdges.close();	
			}
		catch (IOException e) 
			{
			e.printStackTrace();
			}
		try 
			{
			///////////////////////////////
			// CSVs del grafo de atletas //
			///////////////////////////////
	        CSVFormat csvNextLine=CSVFormat.DEFAULT.withRecordSeparator("\n");  // Indica el formato del salto de línea en el CSV
	        Object [] nodesHeader={"Id","Label"};  // Cabecera del CSV de nodos
	        Object [] edgesHeader={"Source","Target","Type","Id","Label","Weight"};  // Cabecera del CSV de aristas
			// Writing nodes file
			FileWriter nodesFile=new FileWriter(pathNodesAthletes);  // Archivo en el que se va a escribir
	        CSVPrinter printerNodes=new CSVPrinter(nodesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerNodes.printRecord(nodesHeader);
	        for (int i=0; i<athletesGraph.getNumNodes(); i++)
	        	{
	        	GraphFormattedAthlete gfa=athletesGraph.getNodeValue(i);
	        	ArrayList nodeRowData=new ArrayList();
	        	nodeRowData.add(gfa.id);  // Id
	        	nodeRowData.add(gfa.name);  // Label
	        	printerNodes.printRecord(nodeRowData);
		        }
	        nodesFile.flush();
	        nodesFile.close();
	        printerNodes.close();
			// Writing edges file
			FileWriter edgesFile=new FileWriter(pathEdgesAthletes);  // Archivo en el que se va a escribir
	        CSVPrinter printerEdges=new CSVPrinter(edgesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerEdges.printRecord(edgesHeader);
	        int id=0;
	        for (int i=0; i<athletesGraph.getNumNodes(); i++)
	        	{
	        	GraphFormattedAthlete gfaA=athletesGraph.getNodeValue(i);
	        	for (int j=0; j<athletesGraph.getNumNodes(); j++)
	        		{
	        		GraphFormattedAthlete gfaB=athletesGraph.getNodeValue(j);
	        		if (athletesGraph.getEdgeValue(i,j)==1)
		        		{
			    	    ArrayList edgeRowData=new ArrayList();
			    	    edgeRowData.add(gfaA.id);  // Source
			    	    edgeRowData.add(gfaB.id);  // Target
			    	    edgeRowData.add("Undirected");  // Type
			    	    edgeRowData.add(id);  // Id (se calcula secuencialmente cada vez que se añade una arista)
			    	    edgeRowData.add("");  // Label (las aristas no tienen nombre)
			    	    edgeRowData.add(athletesGraph.getWeightValue(i,j));	 // Weight
			    	    printerEdges.printRecord(edgeRowData);
		        		id++;
		        		}
	        		}
		        }
	        edgesFile.flush();
	        edgesFile.close();
	        printerEdges.close();	
			}
		catch (IOException e) 
			{
			e.printStackTrace();
			}
	    }  

	}
