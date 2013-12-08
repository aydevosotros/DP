package AA;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

public class P102 {

	private kNN knn;

	/**
	 * Qué va a puntuar: -Va a mirar el docs a la par que el código para
	 * corroborar lo que hemos hecho -El estará 10 minutos con cada uno pero
	 * solo 5 podremos exponer lo hecho -Tendremos que almacenar en disco todo
	 * lo requerido para clasificar una nueva entrada
	 */

	public static void main(String[] args) throws IOException {

		P102 pr = new P102();
		// Aquí están diferentes pruebas. Entrad en los métodos y echar un
		// vistazo. Son muy fáciles.

		BufferedWriter resultado;
		resultado = new BufferedWriter(
				new FileWriter(new File("resultado.csv")));
		// crearArchivosEquilibrados();
		
		
		//1NN ha acertado un 88.36538
		//kNN (3) ha acertado un 88.65385

//		ArrayList<HashMap<Character, Integer>> mapacoCercano = new ArrayList<>();
		ArrayList<HashMap<Character, Integer>> mapacoKCercanos = new ArrayList<>();
		
		//Valor de las k a probar
//		int test1 = 1;
		int test2 = 3;
		
		
		for(int i = 0; i < 5; i++)
		{
//			pr.setNumVecinosCercanos(test1);
//			mapacoCercano.add(pr.kVecinosMasCercanos("salida" + i + ".txt"));
			pr.setNumVecinosCercanos(test2);
			mapacoKCercanos.add(pr.kVecinosMasCercanos("salida" + i + ".txt"));
		}

		resultado.write("Data sets,Algorithm 1,Algorithm 2\n");

		char letra = 'A';
		int aciertosTotales1 = 0;
		int aciertosTotales2 = 0;
		for (int i = 0; i < 26; i++) {
			int aciertosLetra1 = 0;
			int aciertosLetra2 = 0;

			for(int j = 0; j < 5; j++){
//				aciertosLetra1 += mapacoCercano.get(j).get(letra);
				aciertosLetra2 += mapacoKCercanos.get(j).get(letra);
			}
			aciertosTotales1 += aciertosLetra1;
			aciertosTotales2 += aciertosLetra2;
			System.out.println("Para la " + letra + ":");
//			System.out.println("Con el " + test1 + " vecinos tengo: "	+ aciertosLetra1);
			System.out.println("Con el " + test2 + " vecinos tengo: "	+ aciertosLetra2);
			resultado.write(letra + "," + aciertosLetra1 + ","+ aciertosLetra2 + "\n");
			letra++;
		}
//		System.out.println("El " + test1 + " vecinos tengo un porcentaje de acierto de: "	+ ((float)aciertosTotales1/5200.0)*100.0);
		System.out.println("El " + test2 + " vecinos tengo un porcentaje de acierto de: "	+ ((float)aciertosTotales2/5200.0)*100.0);
		resultado.close();

	}

	P102() {
		knn = new kNN();
	}
	
	void setNumVecinosCercanos(int nvecinos){
		knn.setnVecinos(nvecinos);
	}

