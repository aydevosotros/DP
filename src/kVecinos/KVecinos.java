package kVecinos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KVecinos {
	
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
			// TODO Auto-generated catch block
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
	 * Calcular las distancias entre el test y el training
	 * @param ficheroTest
	 * @param ficheroTraining
	 */
	public static HashMap<Character, Integer> vecinoMasCercano(String ficheroTest, String ficheroTraining){
		HashMap<Character, Integer> mapaco = new HashMap<>();
		try {
			Character letra = 'A';
			// Inicializo el map a 0;
			for(int i=0; i<26; i++){
//				Integer current = mapaco.get(letra);
//				current++;
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
			int aciertos=0;
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
//				System.out.println("Este tiene la etiqueta " + item.split(" ")[0] + " y la mínima es " + etiquetaMin);
				if(item.split(" ")[0].equals(etiquetaMin))
					mapaco.put(item.split(" ")[0].charAt(0), mapaco.get(item.split(" ")[0].charAt(0))+1);
			}
//			System.out.println("El índice de acierto es de "+ (double)aciertos/lTest.size()*100.0);
			brTraining.close();
			brTest.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapaco;
	}

	public static void main(String[] args) {
//		crearArchivosEquilibrados();
		HashMap<Character, Integer> aciertosGlobales = new HashMap<>();
		ArrayList<HashMap<Character, Integer>> listaAciertosParciales = new ArrayList<>();
		for(int i=0; i<4; i++){
			System.out.println("Procesando fichero: salida"+(i+1)+".txt");
			HashMap<Character, Integer> aciertos = vecinoMasCercano("salida"+(i+1)+".txt", "salida0.txt");
			Character letra = 'A';
			for(int j=0; j<26; j++){
				Integer parcial = aciertos.get(letra);
				if(i!=0)
					aciertosGlobales.put(letra, aciertosGlobales.get(letra)+parcial);
				else aciertosGlobales.put(letra, parcial);
				letra++;
			}
			listaAciertosParciales.add(aciertos);
			
		}
		Character letra = 'A';
		for(int i=0; i<26; i++){
			if(i<25)
				System.out.print(letra+"\t");
			else System.out.print(letra+"\n");
			letra++;
		}
		letra = 'A';
		for(int i=0; i<26; i++){
			if(i<25)
				System.out.print(aciertosGlobales.get(letra)+"\t");
			else System.out.print(aciertosGlobales.get(letra)+"\n");
			letra++;
		}
		
	}

}
