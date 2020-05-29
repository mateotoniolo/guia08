package frsf.isi.died.guia08.problema01.modelo;

public class tareaAsignada extends Exception{
	
	public tareaAsignada() {
		
		super("Exception. Esta Tarea ya se encuentra asignada a otro Empleado.");
	}

}
