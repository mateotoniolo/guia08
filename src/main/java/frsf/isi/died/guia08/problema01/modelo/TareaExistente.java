package frsf.isi.died.guia08.problema01.modelo;

public class TareaExistente extends Exception {
	public TareaExistente() {
		super("El ID de esta Tarea coincide con una tarea ya existente.");
	}
}
