import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class Graph<T>
	{
	///////////////
	// Atributes //
	///////////////
	private int numNodes;
	private boolean directed;
	private int kTotal;  // Total degree
	private ArrayList<T> nodes;
	private ArrayList<ArrayList<Integer>> edges;
	
	/////////////////
	// Constructor //
	/////////////////
	public Graph(int numNodes, T defaultValue)
		{
		kTotal=0;
		nodes=new ArrayList<T>();
		edges=new ArrayList<ArrayList<Integer>>();
		this.numNodes=numNodes;
		for (int i=0; i<numNodes; i++)
			{
			nodes.add(defaultValue);
			edges.add(new ArrayList<Integer>());
			for (int j=0; j<numNodes; j++)
				{
				edges.get(i).add(0);
				}
			}
		}

	/////////////
	// Getters //
	/////////////
	public int getKTotal()
		{
		return kTotal;	
		}
	
	public int getNumNodes()
		{
		return numNodes;
		}
	
	public T getNodeValue(int i)
		{
		return nodes.get(i);
		}
	
	public int getEdgeValue(int i, int j)
		{
		return edges.get(i).get(j);
		}
	
	/////////////
	// Setters //
	/////////////
	public void setKTotal(int kTotal)
		{
		this.kTotal=kTotal;	
		}
	
	public void setNodeValue(int i, T value)
		{
		nodes.set(i,value);
		}
	
	public void setEdgeValue(int i, int j, int value)
		{
		edges.get(i).set(j,value);
		}
	
	/////////////
	// Methods //
	/////////////
	public void toConsole()
		{
		System.out.println();
		for (int i=0; i<numNodes; i++)
			{
			for(int j=0; j<numNodes; j++)
				{
				System.out.print(getEdgeValue(i, j)+" ");
				}
			System.out.println();
			}
		System.out.println();
		}
	
	public void toCSV(String pathNodes, String pathEdges)
		{
		try 
			{
	        CSVFormat csvNextLine=CSVFormat.DEFAULT.withRecordSeparator("\n");  // Indica el formato del salto de línea en el CSV
	        Object [] nodesHeader={"Id","Label"};  // Cabecera del CSV de nodos
	        Object [] edgesHeader={"Source","Target","Type","Id","Label","Weight"};  // Cabecera del CSV de aristas
			// Writing nodes file
			FileWriter nodesFile=new FileWriter(pathNodes);  // Archivo en el que se va a escribir
	        CSVPrinter printerNodes=new CSVPrinter(nodesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerNodes.printRecord(nodesHeader);
	        for (int i=0; i<numNodes; i++)
	        	{
	        	ArrayList nodeRowData=new ArrayList();
	        	nodeRowData.add(i);  // Id
	        	//nodeRowData.add(i);  // Label
	        	nodeRowData.add(getNodeValue(i));  // Label
	        	printerNodes.printRecord(nodeRowData);
		        }
	        nodesFile.flush();
	        nodesFile.close();
	        printerNodes.close();
			// Writing edges file
			FileWriter edgesFile=new FileWriter(pathEdges);  // Archivo en el que se va a escribir
	        CSVPrinter printerEdges=new CSVPrinter(edgesFile,csvNextLine);  // Objeto para imprimir en el CSV
	        printerEdges.printRecord(edgesHeader);
	        int id=0;
	        for (int i=0; i<numNodes; i++)
	        	{
	        	for (int j=0; j<numNodes; j++)
	        		{
	        		if (getEdgeValue(i,j)==1)
		        		{
			    	    ArrayList edgeRowData=new ArrayList();
			    	    edgeRowData.add(i);  // Source
			    	    edgeRowData.add(j);  // Target
			    	    edgeRowData.add("Undirected");  // Type
			    	    edgeRowData.add(id);  // Id (se calcula secuencialmente cada vez que se añade una arista)
			    	    edgeRowData.add("");  // Label (las aristas no tienen nombre)
			    	    edgeRowData.add(1.0);	 // Weight
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