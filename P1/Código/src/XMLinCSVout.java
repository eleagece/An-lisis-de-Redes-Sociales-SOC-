import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Hashtable;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import org.jdom2.Attribute;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.util.IteratorIterable;

/* XMLinCSVout
 * 
 * Aqu� se guarda la informaci�n de una pel�cula seg�n se saca del XML. Tambi�n se
 * relaciona la informaci�n para crear el grafo de pel�culas y el grafo de actores.
 * Y finalmente se vuelca esa informaci�n de los grafos en archivos CSV.
 * 
 */

public class XMLinCSVout 
	{	
	// Atributos
	public RAWdata data[];  // Array de objetos RAWdata (cada objeto RAWdata es una peli y sus actores)
	public int numPelis;
	
	// Constructor
	public XMLinCSVout(int numPeliculas)
		{
		numPelis=numPeliculas;
		data=new RAWdata[numPeliculas];  // array 'data' con la informaci�n de pel�culas y actores en objetos RAWData
		}
	
	//------------------------------------
	//-- M�todos para leer archivos XML --
	//------------------------------------
	public void obtenRAWdata()
		{
		/*
		//--------------------------------------------------------------------
		//-- ESTO ES UN EJEMPLO CON 10 PEL�CULAS METIDAS EN EL ARRAY data[] --
		//--------------------------------------------------------------------
		// Primero los datos de las pel�culas
		for (int i=0; i<data.length; i++)
			{			
			data[i]=new RAWdata();
			data[i].pelicula=new Pelicula(i,"nombre"+i);
			data[i].actores=new ArrayList<Actor>();
			}
		// Despu�s los datos de los actores
		Actor actor1=new Actor(1,"uno");
		Actor actor2=new Actor(2,"dos");
		Actor actor3=new Actor(3,"tres");
		Actor actor4=new Actor(4,"cuatro");
		Actor actor5=new Actor(5,"cinco");
		Actor actor6=new Actor(6,"seis");
		Actor actor7=new Actor(7,"siete");
		Actor actor8=new Actor(8,"ocho");
		Actor actor9=new Actor(9,"nueve");
		data[0].actores.add(actor1);
		data[0].actores.add(actor2);
		data[0].actores.add(actor3);
		data[0].actores.add(actor4);
		//
		data[1].actores.add(actor7);
		//
		data[2].actores.add(actor5);
		data[2].actores.add(actor1);
		//
		data[3].actores.add(actor7);
		data[3].actores.add(actor8);
		//
		data[4].actores.add(actor6);
		data[4].actores.add(actor2);
		//
		data[5].actores.add(actor8);
		data[5].actores.add(actor9);
		data[5].actores.add(actor7);
		//
		data[6].actores.add(actor3);
		//
		data[7].actores.add(actor8);
		data[7].actores.add(actor7);
		data[7].actores.add(actor9);
		//
		data[8].actores.add(actor4);
		data[8].actores.add(actor2);
		//
		data[9].actores.add(actor7);
		*/
		
		//-------------------------------------------
		//-- ESTO ES SACANDO LAS PEL�CULAS DEL XML --
		//-------------------------------------------
		int i=0;  // �ndice donde colocaremos la pel�cula en el array
		int j=0;  // �ndice del xml correspondiente a la pel�cula
		while (i<numPelis)
			{
			String documentoPeliculaXML = "c:/materialPractica1/films/data.linkedmdb.org.data.film."+j+".xml";
			File xmlFile = new File(documentoPeliculaXML);
			if (xmlFile.exists())
				{
				data[i]=new RAWdata();
				data[i].actores=new ArrayList<Actor>();
				System.out.println("************************************************");
				System.out.println("           EMPIEZA PEL�CULA N�MERO: "+i);
				System.out.println("************************************************");
				rellena_data_i(xmlFile,i);
				i++;
				j++;
				}
			else
				{
				j++;
				}
			}
		}
	
	public boolean recorrerArbol(int posPelicula, Element etiqueta)
		{
		boolean encontrado = false;
		ArrayList<String> listaIdActores = new ArrayList<String>();  // id's de los actores
		String nombre = "";  // nombre de la pel�cula
		String idPelicula = null;  // id de la pel�cula
		String cadenaResource = null;
		List<Element> e = etiqueta.getChildren();
		for (int i = 0; i < e.size(); i++)
			{
			Element node = (Element) e.get(i);
			String film = node.getName();
			if(film == "film")
				{
				List<Element> nodoFilm = node.getChildren();
				for(int j = 0; j < nodoFilm.size(); j++)
					{
					Element nodePelicula = (Element) nodoFilm.get(j);
					String label = nodePelicula.getName();
					if(label=="title")
						nombre = nodePelicula.getText();
					if(label=="filmid")
						idPelicula = nodePelicula.getValue();
					if(label=="actor")
						{
						List<Attribute> atributos = nodePelicula.getAttributes();
						for(int t = 0; t < atributos.size(); t++)
							{
							Attribute atr = atributos.get(t);
							String nameAtributo = atr.getName();
							if(nameAtributo == "resource")
								{
								cadenaResource = atr.getValue();
								String idCadena = cadenaResource.substring(41, cadenaResource.length());
								listaIdActores.add(idCadena);
								}	
							}
						}	
					}
				// Rellenamos los datos de la pel�cula (id y nombre)
				data[posPelicula].pelicula=new Pelicula(Integer.parseInt(idPelicula),nombre);
				// Rellenamos los actores de la pel�cula (ArrayList de id y nombre)
				//System.out.println("************************************************");
				for (int j=0; j<listaIdActores.size();j++)
					{
					Actor actor=new Actor(Integer.parseInt(listaIdActores.get(j)),leerNombreActor(listaIdActores.get(j)));
					data[posPelicula].actores.add(actor);
					System.out.println("Actor: "+ actor.nombre +"");
					}
				System.out.println("Pel�cula: "+ nombre +"");
				//System.out.println("************************************************");
				encontrado = true;
				}
			else
				recorrerArbol(posPelicula, node);
			}
		return encontrado;
		}	
	
	// Extrae del XML de pel�cula el t�tulo y el id de la pel�cula y de los XML de los actores de la pel�cula
	// tambi�n su id y nombre
	public void rellena_data_i(File nombreArchivo, int posPelicula)
		{
		SAXBuilder builder = new SAXBuilder();
		try {
			Document document = (Document) builder.build(nombreArchivo);
			Element rootNode = document.getRootElement();
			recorrerArbol(posPelicula, rootNode);
			}
		catch (JDOMException e)
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		catch (IOException e) 
			{
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		
	// Extrae del XML del actor su nombre a partir de su id
	public String leerNombreActor(String id)
		{
		String nombreActor = null;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File("c:/materialPractica1/actors/data.linkedmdb.org.data.actor."+ id +".xml");
		if(xmlFile.exists())
			{
			try 
				{
				Document document = (Document) builder.build(xmlFile);
				Element rootNode = document.getRootElement();
				IteratorIterable<Content> descendantsOfXML = rootNode.getDescendants();
				for (Content descendant : descendantsOfXML) 
					{
		            if (descendant.getCType().equals(Content.CType.Element)) 
		            	{
		                Element element = (Element) descendant;
		                if (element.getName().equals("actor_name")) 
		                	{
		                	List<Content> contenido = element.getContent();
		                	for(int j = 0; j < contenido.size(); j++)
		                		{
		                		Content atr = contenido.get(j);
		                		//System.out.println(atr.getValue());	
		                		nombreActor = atr.getValue();
		                		}
		                	}
		            	}
					}
				} 
			catch (IOException io) 
				{
				System.out.println(io.getMessage());
				} 
			catch (JDOMException jdomex) 
				{
				System.out.println(jdomex.getMessage());
				}
			}
		return nombreActor;
		}
		
	//---------------------------------------------------------
	//-- M�todos para rellenar las estructuras de los grafos --
	//---------------------------------------------------------
	public void rellenaGrafoPelis(GrafoPelicula grafoPelis)
		{
		// Aqu� vamos llenando la estructura de 'grafoPelis' con la informaci�n que tenemos en el array 'data'
		for (int i=0; i<grafoPelis.numPeliculas; i++)  // Recorro todas las pel�culas...
			{
			grafoPelis.peliculas[i]=new Pelicula(data[i].pelicula.id,data[i].pelicula.nombre);  // ...y las creo con su 'id' y 'nombre'
			for (int j=i+1; j<grafoPelis.numPeliculas; j++)  // Comparo todas las pel�culas que recorro con sus siguientes...
				{
				comprobarActoresRelacionados(i,j,grafoPelis);  // ...viendo si comparten actores y actualizando 'grafoPelis' en consecuencia
				}
			}
		}
	
	public void comprobarActoresRelacionados(int i, int j, GrafoPelicula grafoPelis)
		{
		for (int buscoEn=j; buscoEn<grafoPelis.numPeliculas; buscoEn++)  // Recorro pel�culas desde 'i+1' hasta el final comparando sus actores con los de 'i'
			{
			// En 'data[i].actores' tengo los actores de la pel�cula 'i' que voy a comparar con los actores de las siguientes pel�culas
			// En 'data[buscoEn].actores' tengo los actores de la pel�cula 'buscoEn' que es una de las siguientes pel�culas a la pel�cula 'i'
			int numActoresI=data[i].actores.size();  // El n�mero de actores de la pel�cula 'i'
			int numActoresBuscoEn=data[buscoEn].actores.size();  // El n�mero de actores de la pel�cula 'buscoEn'
			boolean match=false;
			int k=0; int l=0; 
			while (k<numActoresI && !match)
				{
				while (l<numActoresBuscoEn && !match)
					{
					if ((data[i].actores.get(k).id)==(data[buscoEn].actores.get(l).id))
						{
						match=true;
						System.out.println("Relaci�n entre "+i+" y "+buscoEn+" con actores "+data[i].actores.get(k).id+" y "+data[buscoEn].actores.get(l).id);
						}
					else
						l++;
					}
				l=0;
				k++;
				}
			grafoPelis.setPosicionMatriz(match,i,buscoEn);
			}
		}
	
	public void rellenaGrafoActores(GrafoActor grafoActores)
		{
		// Aqu� vamos llenando la estructura de 'grafoActores' con la informaci�n que tenemos en el array 'data'
		Hashtable<Integer,Integer> traductor=new Hashtable<Integer,Integer>();  // Tabla de equivalencia de idOriginal e idNuevo
		int indiceActores=0;  // Posici�n donde guardaremos cada actor
		for (int i=0; i<data.length; i++)  // Recorro todas las pel�culas (�ndice i)...
			{
			// Inserci�n de nuevos actores encontrados en esta pel�cula
			for (int j=0; j<data[i].actores.size(); j++)  // ...accedo a cada actor de la pel�cula secuencialmente (�ndice j)...
				{
				int comprobando=data[i].actores.get(j).id;  // ...y obtengo el 'id' original del actor en ese �ndice
				if (!traductor.containsKey(comprobando))  // Si no ha sido inscrito previamente en la hash...
					{
					traductor.put(comprobando,indiceActores);  // ...lo inserto en la hash con clave 'comprobando' (idOriginal) y valor 'indiceActores' (idNuevo)... 
					grafoActores.actores[indiceActores]=data[i].actores.get(j);  // ... y lo inserto en grafoActores.actor[indiceActores]...
					indiceActores++;  //...y hago indiceActores++
					}
				}
			// Relaci�n de los actores de esta pel�cula
			for (int k=0; k<data[i].actores.size(); k++)  // Comparo todos los actores de la pel�cula entre s�
				{
				for (int l=k+1; l<data[i].actores.size(); l++)
					{
					grafoActores.setPosicionMatriz(true,traductor.get((data[i].actores.get(k).id)),traductor.get((data[i].actores.get(l).id)));
					grafoActores.setPosicionMatriz(true,traductor.get((data[i].actores.get(l).id)),traductor.get((data[i].actores.get(k).id)));
					}
				}
			}
		grafoActores.limpiaDuplicados();  // Limpia los duplicados de la matriz por ser grafo no dirigido
		}
	
	//-------------------------------------------
	//-- M�todos para escribir en archivos CSV --
	//-------------------------------------------
	public void toCSV(GrafoPelicula grafoPelis, GrafoActor grafoActores)  
	 	{
		try 
			{
	        CSVFormat csvSalto=CSVFormat.DEFAULT.withRecordSeparator("\n");  // Indica el formato del salto de l�nea en el CSV
	        Object [] cabeceraNodos={"Id","Label"};  // Cabecera del CSV de nodos
	        Object [] cabeceraAristas={"Source","Target","Type","Id","Label","Weight"};  // Cabecera del CSV de aristas
	        //------------------------------------------
			//-- Escribir CSVs del grafo de pel�culas --
			//------------------------------------------
			// Se escribe el archivo de Nodos de Pel�culas
			FileWriter archivoNodosPelis=new FileWriter("c:/PeliculasNodos.csv");  // Archivo en el que se va a escribir
	        CSVPrinter printerNodosPelis=new CSVPrinter(archivoNodosPelis,csvSalto);  // Objeto para imprimir en el CSV
	        printerNodosPelis.printRecord(cabeceraNodos);
	        for (int i=0; i<grafoPelis.numPeliculas; i++)
	        	{
	        	ArrayList datosFilaNodosPelis=new ArrayList();
	        	datosFilaNodosPelis.add(i);  // Id
	        	datosFilaNodosPelis.add(grafoPelis.peliculas[i].nombre);  // Label
		        printerNodosPelis.printRecord(datosFilaNodosPelis);
		        }
	        archivoNodosPelis.flush();
	        archivoNodosPelis.close();
	        printerNodosPelis.close();
			// Se escribe el archivo de Aristas de Pel�culas
			FileWriter archivoAristasPelis=new FileWriter("c:/PeliculasAristas.csv");  // Archivo en el que se va a escribir
	        CSVPrinter printerAristasPelis=new CSVPrinter(archivoAristasPelis,csvSalto);  // Objeto para imprimir en el CSV
	        printerAristasPelis.printRecord(cabeceraAristas);
	        int id=0;
	        for (int i=0; i<grafoPelis.numPeliculas; i++)
	        	{
	        	for (int j=0; j<grafoPelis.numPeliculas; j++)
	        		{
	        		if (grafoPelis.relacionPeliculas[i][j]==1)
		        		{
			    	    ArrayList datosFilaAristasPelis=new ArrayList();
				        datosFilaAristasPelis.add(i);  // Source
				        datosFilaAristasPelis.add(j);  // Target
				        datosFilaAristasPelis.add("Undirected");  // Type
				        datosFilaAristasPelis.add(id);  // Id (se calcula secuencialmente cada vez que se a�ade una arista)
				        datosFilaAristasPelis.add("");  // Label (las aristas no tienen nombre)
				        datosFilaAristasPelis.add(1.0);	 // Weight
				        printerAristasPelis.printRecord(datosFilaAristasPelis);
		        		id++;
		        		}
	        		}
		        }
	        archivoAristasPelis.flush();
	        archivoAristasPelis.close();
	        printerAristasPelis.close();
	        //----------------------------------------
	        //-- Escribir CSVs del grafo de actores --
	        //----------------------------------------
	        // Se escribe en el archivo de Nodos de Actores
	        FileWriter archivoNodosActores=new FileWriter("c:/ActoresNodos.csv");  // Archivo en el que se va a escribir
	        CSVPrinter printerNodosActores=new CSVPrinter(archivoNodosActores,csvSalto);  // Objeto para imprimir en el CSV
	        printerNodosActores.printRecord(cabeceraNodos);
	        for (int i=0; i<(grafoActores.numActores); i++)
	        	{
	        	ArrayList datosFilaNodosActores=new ArrayList();
	        	datosFilaNodosActores.add(i);  // Id
	        	datosFilaNodosActores.add(grafoActores.actores[i].nombre);  // Label
	        	printerNodosActores.printRecord(datosFilaNodosActores);
		        }
	        archivoNodosActores.flush();
	        archivoNodosActores.close();
	        printerNodosActores.close();
	        // Se escribe en el archivo de Aristas de Actores
	        FileWriter archivoAristasActores=new FileWriter("c:/ActoresAristas.csv");  // Archivo en el que se va a escribir
	        CSVPrinter printerAristasActores=new CSVPrinter(archivoAristasActores,csvSalto);  // Objeto para imprimir en el CSV
	        printerAristasActores.printRecord(cabeceraAristas);
	        id=0;
	        for (int i=0; i<(grafoActores.numActores); i++)
	        	{
	        	for (int j=0; j<(grafoActores.numActores); j++)
	        		{
	        		if (grafoActores.relacionActores[i][j]==1)
		        		{
			    	    ArrayList datosFilaAristasActores=new ArrayList();
			    	    datosFilaAristasActores.add(i);  // Source
			    	    datosFilaAristasActores.add(j);  // Target
			    	    datosFilaAristasActores.add("Undirected");  // Type
			    	    datosFilaAristasActores.add(id);  // Id (se calcula secuencialmente cada vez que se a�ade una arista)
			    	    datosFilaAristasActores.add("");  // Label (las aristas no tienen nombre)
			    	    datosFilaAristasActores.add(1.0);	 // Weight
			    	    printerAristasActores.printRecord(datosFilaAristasActores);
		        		id++;
		        		}
	        		}
		        }
	        archivoAristasActores.flush();
	        archivoAristasActores.close();
	        printerAristasActores.close();
			} 
		catch (IOException e) 
			{
			e.printStackTrace();
			}
	    }  
	
	}
