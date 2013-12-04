package AA;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Pruebas {
	public static void PruebasDeKVecinos() {
		kNN knn = new kNN();
		knn.inicializarKVecinos();
		// Hago una pruebecica
		knn.checkKVecinos(new Candidato("G", 10));
		knn.checkKVecinos(new Candidato("Q", 5));
		knn.checkKVecinos(new Candidato("O", 3));
		knn.checkKVecinos(new Candidato("C", 44));
		knn.checkKVecinos(new Candidato("D", 22));
		knn.checkKVecinos(new Candidato("O", 2));
		knn.checkKVecinos(new Candidato("D", 23));
		// knn.printKVecinos();
		System.out.println(knn.getMejorCandidatoPonderando());
	}

	public static void PruebasConTodoVecinoMasCercano() {
		P102 p = new P102();
		Character letra = 'A';
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(new File("resultados.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<HashMap<Character, Integer>> listaAciertosParciales = new ArrayList<>();
		// Dibujo la fila de arriba de la tabla e inicializo los aciertos
		// globales
		for (int i = 0; i < 26; i++) {
			if (i < 25) {
				System.out.print(letra + "\t");
				try {
					bw.write(letra + "\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.print(letra + "\n");
				try {
					bw.write(letra + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			letra++;
		}

		for (int j = 0; j < 5; j++) {
			// System.out.println("Usando archivo test: salida"+j+".txt");
			HashMap<Character, Integer> aciertosGlobales = new HashMap<>();
			// Inicializo los globales
			letra = 'A';
			for (int i = 0; i < 26; i++) {
				aciertosGlobales.put(letra, 0);
				letra++;
			}
			for (int i = 0; i < 5; i++) {
				if (i != j) {
					// System.out.println("Procesando fichero: salida"+i+".txt");
					HashMap<Character, Integer> aciertos = p.kVecinosMasCercanos(
							"salida" + i + ".txt", "salida" + j + ".txt");
					letra = 'A';
					for (int k = 0; k < 26; k++) {
						Integer parcial = aciertos.get(letra);
						aciertosGlobales.put(letra, aciertosGlobales.get(letra)
								+ parcial);
						letra++;
					}
					listaAciertosParciales.add(aciertos);
				}
			}
			letra = 'A';
			for (int i = 0; i < 26; i++) {
				if (i < 25) {
					System.out.print(aciertosGlobales.get(letra) + "\t");
					try {
						bw.write(aciertosGlobales.get(letra) + "\t");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.print(aciertosGlobales.get(letra) + "\n");
					try {
						bw.write(aciertosGlobales.get(letra) + "\n");
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

	public static void PruebasConTodoKVecinos() {
		P102 p = new P102();
		Character letra = 'A';
		BufferedWriter bw = null;

		try {
			bw = new BufferedWriter(new FileWriter(new File("resultados.txt")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ArrayList<HashMap<Character, Integer>> listaAciertosParciales = new ArrayList<>();
		// Dibujo la fila de arriba de la tabla e inicializo los aciertos
		// globales
		for (int i = 0; i < 26; i++) {
			if (i < 25) {
				System.out.print(letra + "\t");
				try {
					bw.write(letra + "\t");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				System.out.print(letra + "\n");
				try {
					bw.write(letra + "\n");
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

		for (int j = 0; j < 5; j++) {
			// System.out.println("Usando archivo test: salida"+j+".txt");
			HashMap<Character, Integer> aciertosGlobales = new HashMap<>();
			// Inicializo los globales
			letra = 'A';
			for (int i = 0; i < 26; i++) {
				aciertosGlobales.put(letra, 0);
				letra++;
			}
			for (int i = 0; i < 5; i++) {
				if (i != j) {
					// System.out.println("Procesando fichero: salida"+i+".txt");
					HashMap<Character, Integer> aciertos = p.vecinoMasCercano(
							"salida" + i + ".txt", "salida" + j + ".txt");
					letra = 'A';
					for (int k = 0; k < 26; k++) {
						Integer parcial = aciertos.get(letra);
						aciertosGlobales.put(letra, aciertosGlobales.get(letra)
								+ parcial);
						letra++;
					}
					listaAciertosParciales.add(aciertos);
				}
			}

			try {
				bw = new BufferedWriter(new FileWriter(new File(
						"resultados.txt")));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			letra = 'A';
			for (int i = 0; i < 26; i++) {
				if (i < 25) {
					System.out.print(aciertosGlobales.get(letra) + "\t");
					try {
						bw.write(aciertosGlobales.get(letra) + "\t");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					System.out.print(aciertosGlobales.get(letra) + "\n");
					try {
						bw.write(aciertosGlobales.get(letra) + "\n");
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
