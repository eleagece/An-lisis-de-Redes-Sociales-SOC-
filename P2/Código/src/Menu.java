import java.text.DecimalFormat;
import java.util.Scanner;

public class Menu 
	{
	Scanner scanner;  // Sirve para recoger texto por consola
	int select;  // Opción elegida del usuario
	DecimalFormat maxDecimals;  // Nos dice el máximo de decimales que se muestran en p
 	
	/////////////////
	// Constructor //
	/////////////////
	public Menu()
		{
		scanner=new Scanner(System.in);
		select=-1;
		maxDecimals=new DecimalFormat("#.######");  
		}

	/////////////
	// Methods //
	/////////////
	public void mainMenu()
		{
		while(select!=0)
			{
			try
				{
				System.out.println("+------------- MENÚ PRINCIPAL ---------------+"+"\n"+
						   		   "| 1. Modelo de Red Aleatoria                 |"+"\n"+
						   		   "| 2. Modelo de Red Libre de Escala por B-A   |"+"\n"+
						   		   "| 0. Salir                                   |"+"\n"+
						   		   "+--------------------------------------------+");
				System.out.print(">>> ");
				select = Integer.parseInt(scanner.nextLine());
				switch(select)
					{
					case 1: 
						randomMenu();
						break;
					case 2: 
						barabasiAlbertMenu();
						break;
					case 0:
						System.out.println("¡Adiós!");
						System.exit(0);
						break;
					default:
						System.out.println("Número no reconocido");
						break;
					}
				System.out.println("\n");
				}
			catch(Exception e)
				{
				System.out.println("¡Error!");
				}
			}
		}
	
	public void randomMenu()
		{
		while(true)
			{
			try
				{
				System.out.println("+------------- MENÚ DE RED ALEATORIA ---------------+"+"\n"+
								   "| 1. Cargar el modelo de Red Aleatoria por defecto  |"+"\n"+
								   "| 2. Crear un modelo de Red Aleatoria               |"+"\n"+
								   "| 0. Volver al menú principal                       |"+"\n"+
								   "+---------------------------------------------------+");
				System.out.print(">>> ");
				select = Integer.parseInt(scanner.nextLine());
				switch(select)
					{
					case 1: 
						defaultRandomModel();
						break;
					case 2: 
						userRandomModel();
						break;
					case 0: 
						select=-1;
						mainMenu();
						break;
					default:
						System.out.println("Número no reconocido");
						break;
					}
				System.out.println("\n");
				}
			catch(Exception e)
				{
				System.out.println("¡Error!");
				}
			}
		}
	
	public void barabasiAlbertMenu() 
		{
		while(true)
			{
			try
				{
				System.out.println("+---------- MENÚ DE RED LIBRE DE ESCALA POR B-A ----------+"+"\n"+
								   "| 1. Cargar el modelo de Red Libre de Escala por defecto  |"+"\n"+
								   "| 2. Crear un modelo de Red Libre de Escala               |"+"\n"+
								   "| 0. Volver al menú principal                             |"+"\n"+
								   "+---------------------------------------------------------+");
				System.out.print(">>> ");
				select = Integer.parseInt(scanner.nextLine());
				switch(select)
					{
					case 1: 
						defaultBarabasiAlbertModel();
						break;
					case 2: 
						userBarabasiAlbertModel();
						break;
					case 0: 
						select=-1;
						mainMenu();
						break;
					default:
						System.out.println("Número no reconocido");
						break;
					}
				System.out.println("\n");
				}
			catch(Exception e)
				{
				System.out.println("¡Error!");
				}
			}
		}
	
	/*
	 * Cargamos los tres modelos aleatorios por defecto con N=500, N=1000 y N=5000
	 */
	public void defaultRandomModel()
		{
		System.out.println("+-------------- 1. RED ALEATORIA POR DEFECTO ---------------+"+"\n"+
				   		   "| En este modo se crearán las redes aleatorias pedidas en   |"+"\n"+
				   		   "| la práctica 2. 4 redes para N=500, 4 redes para N=1000 y  |"+"\n"+
				   		   "| 4 redes para N=5000. En total 12 redes. Las probabilida-  |"+"\n"+
				   		   "| des para las redes se pedirán por teclado. Los archivos   |"+"\n"+
				   		   "| .csv para Gephi se encuentran en c:/hlocal/               |"+"\n"+
				   		   "+-----------------------------------------------------------+");
		//int n1=10, n2=15, n3=20;
		int n1=500, n2=1000, n3=5000;
		loadRandomModel(n1);
		loadRandomModel(n2);
		loadRandomModel(n3);
		}

	/*
	 * Creamos un nuevo modelo aleatorio con N cualquiera
	 */	
	public void userRandomModel()
		{
		System.out.println("+-------------- 2. RED ALEATORIA ELEGIDA -------------------+"+"\n"+
		   		   		   "| En este modo se creará una red aleatoria en la que elegi- |"+"\n"+
		   		   		   "| remos el número de nodos N y las cuatro probabilidades    |"+"\n"+
		   		   		   "| para cada una de las etapas (subcrítica, crítica, super-  |"+"\n"+
		   		   		   "| crítica y conectada)                                      |"+"\n"+
		   		   		   "+-----------------------------------------------------------+");
		int n=-1;
		boolean selected=false;
		while (!selected)
			{
			System.out.print("Introduce el número de nodos (entre 1 y 5000) >>> ");
			n=Integer.parseInt(scanner.nextLine()); 
			if (n>0 && n<5001)
				{
				selected=true;
				}
			}
		loadRandomModel(n);
		}

	/*
	 * Cargamos los tres modelos libres de escala por defecto con N=500, N=1000 y N=5000
	 */
	public void defaultBarabasiAlbertModel()
		{
		System.out.println("+------- 1. RED LIBRE DE ESCALA POR BARABASI-ALBERT --------+"+"\n"+
				   		   "| En este modo se crearán las redes aleatorias pedidas en   |"+"\n"+
				   		   "| la práctica 2. 2 redes para N=500 con m=3 y m=4, 2 redes  |"+"\n"+
				   		   "| para N=1000 con m=3 y m=4, y 2 redes para N=5000 con m=3  |"+"\n"+
				   		   "| y m=4. En total 6 redes. Los archivos .csv para Gephi se  |"+"\n"+
				   		   "| encuentran en c:/hlocal/                                  |"+"\n"+
				   		   "+-----------------------------------------------------------+");
		//int n1=10, n2=15, n3=20;
		int n1=500, n2=1000, n3=5000;
		loadBarabasiAlbertModel(n1);
		loadBarabasiAlbertModel(n2);
		loadBarabasiAlbertModel(n3);
		}
	
	/*
	 * Creamos un nuevo modelo libre de escala con N y m cualquiera
	 */	
	public void userBarabasiAlbertModel()
		{
		System.out.println("+------------- 2. RED LIBRE DE ESCALA ELEGIDA --------------+"+"\n"+
		   		   		   "| En este modo se creará una red libre de escala siguiendo  |"+"\n"+
		   		   		   "| el algoritmo de Barabasi-Albert indicando un número de    |"+"\n"+
		   		   		   "| nodos totales (N) y un número de nodos iniciales (m)      |"+"\n"+
		   		   		   "+-----------------------------------------------------------+");
		int n=-1; int m=-1;
		boolean nSelected=false; boolean mSelected=false;
		while (!nSelected)
			{
			System.out.print("Introduce el número de nodos totales (14<N<10001) >>> ");
			n=Integer.parseInt(scanner.nextLine());
			if (n>14 && n<10001)
				{
				nSelected=true;
				}
			}
		while (!mSelected)
			{
			System.out.print("Introduce el número de nodos iniciales (2<m<11) >>> ");
			m=Integer.parseInt(scanner.nextLine());
			if (m>2 && m<11)
				{
				mSelected=true;
				}
			}
		NetworkModel barabasiAlbertNetwork=new NetworkModel(NetworkModel.BARABASI_ALBERT,n,-1,m);
		//barabasiAlbertNetwork.toConsole(barabasiAlbertNetwork.getGraph(NetworkModel.BARABASI_ALBERT));
		barabasiAlbertNetwork.toCSV("c:/hlocal/ba_"+n+"_"+m+"_nodes.csv","c:/hlocal/ba_"+n+"_"+m+"_edges.csv",barabasiAlbertNetwork.getGraph(NetworkModel.BARABASI_ALBERT));
		}
	
	/*
	 * Cargamos el modelo aleatorio para N=n y P=p1,p2,p3,p4
	 */
	public void loadRandomModel(int n)
		{
		double p1=etapaSubCritica(n);
		NetworkModel randomNetworkP1=new NetworkModel(NetworkModel.RANDOM,n,p1,0);
		//randomNetworkP1.toConsole(randomNetworkP1.getGraph(NetworkModel.RANDOM));
		randomNetworkP1.toCSV("c:/hlocal/random_"+n+"_01subcr_nodes.csv","c:/hlocal/random_"+n+"_01subcr_edges.csv",randomNetworkP1.getGraph(NetworkModel.RANDOM));
		
		double p2=etapaCritica(n);
		NetworkModel randomNetworkP2=new NetworkModel(NetworkModel.RANDOM,n,p2,0);
		//randomNetworkP2.toConsole(randomNetworkP2.getGraph(NetworkModel.RANDOM));
		randomNetworkP2.toCSV("c:/hlocal/random_"+n+"_02criti_nodes.csv","c:/hlocal/random_"+n+"_02criti_edges.csv",randomNetworkP2.getGraph(NetworkModel.RANDOM));
		
		double p3=etapaSuperCritica(n);
		NetworkModel randomNetworkP3=new NetworkModel(NetworkModel.RANDOM,n,p3,0);
		//randomNetworkP3.toConsole(randomNetworkP3.getGraph(NetworkModel.RANDOM));
		randomNetworkP3.toCSV("c:/hlocal/random_"+n+"_03supcr_nodes.csv","c:/hlocal/random_"+n+"_03supcr_edges.csv",randomNetworkP3.getGraph(NetworkModel.RANDOM));
		
		double p4 = etapaConectada(n);
		NetworkModel randomNetworkP4=new NetworkModel(NetworkModel.RANDOM,n,p4,0);
		//randomNetworkP4.toConsole(randomNetworkP4.getGraph(NetworkModel.RANDOM));
		randomNetworkP4.toCSV("c:/hlocal/random_"+n+"_04conec_nodes.csv","c:/hlocal/random_"+n+"_04subcr_edges.csv",randomNetworkP4.getGraph(NetworkModel.RANDOM));
		}

	/*
	 * Cargamos el modelo libre de escala para N=n y (m=3 y m=4)
	 */
	public void loadBarabasiAlbertModel(int n)
		{
		int m1=3;
		NetworkModel barabasiAlbertNetworkM1=new NetworkModel(NetworkModel.BARABASI_ALBERT,n,-1,m1);
		//barabasiAlbertNetworkM1.toConsole(barabasiAlbertNetworkM1.getGraph(NetworkModel.BARABASI_ALBERT));
		barabasiAlbertNetworkM1.toCSV("c:/hlocal/ba_"+n+"_"+m1+"_nodes.csv","c:/hlocal/ba_"+n+"_"+m1+"_edges.csv",barabasiAlbertNetworkM1.getGraph(NetworkModel.BARABASI_ALBERT));
		
		int m2=4;
		NetworkModel barabasiAlbertNetworkM2=new NetworkModel(NetworkModel.BARABASI_ALBERT,n,-1,m2);
		//barabasiAlbertNetworkM2.toConsole(barabasiAlbertNetworkM2.getGraph(NetworkModel.BARABASI_ALBERT));
		barabasiAlbertNetworkM2.toCSV("c:/hlocal/ba_"+n+"_"+m2+"_nodes.csv","c:/hlocal/ba_"+n+"_"+m2+"_edges.csv",barabasiAlbertNetworkM2.getGraph(NetworkModel.BARABASI_ALBERT));
		}
	
	/*
	 * Metodo que nos devuelve una probabilidad en funcion de la etapa subcritica
	 */
	public double etapaSubCritica(double n)
		{
		// Aquí sólo hay creados pares de enlaces. Mientras más acercampos p a 1/n
		// más componentes pequeñas aparecen y no hay un componente gigante definido
		double p=0.0;
		double t=1/n;
		System.out.println("*****************************************");
		System.out.println("---------- 1. ETAPA SUBCRÍTICA ----------");
		while(p>=t || p<=0)
			{
			System.out.println("Introduce p: 0 < p < "+maxDecimals.format(t));
			System.out.print  (">>> ");
			p=Double.parseDouble(scanner.nextLine());
			}
		System.out.println("-----------------------------------------");
		System.out.println("N="+n);
		System.out.println("p="+maxDecimals.format(p));
		System.out.println("*****************************************");
		return p;
		}
	
	/*
	 * Metodo que nos devuelve una probabilidad en funcion de la etapa Critica
	 */
	public double etapaCritica(double n)
		{
		// Es aquí donde hay un gran número de componentes pequeñas y la componente
		// gigante está a punto de aparecer por la conexión de varias de las peque-
		// ñas
		double p=(1/n);
		System.out.println("----------- 2. ETAPA CRÍTICA ------------");
		System.out.println("N="+n);
		System.out.println("p="+maxDecimals.format(p));
		System.out.println("-----------------------------------------");
		return p;
		}
	
	/*
	 * Metodo que nos devuelve una probabilidad en funcion de la etapa Super critica
	 */
	public double etapaSuperCritica(double n)
		{
		// Aquí es donde la componente gigante aparece claramente y va integrando
		// más y más componentes pequeñas
		double p=0.0;
		double t1=(1/n);
		double t2=(Math.log(n)/n);
		System.out.println("-------- 3. ETAPA SUPERCRÍTICA ----------");
		while(p<=t1 || p>=t2)
			{
			System.out.println("Introduce p: "+maxDecimals.format(t1)+" < p < "+maxDecimals.format(t2));
			System.out.print  (">>> ");
			p=Double.parseDouble(scanner.nextLine());
			}
		System.out.println("-----------------------------------------");
		System.out.println("N="+n);
		System.out.println("p="+maxDecimals.format(p));
		System.out.println("-----------------------------------------");
		return p;
		}
	
	/*
	 * Metodo que nos devuelve una probabildiad en funcion de la etapa conectada
	 */
	public double etapaConectada(double n)
		{
		// Aquí la componente gigante es única, lo que significa que todos los nodos
		// de la red están conectados por medio de algún camino
		double p=0.0;
		double t=(Math.log(n)/n);
		System.out.println("---------- 4. ETAPA CONECTADA -----------");
		while(p<t || p>=1)
			{
			System.out.println("Introduce p: "+maxDecimals.format(t)+" <= p < 1");
			System.out.print  (">>> ");
			p=Double.parseDouble(scanner.nextLine());
			}
		System.out.println("-----------------------------------------");
		System.out.println("N="+n);
		System.out.println("P="+maxDecimals.format(p));
		System.out.println("*****************************************");
		return p;
		}

	}