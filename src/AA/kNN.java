package AA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class kNN{
	private Candidato vecinos[];
	private Integer nVecinos;
	
	public Integer getnVecinos() {
		return nVecinos;
	}

	public void setnVecinos(Integer nVecinos) {
		this.nVecinos = nVecinos;
	}

	public ArrayList<String> getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(ArrayList<String> trainingSet) {
		this.trainingSet = trainingSet;
	}

	private ArrayList<String> trainingSet;
	
	kNN(){
		nVecinos = 9;
	}
	
	public Character getMejorCandidatoSumando(){
		HashMap<Character, Integer> etiquetas = new HashMap<>();
		ArrayList<Character> claves = new ArrayList<>();
		for(int i=0; i<vecinos.length; i++){
			if(etiquetas.get(vecinos[i].getEtiqueta().charAt(0)) != null){
				etiquetas.put(vecinos[i].getEtiqueta().charAt(0), etiquetas.get(vecinos[i].getEtiqueta().charAt(0))+1);
			}
			else{
				etiquetas.put(vecinos[i].getEtiqueta().charAt(0), 1);
				claves.add(vecinos[i].getEtiqueta().charAt(0));
			}
		}
		
		int maxCandidatosLetra = 0;
		Character letra = null;
		
		Iterator<?> it = etiquetas.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry e = (Map.Entry)it.next();
			if((int)e.getValue() > maxCandidatosLetra){
				maxCandidatosLetra = (int) e.getValue();
				letra = (Character)e.getKey();
			};
//			System.out.println(e.getKey() + " " + e.getValue());
		}
		return letra;
	}
	
	public String getMejorCandidatoPonderando(){
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
		Collections.sort(mejores);
		
		return mejores.get(mejores.size()-1).getEtiqueta();
	}
	
	public void checkKVecinos(Candidato candidato){
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
	
	public void printKVecinos(){
		for(int i=0; i<nVecinos; i++){
			if(vecinos[i] != null)
				System.out.println(vecinos[i].getDistancia() + "," + vecinos[i].getEtiqueta());
		}
	}
	
	public void inicializarKVecinos(){
		vecinos = new Candidato[nVecinos];
		// Inicializo el vector
		for(int i=0; i<nVecinos; i++){
			vecinos[i] = new Candidato("null", Integer.MAX_VALUE);
		}
	}
	
}