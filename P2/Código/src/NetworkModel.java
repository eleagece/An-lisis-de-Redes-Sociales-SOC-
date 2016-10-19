import java.util.Random;

public class NetworkModel
	{
	public static final int RANDOM=1;
	public static final int BARABASI_ALBERT=2;
	private Graph<Integer> graphRandom;  // Node value: don't care
	private Graph<Integer> graphBarabasiAlbert;  // Node value: degree
	
	/////////////////
	// Constructor //
	/////////////////
	public NetworkModel(int type, int numNodes, double prob, int numNodesInit)
		{
		switch (type)
			{
			case RANDOM:
				graphRandom=new Graph/*<Integer>*/(numNodes,prob);
				random(numNodes,prob,graphRandom);
				break;
			case BARABASI_ALBERT:
				graphBarabasiAlbert=new Graph/*<Integer>*/(numNodes,0);
				barabasiAlbert(numNodes,numNodesInit,graphBarabasiAlbert);
				break;
			default:
				break;
			}
		}
	
	/////////////
	// Methods //
	/////////////
	public Graph getGraph(int type)
		{
		switch (type)
			{
			case RANDOM:
				return graphRandom;
			case BARABASI_ALBERT:
				return graphBarabasiAlbert;
			default:
				return graphRandom;
			}		
		}

	private void random(int numNodes, double prob, Graph graph)
		{
		double randomNumber;
		for (int i=0; i<numNodes; i++)
			{
			graph.setNodeValue(i,i);  // Es indiferente lo que se mete
			for (int j=i+1; j<numNodes; j++)
				{
				randomNumber=Math.random();
				if (i!=j && randomNumber<=prob)
					{
					graph.setEdgeValue(i, j, 1);
					//graph.setEdgeValue(j, i, 1);
					}	
				}
			}
		//keepUndirected(graph);
		}
	
	private void barabasiAlbertInit(int numNodes, int numNodesInit, Graph graph)
		{
		int degree=numNodesInit-1;
		for (int i=0; i<numNodesInit; i++)
			{
			graph.setNodeValue(i,degree);
			graph.setKTotal(graph.getKTotal()+degree);
			for (int j=0; j<numNodesInit; j++)
				{
				if (i!=j)
					{
					graph.setEdgeValue(i, j, 1);
					graph.setEdgeValue(j, i, 1);
					}
				}
			}
		//keepUndirected(graph);
		}

	/*
	 * Este método elige aleatoriamente uno de entre todos los nodos a los que se puede conectar el nodo que estamos
	 * analizando. Se puede dar el caso de que aleatoriamente se vuelva a elegir el mismo, pero son limitaciones acep-
	 * tables.
	 * m -> para poder generar un número aleatorio entre 0 y m-1
	 * graph -> para comprobar que no hemos generado uno que esté ya enlazado
	 */
	private int chooseRandomly(int m, Random rnd)
		{
		return (int)(rnd.nextDouble()*m);  // devuelve un entero entre 0 y m-1
		}
	
	private boolean isConnected(int m, int i, Graph graph)
		{
		boolean b;
		if (graph.getEdgeValue(m, i)==1 || graph.getEdgeValue(i, m)==1)
			b=true;
		else
			b=false;
		return b;
		}
	
	private void barabasiAlbert(int numNodes, int numNodesInit, Graph graph)
		{
		barabasiAlbertInit(numNodes,numNodesInit,graph);  // Crea el estado inicial con todos los nodos conectados
		Random rnd=new Random();  // Crea el generador de números aleatorios basándonos en el instante actual		
		for (int m=numNodesInit; m<numNodes; m++)  // Añadimos un nodo nuevo cada vez. El índice del nuevo nodo es 'm'
			{
			int kTotalDegree=graph.getKTotal();  // El grado total del grafo (suma de todos los grados de todos los nodos)
			int nodeMDegree=0;  // El grado actual del nodo que se crea con índice 'm'
			int index=0;  // Índice para contar el número de nodos que se conectan
			while (index<numNodesInit)  // Vamos a conectar el nodo de índice 'm' con exactamente 'numNodesInit' nodos existentes
				{
				int i=chooseRandomly(m,rnd);  // Nos dice aleatoriamente con qué nodo 'i' vamos a intentar conectar el nodo 'm'
				if (!isConnected(m,i,graph))  // Si no estamos conectados con el nodo 'i' conectamos con el nodo 'i'
					{
					int nodeIDegree=(int)graph.getNodeValue(i);
					int totalDegree=graph.getKTotal();
					double degreeDIVTotalDegree=(double)nodeIDegree/(double)totalDegree;
					double randomNumber=Math.random();
					if (randomNumber<degreeDIVTotalDegree)
						{
						graph.setEdgeValue(i, m, 1);  // Las aristas entre el nodo 'i' y el 'm' se actualizan
						graph.setEdgeValue(m, i, 1);
						graph.setNodeValue(i,nodeIDegree+1);  // El grado del nodo con el que conectamos ('i') aumenta en 1
						nodeMDegree++;  // El grado del nodo que analizamos ('m') aumenta en 1
						kTotalDegree=kTotalDegree+2;  // El grado total aumenta en 2
						graph.setNodeValue(m,nodeMDegree);  // Actualizamos el nodo 'm' en el grafo
						graph.setKTotal(kTotalDegree);  // Actualizamos el grado total del grafo
						index++;  // Al conectar 'm' con 'i' actualizamos 'index' para aumentar el bucle while
						}
					}
				else   
					{
					// Si estamos conectados con el nodo 'i' no hacemos nada y el bucle continua sin actualizar 'index'
					}
				}
			}
		keepUndirected(graph);
		}
		
	/*
	 * Este método era correcto a excepción de que no ponía límites a los nuevos enlaces y probaba con todos los
	 * nodos anteriores. El nuevo se limita a hacer un máximo de conexiones igual al número inicial de nodos
	 */
	/*
	private void barabasiAlbert(int numNodes, int numNodesInit, Graph graph)
		{
		barabasiAlbertInit(numNodes,numNodesInit,graph);  // Crea el estado inicial con todos los nodos conectados
		int m=numNodesInit;  // Nodo que estamos evaluando
		while (m<numNodes)
			{
			int kTotalDegree=graph.getKTotal();
			int nodeMDegree=0;
			for (int i=0; i<=m; i++)
				{
				int nodeIDegree=(int)graph.getNodeValue(i);
				int totalDegree=graph.getKTotal();
				double degreeDIVTotalDegree=(double)nodeIDegree/(double)totalDegree;
				double randomNumber=Math.random();
				if (randomNumber<degreeDIVTotalDegree)
					{
					graph.setEdgeValue(i, m, 1);	
					graph.setEdgeValue(m, i, 1);
					graph.setNodeValue(i,nodeIDegree+1);
					nodeMDegree++;
					kTotalDegree=kTotalDegree+2;
					}
				}
			graph.setNodeValue(m,nodeMDegree);
			graph.setKTotal(kTotalDegree);
			m++;
			}
		keepUndirected(graph);
		}
	*/
		
	private void keepUndirected(Graph graph)
		{
		for (int i=0; i<graph.getNumNodes(); i++)
			for (int j=0; j<graph.getNumNodes(); j++)
				if (i>=j)
					graph.setEdgeValue(i, j, 0);
		}
	
	public void toConsole(Graph graph)
		{
		graph.toConsole();
		}
	
	public void toCSV(String pathNodes, String pathEdges, Graph graph)
		{
		graph.toCSV(pathNodes,pathEdges);	
		}

	}
	
	

