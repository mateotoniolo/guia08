package frsf.isi.died.guia08.problema01.modelo;

public class noExisteTareaAsignada extends Exception {
	public noExisteTareaAsignada() {
		super("Exception. Esta Tarea no se encuentra asignada.");
	}
}
