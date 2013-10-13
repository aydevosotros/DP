package kVecinos;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	public static void CalculoDistanciaMinima(String ficheroTest, String ficheroTraining){
		try {
			BufferedReader brTraining = new BufferedReader(new FileReader(new File("training.cad")));
			BufferedReader brTest = new BufferedReader(new FileReader(new File("test.cad")));
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
			System.out.println(lTraining.size());
			System.out.println(lTest.size());
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
					aciertos++;
			}
			System.out.println("El índice de acierto es de "+ (double)aciertos/lTest.size()*100.0);
			brTraining.close();
			brTest.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		
//		crearArchivosEquilibrados();
		
		for(int i=0; i<4; i++){
			CalculoDistanciaMinima("salida"+(i+1)+".txt", "0");
		}
	}

}
