public class Principal 
	{
	public static void main(String[] args)
		{
		//--------------------------------------------------------------------
		//-- ESTO ES UN EJEMPLO CON 10 PELÍCULAS METIDAS EN EL ARRAY data[] --
		//--------------------------------------------------------------------
		/*
		int numPelis=10;  // 10 películas de ejemplo
		int numActores=9;  // 9 actores de ejemplo
		GrafoPelicula grafoPelis=new GrafoPelicula(numPelis);  // Grafo de 'numPelis' películas relacionadas por actores
		GrafoActor grafoActores=new GrafoActor(numActores);  // Grafo de 'numActores' actores relacionados por películas
		XMLinCSVout reader=new XMLinCSVout(numPelis);  // Crea el array data[] de 'numPelis' vacío
		reader.obtenRAWdata();  // Mete los datos de los 10 películas en el array data[]
		reader.rellenaGrafoPelis(grafoPelis);  // Llena los datos de 'grafoPelis' con los datos obtenidos con obtenRAWdata
		reader.rellenaGrafoActores(grafoActores);  // Llena los datos de 'grafoActores' con los datos obtenidos con obtenRAWdata
		reader.toCSV(grafoPelis, grafoActores);  // Guarda en dos CSV los datos del grafo 'grafoPelis' y en dos CSV los datos del grafo 'grafoActores'
		*/
		//-------------------------------------------
		//-- ESTO ES SACANDO LAS PELÍCULAS DEL XML --
		//-------------------------------------------
		int numPelis=2500;  // Películas que voy a leer de los XML
		int numActores=7701;  // Actores que voy a leer de los XML
		GrafoPelicula grafoPelis=new GrafoPelicula(numPelis);  // Grafo de 'numPelis' películas relacionadas por actores
		GrafoActor grafoActores=new GrafoActor(numActores);  // Grafo de 'numActores' actores relacionados por películas
		XMLinCSVout reader=new XMLinCSVout(numPelis);  // Crea el array data[] de 'numPelis' vacío
		reader.obtenRAWdata();  // Obtiene los datos de los XML y los guarda para tratarlos luego
		reader.rellenaGrafoPelis(grafoPelis);  // Llena los datos de 'grafoPelis' con los datos obtenidos con obtenRAWdata
		reader.rellenaGrafoActores(grafoActores);  // Llena los datos de 'grafoActores' con los datos obtenidos con obtenRAWdata
		reader.toCSV(grafoPelis, grafoActores);  // Guarda en dos CSV los datos del grafo 'grafoPelis' y en dos CSV los datos del grafo 'grafoActores'
		}
	}