	void ExportTrainingSet(ArrayList<String> T) {
		try {
			OutputStream file = new FileOutputStream("trainingSet.ser");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);

			output.writeObject(T);
			output.close();
		} catch (Exception ex) {

		}

	}

	ArrayList<String> ImportTrainingSet() {
		try {
			InputStream file = new FileInputStream("trainingSet.ser");
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream(buffer);
			@SuppressWarnings("unchecked")
			ArrayList<String> tmp = (ArrayList<String>) input.readObject();
			input.close();
			return tmp;
		} catch (Exception ex) {

		}
		return null;
	}

	ArrayList<String> Editing(int k, ArrayList<String> T) { // Training set
		ArrayList<String> S = new ArrayList<String>(T), // Edited set
		R = new ArrayList<String>(); // Misclassified set
		knn.setTrainingSet(S);
		for (String p : S) {
			if (p.charAt(0) != getContourClass(p)) { // Misclassified example
				R.add(p);
				// Remove example
			}
		} // for
		S.removeAll(R);
		// Remove all misclassified examples
		return S;
	}
	
	ArrayList<String> MultiEdit(int k, ArrayList<String> T) { // Training set
		ArrayList<String> S = new ArrayList<String>(T); // Edited set
		ArrayList<String> R;
		do{
			R = new ArrayList<String>(); // Misclassified set
			knn.setTrainingSet(S);
			for (String p : S) {
				if (p.charAt(0) != getContourClass(p)) { // Misclassified example
					R.add(p);
					// Remove example
				}
			} // for
			S.removeAll(R);
			// Remove all misclassified examples
		}while(!R.isEmpty());
		return S;
	}

	ArrayList<String> CNN(int k, ArrayList<String> T) { // Traning set
		ArrayList<String> S = new ArrayList<String>(); // CNN set
		boolean updated;
		Collections.shuffle(T); // Shuffle array elements
		do {
			updated = false;
			knn.setTrainingSet(T);
			knn.setnVecinos(k);
			for (String p : T) {
				if (p.charAt(0) != getContourClass(p)) { // Misclassified
															// example
					S.add(p);
					// It ’s needed
					updated = true;
				}
			}
		} while (S.size() < T.size() && updated);

		return S;
	}

	public char getContourClass(String example) {

		knn.inicializarKVecinos();
		for (String item2 : knn.getTrainingSet()) {
			int distancia = Levenshtein.computeLevenshteinDistance(
					example.split(" ")[1], item2.split(" ")[1]);
			Candidato candidato = new Candidato(item2.split(" ")[0], distancia);
			knn.checkKVecinos(candidato);
		}
		return knn.getMejorCandidatoPonderando().charAt(0);
	}

	public void createFiveFoldCrossValidation() {

		HashMap<Character, ArrayList<String>> mapaco = new HashMap<Character, ArrayList<String>>();
		Character inicial = 'A';
		for (int i = 0; i < 26; i++) {
			ArrayList<String> letra = new ArrayList<>();
			mapaco.put(inicial, letra);
			System.out.println(inicial);
			inicial++;
		}
		try {
			BufferedReader br1 = new BufferedReader(new FileReader(new File(
					"Training.200.cad")));
			String line = br1.readLine();
			while (line != null) {
				try {
					System.out.println(line.charAt(0));
					mapaco.get(line.charAt(0)).add(line);
				} catch (Exception ex) {
				}
				line = br1.readLine();
			}
			br1.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

//		System.out.println("Vamos a hacer archivos!!!!!");
		Character ini = 'A';
		try {
			for (int f = 0; f < 5; f++) {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
						"salida" + f + ".txt")));
				ini = 'A';
				for (int i = 0; i < 26; i++) {
					for (int j = 0; j < 40; j++) {
						System.out.println(mapaco.get(ini).get(0));
						bw.write(mapaco.get(ini).get(0) + "\n");
						mapaco.get(ini).remove(0);
					}
					ini++;
				}
				bw.close();
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	/**
	 * Calcular las distancias entre el test y el training usando el k-vecino
	 * más cercano
	 * 
	 * @param ficheroTest
	 * @param ficheroTraining
	 * @return
	 */
	public HashMap<Character, Integer> kVecinosMasCercanos(String ficheroTest) {
		HashMap<Character, Integer> mapaco = new HashMap<>();

		// Inicializo el vector de vecinos

		try {
			Character letra = 'A';
			// Inicializo el map a 0;
			for (int i = 0; i < 26; i++) {
				mapaco.put(letra, 0);
				letra++;
			}


			BufferedReader brTest = new BufferedReader(new FileReader(new File(
					ficheroTest)));
			ArrayList<String> lTraining = new ArrayList<>();
			ArrayList<String> lTest = new ArrayList<>();
			String line;
			BufferedReader brTraining = null;
			for(int i = 0; i < 5; i++){
				String fileToLoad = "salida"+i+".txt";
				if(!fileToLoad.equals(ficheroTest)){
					brTraining = new BufferedReader(new FileReader(new File(fileToLoad)));
					line = brTraining.readLine();
					while (line != null) {
						lTraining.add(line);
						line = brTraining.readLine();
					}
				}
			}
			line = brTest.readLine();
			while (line != null) {
				lTest.add(line);
				line = brTest.readLine();
			}
			char mejorEtiqueta;
			
//			knn.setTrainingSet(lTraining);;
			//knn.setTrainingSet(CNN(knn.getnVecinos(), lTraining));
			knn.setTrainingSet(Editing(knn.getnVecinos(), lTraining));
			//knn.setTrainingSet(MultiEdit(knn.getnVecinos(), lTraining));

			int numAciertos = 0;
			for (String item : lTest) {
				mejorEtiqueta = getContourClass(item);
//				System.out.println("Este tiene la etiqueta "+ item.split(" ")[0] + " y la mínima con kNN es "+ mejorEtiqueta);
				if (item.split(" ")[0].charAt(0) == mejorEtiqueta) {
					mapaco.put(item.split(" ")[0].charAt(0),mapaco.get(item.split(" ")[0].charAt(0)) + 1);
					numAciertos++;
				}
			}
//			System.out.println(knn.getnVecinos() + "NN ha acertado un " + ((float)numAciertos/(float)lTest.size()*100));

			brTraining.close();
			brTest.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapaco;

	}

	/**
	 * Calcular las distancias entre el test y el training usando el vecino más
	 * cercano
	 * 
	 * @param ficheroTest
	 * @param ficheroTraining
	 */
	public HashMap<Character, Integer> vecinoMasCercano(String ficheroTest) {
		HashMap<Character, Integer> mapaco = new HashMap<>();
		try {
			Character letra = 'A';
			// Inicializo el map a 0;
			for (int i = 0; i < 26; i++) {
				mapaco.put(letra, 0);
				letra++;
			}

			BufferedReader brTest = new BufferedReader(new FileReader(new File(
					ficheroTest)));
			ArrayList<String> lTraining = new ArrayList<>();
			ArrayList<String> lTest = new ArrayList<>();
			String line;
			BufferedReader brTraining = null;
			for(int i = 0; i < 5; i++){
				String fileToLoad = "salida"+i+".txt";
				if(!fileToLoad.equals(ficheroTest)){
					brTraining = new BufferedReader(new FileReader(new File(fileToLoad)));
					line = brTraining.readLine();
					while (line != null) {
						lTraining.add(line);
						line = brTraining.readLine();
					}
				}
			}
			line = brTest.readLine();
			while (line != null) {
				lTest.add(line);
				line = brTest.readLine();
			}
			int distanciaMin = Integer.MAX_VALUE;
			String etiquetaMin = "";
			int numAciertos = 0;
			for (String item : lTest) {
				distanciaMin = Integer.MAX_VALUE;
				for (String item2 : lTraining) {
					int distancia = Levenshtein.computeLevenshteinDistance(
							item.split(" ")[1], item2.split(" ")[1]);
					if (distancia < distanciaMin) {
						distanciaMin = distancia;
						etiquetaMin = item2.split(" ")[0];
					}
				}
//				System.out.println("Este tiene la etiqueta "+ item.split(" ")[0] + " y la mínima con 1NN es "+ etiquetaMin);
				if (item.split(" ")[0].equals(etiquetaMin)){
					mapaco.put(item.split(" ")[0].charAt(0),mapaco.get(item.split(" ")[0].charAt(0)) + 1);
					numAciertos++;
					
				}
			}
			
			System.out.println("1NN ha acertado un " + ((float)numAciertos/(float)lTest.size()*100));
			brTraining.close();
			brTest.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return mapaco;
	}
	
	public static ArrayList<ArrayList<String>> bagging(ArrayList<String> training, int m, float percent){
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		for(int i = 0; i < m; i++){
			result.add(new ArrayList<>(training));
			
			
			int elemToDel = (int)((float)result.get(i).size() * (1-percent));
			
			for(int j = 0; j < elemToDel; j++){
				Random r = new Random(result.get(i).size()-1);
				result.get(i).remove(r.nextInt());
			}
			
		}
		
		return result;
	}
	
	public static void pruebaMejorK(ArrayList<String> training, int m, float percent){
		kNN knn = new kNN();
		ArrayList<ArrayList<String>> baggingSet = bagging(training,m,percent);
		for(int i = 0; i < 9; i=i+2){
			knn.inicializarKVecinos();
			knn.setnVecinos(i);
			for(int j = 0; j < baggingSet.size(); j++){
				knn.setTrainingSet(baggingSet.get(j));
			}
		}
		
	}
}
