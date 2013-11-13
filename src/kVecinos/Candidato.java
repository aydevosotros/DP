package kVecinos;

public class Candidato implements Comparable<Object> {
	Candidato(String et, Integer dist) {
		this.etiqueta = et;
		this.distancia = dist;
	}

	private Integer distancia;

	public Integer getDistancia() {
		return distancia;
	}

	public void setDistancia(Integer distancia) {
		this.distancia = distancia;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	private String etiqueta;

	@Override
	public int compareTo(Object o) {
		if (o instanceof Candidato) {
			Candidato c = (Candidato) o;
			return this.distancia - c.getDistancia();
		} else
			return 0;
	}
}
