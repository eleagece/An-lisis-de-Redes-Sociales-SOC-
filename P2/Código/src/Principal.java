public class Principal 
	{
	public static void main(String[] args)
		{
		/*
		// Ejemplo de red aleatoria de 10 nodos totales y probabilidad 0,5 de unirse
		NetworkModel randomNetwork=new NetworkModel(NetworkModel.RANDOM,10,0.5,-1);
		randomNetwork.toConsole(randomNetwork.getGraph(NetworkModel.RANDOM));
		// Ejemplo de Red Libre de Escala construida Barabasi-Albert. 10 nodos totales y 4 nodos iniciales. El -1 siempre es -1
		NetworkModel baNetwork=new NetworkModel(NetworkModel.BARABASI_ALBERT,10,-1,3); 
		baNetwork.toConsole(baNetwork.getGraph(NetworkModel.BARABASI_ALBERT));
		*/
		// Con menú
		Menu menu=new Menu();
		menu.mainMenu();
		}
	}