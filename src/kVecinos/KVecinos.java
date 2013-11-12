package kVecinos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class KVecinos {
	
	public static Candidato vecinos[];
	public static Integer nVecinos;
	
	public static void checkKVecinos(Candidato candidato){
		for(int i=0; i< nVecinos; i++){
			if(candidato.getDistancia() < vecinos[i].getDistancia()){
				Candidato aux = vecinos[i];
				Candidato aux2;
				vecinos[i] = candidato;
				for(int j=i+1; j<nVecinos; j++){
					aux2 = vecinos[j];
					vecinos[j]= aux;
					aux=aux2;
				}
				break;
			}
		}
	}
	
	public static void printKVecinos(){
		for(int i=0; i<nVecinos; i++){
			if(vecinos[i] != null)
				System.out.println(vecinos[i].getDistancia());
		}
	}
	
	public static void inicializarKVecinos(){
		vecinos = new Candidato[nVecinos];
		// Inicializo el vector
		for(int i=0; i<nVecinos; i++){
			vecinos[i] = new Candidato("null", Integer.MAX_VALUE);
		}
	}
	
	// Esto está por implementar
	public static Character getMejorCandidatoSumando(){
		HashMap<Character, Integer> etiquetas = new HashMap<>();
		for(int i=0; i<nVecinos; i++){
			if(etiquetas.containsKey(vecinos[i].getEtiqueta())){
				
			}
		}
		return 'A';
	}
	
	public static String getMejorCandidatoPonderando(){
		ArrayList<Candidato> mejores = new ArrayList<>();
		for(Candidato v : vecinos){
			boolean insertada = false;
			for(Candidato m : mejores){				
				if(m.getEtiqueta() == v.getEtiqueta()){
					m.setDistancia(m.getDistancia()+(int)((1/(double)v.getDistancia())*1000));
					insertada = true;
					break;
				}				
			}
			if(!insertada)
				mejores.add(new Candidato(v.getEtiqueta(), (int)((1/(double)v.getDistancia())*1000)));
		}
//		for(Candidato c : mejores)
//			System.out.println("Miro el candidato con distancia: "+c.getDistancia()+" y etiqueta "+c.getEtiqueta());
		Collections.sort(mejores);
		
		return mejores.get(0).getEtiqueta();
	}
	
	public static void crearArchivosEquilibrados(){
		HashMap<Character, ArrayList<String>> mapaco = new HashMap<Character, ArrayList<String>>();
		Character inicial = 'A';
		// Inicializo el hashmap
		for(int i=0; i<26; i++){
			ArrayList<String> letra = new ArrayList<>();
			mapaco.put(inicial, letra);
			System.out.println(inicial);
			inicial++;
		}
		try {
			// Leo y meto (no es un chiste sobre leo)
			BufferedReader br1 = new BufferedReader(new FileReader(new File("Training.200.cad")));
			String line = br1.readLine();
			while(line != null){
				try {
					System.out.println(line.charAt(0));
					mapaco.get(line.charAt(0)).add(line);
				} catch (Exception ex){
				}
				line = br1.readLine();
			}
			br1.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Escritura de los archivos
		System.out.println("Vamos a hacer archivos!!!!!");
		Character ini = 'A';
		try{
			for(int f=0; f<5; f++){
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File("salida"+f+".txt")));
				ini = 'A';
				for(int i=0; i<26; i++){
					for(int j=0; j<40; j++){
						System.out.println(mapaco.get(ini).get(0));
						bw.write(mapaco.get(ini).get(0)+"\n");
						mapaco.get(ini).remove(0);
					}
					ini++;
				}
				bw.close();
			}
		} catch (Exception ex){
			System.out.println(ex.getMessage());
		}		
	}
	
	/**
	 * Calcular las distancias entre el test y el training usando el k-vecino más cercano
	 * @param ficheroTest
	 * @param ficheroTraining
	 * @return
	 */
	public static HashMap<Character, Integer> kVecinosMasCercanos(String ficheroTest, String ficheroTraining){
		HashMap<Character, Integer> mapaco = new HashMap<>();
		// Inicializo el vector de vecinos
		
		try {
			Character letra = 'A';
			// Inicializo el map a 0;
			for(int i=0; i<26; i++){
				mapaco.put(letra, 0);
				letra++;
			}		
			
			BufferedReader brTraining = new BufferedReader(new FileReader(new File(ficheroTraining)));
			BufferedReader brTest = new BufferedReader(new FileReader(new File(ficheroTest)));
//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("salida.txt")));
			ArrayList<String> lTraining = new ArrayList<>();
			ArrayList<String> lTest = new ArrayList<>();
			String line = brTraining.readLine();
			while(line != null){
				lTraining.add(line);
				line = brTraining.readLine();
			}
			line = brTest.readLine();
			while(line != null){
				lTest.add(line);
				line = brTest.readLine();
			}
			String mejorEtiqueta = "null";
			for(String item : lTest){
				inicializarKVecinos();
				for(String item2 : lTraining){
					int distancia = Levenshtein.computeLevenshteinDistance(item.split(" ")[1], item2.split(" ")[1]);
					Candidato candidato = new Candidato(item2.split(" ")[0], distancia);
					checkKVecinos(candidato);					
				}
				mejorEtiqueta = getMejorCandidatoPonderando();
				System.out.println("Este tiene la etiqueta " + item.split(" ")[0] + " y la mínima es " + mejorEtiqueta);
				if(item.split(" ")[0].equals(mejorEtiqueta)){
					mapaco.put(item.split(" ")[0].charAt(0), mapaco.get(item.split(" ")[0].charAt(0))+1);
//					System.out.println("La etiqueta es buena");
				}
			}
//			System.out.println("El índice de acierto es de "+ (double)aciertos/lTest.size()*100.0);
			brTraining.close();
			brTest.close();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapaco;
		
	}
	
	/**
	 * Calcular las distancias entre el test y el training usando el vecino más cercano
	 * @param ficheroTest
	 * @param ficheroTraining
	 */
	public static HashMap<Character, Integer> vecinoMasCercano(String ficheroTest, String ficheroTraining){
		HashMap<Character, Integer> mapaco = new HashMap<>();
		try {
			Character letra = 'A';
			// Inicializo el map a 0;
			for(int i=0; i<26; i++){
				mapaco.put(letra, 0);
				letra++;
			}				
			
			BufferedReader brTraining = new BufferedReader(new FileReader(new File(ficheroTraining)));
			BufferedReader brTest = new BufferedReader(new FileReader(new File(ficheroTest)));
//			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("salida.txt")));
			ArrayList<String> lTraining = new ArrayList<>();
			ArrayList<String> lTest = new ArrayList<>();
			String line = brTraining.readLine();
			while(line != null){
				lTraining.add(line);
				line = brTraining.readLine();
			}
			line = brTest.readLine();
			while(line != null){
				lTest.add(line);
				line = brTest.readLine();
			}
			int distanciaMin = Integer.MAX_VALUE;
			String etiquetaMin = "";
			for(String item : lTest){
				distanciaMin = Integer.MAX_VALUE;
				for(String item2 : lTraining){
					int distancia = Levenshtein.computeLevenshteinDistance(item.split(" ")[1], item2.split(" ")[1]);
					if(distancia < distanciaMin){
						distanciaMin = distancia;
						etiquetaMin = item2.split(" ")[0];
					}					
				}
				System.out.println("Este tiene la etiqueta " + item.split(" ")[0] + " y la mínima es " + etiquetaMin);
				if(item.split(" ")[0].equals(etiquetaMin))
					mapaco.put(item.split(" ")[0].charAt(0), mapaco.get(item.split(" ")[0].charAt(0))+1);
			}
//			System.out.println("El índice de acierto es de "+ (double)aciertos/lTest.size()*100.0);
			brTraining.close();
			brTest.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapaco;
	}

	public static void main(String[] args) throws IOException {
		
		PruebasConDosAlgoritmosBasica();		
//		PruebasConTodoVecinoMasCercano();
//		PruebasConTodoKVecinos();
		
	}

	private static void PruebasConDosAlgoritmosBasica() throws IOException {
		BufferedWriter resultado;
		resultado = new BufferedWriter(new FileWriter(new File("resultado.csv")));
//		crearArchivosEquilibrados();
		
		nVecinos = 7;
		
		HashMap<Character, Integer> mapacoCercano = vecinoMasCercano("salida0Mini.txt", "salida1Mini.txt");
		HashMap<Character, Integer> mapacoKCercanos = kVecinosMasCercanos("salida0Mini.txt", "salida1Mini.txt");
		
		resultado.write("Data sets,Algorithm 1,Algorithm 2\n");
		
		char letra = 'A';
		for(int i=0; i<26; i++){
			System.out.println("Para la "+ letra +":");
			System.out.println("Con el más cercano tengo: " + mapacoKCercanos.get(letra));
			System.out.println("Con el k vecinos: " + mapacoCercano.get(letra));
			resultado.write(letra+","+mapacoCercano.get(letra)+","+mapacoCercano.get(letra)+"\n");
			letra++;
		}
		resultado.close();
	}

	/**
	 * 
	 */
	private static void PruebasDeKVecinos() {
		inicializarKVecinos();
		// Hago una pruebecica
		checkKVecinos(new Candidato("A", 10));
		checkKVecinos(new Candidato("A", 5));
		checkKVecinos(new Candidato("A", 2));
		checkKVecinos(new Candidato("A", 44));
		checkKVecinos(new Candidato("A", 22));
		checkKVecinos(new Candidato("A", 1));
		checkKVecinos(new Candidato("A", 23));
		printKVecinos();
	}

	/**
	 * 
	 */
	private static void PruebasConTodoVecinoMasCercano() {
		Character letra = 'A';
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(new File("resultados.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<HashMap<Character, Integer>> listaAciertosParciales = new ArrayList<>();
		// Dibujo la fila de arriba de la tabla e inicializo los aciertos globales
		for(int i=0; i<26; i++){
			if(i<25){
				System.out.print(letra+"\t");
				try {
					bw.write(letra+"\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{
				System.out.print(letra+"\n");
				try {
					bw.write(letra+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			letra++;
		}
		
		for(int j=0; j<5; j++){
//			System.out.println("Usando archivo test: salida"+j+".txt");
			HashMap<Character, Integer> aciertosGlobales = new HashMap<>();
			// Inicializo los globales
			letra = 'A';
			for(int i=0; i<26; i++){
				aciertosGlobales.put(letra, 0);
				letra++;
			}
			for(int i=0; i<5; i++){
				if(i!=j){
//					System.out.println("Procesando fichero: salida"+i+".txt");
					HashMap<Character, Integer> aciertos = kVecinosMasCercanos("salida"+i+".txt", "salida"+j+".txt");
					letra = 'A';
					for(int k=0; k<26; k++){
						Integer parcial = aciertos.get(letra);
						aciertosGlobales.put(letra, aciertosGlobales.get(letra)+parcial);
						letra++;
					}
					listaAciertosParciales.add(aciertos);
				}
			}
			letra = 'A';
			for(int i=0; i<26; i++){
				if(i<25){
					System.out.print(aciertosGlobales.get(letra)+"\t");
					try {
						bw.write(aciertosGlobales.get(letra)+"\t");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.print(aciertosGlobales.get(letra)+"\n");
					try {
						bw.write(aciertosGlobales.get(letra)+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				letra++;
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	private static void PruebasConTodoKVecinos(){
		Character letra = 'A';
		BufferedWriter bw = null;
		
		try {
			bw = new BufferedWriter(new FileWriter(new File("resultados.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<HashMap<Character, Integer>> listaAciertosParciales = new ArrayList<>();
		// Dibujo la fila de arriba de la tabla e inicializo los aciertos globales
		for(int i=0; i<26; i++){
			if(i<25){
				System.out.print(letra+"\t");
				try {
					bw.write(letra+"\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else{
				System.out.print(letra+"\n");
				try {
					bw.write(letra+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			letra++;
		}
		
		try {
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int j=0; j<5; j++){
//				System.out.println("Usando archivo test: salida"+j+".txt");
			HashMap<Character, Integer> aciertosGlobales = new HashMap<>();
			// Inicializo los globales
			letra = 'A';
			for(int i=0; i<26; i++){
				aciertosGlobales.put(letra, 0);
				letra++;
			}
			for(int i=0; i<5; i++){
				if(i!=j){
//						System.out.println("Procesando fichero: salida"+i+".txt");
					HashMap<Character, Integer> aciertos = vecinoMasCercano("salida"+i+".txt", "salida"+j+".txt");
					letra = 'A';
					for(int k=0; k<26; k++){
						Integer parcial = aciertos.get(letra);
						aciertosGlobales.put(letra, aciertosGlobales.get(letra)+parcial);
						letra++;
					}
					listaAciertosParciales.add(aciertos);
				}
			}
			
			try {
				bw = new BufferedWriter(new FileWriter(new File("resultados.txt")));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			letra = 'A';
			for(int i=0; i<26; i++){
				if(i<25){
					System.out.print(aciertosGlobales.get(letra)+"\t");
					try {
						bw.write(aciertosGlobales.get(letra)+"\t");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					System.out.print(aciertosGlobales.get(letra)+"\n");
					try {
						bw.write(aciertosGlobales.get(letra)+"\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				letra++;
			}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
