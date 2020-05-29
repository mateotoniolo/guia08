package frsf.isi.died.guia08.problema01;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.TareaExistente;
import frsf.isi.died.guia08.problema01.modelo.noExisteTareaAsignada;

public class AppRRHH {

	private List<Empleado> empleados = new ArrayList<>();
	private Predicate<Empleado> predicado ;
	private Integer cantReportes = 1;//cuenta la cantidad de reportes 
	
	public void agregarEmpleadoContratado(Integer cuil,String nombre,Double costoHora) {
		predicado = t -> t.getCuil().equals(cuil);
		Optional<Empleado> emp	= buscarEmpleado(predicado);//verifica que el empleado no se encuentre registrado
		if(emp.isEmpty()) {
			this.empleados.add(new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora));		
		}else System.out.println("Error.Este Empleado ya se encuentra agregado");
		// crear un empleado
		// agregarlo a la lista
	}
	
	public void agregarEmpleadoEfectivo(Integer cuil,String nombre,Double costoHora) {
		predicado = t -> t.getCuil().equals(cuil);
		Optional<Empleado> emp	= buscarEmpleado(predicado);//verifica que el empleado no se encuentre registrado
		if(emp.isEmpty()) {
			this.empleados.add(new Empleado(cuil, nombre, Tipo.EFECTIVO, costoHora));		
		}else System.out.println("Error.Este Empleado ya se encuentra agregado");
		
		// crear un empleado
		// agregarlo a la lista	this.empleados.add(new Empleado(cuil, nombre, Tipo.CONTRATADO, costoHora));	
	}
	
	
	public void asignarTarea(Integer cuil,Integer idTarea,String descripcion,Integer duracionEstimada) {
		predicado = t -> t.getCuil().equals(cuil);
		Optional<Empleado> emp	= buscarEmpleado(predicado);//verifica q el empleado indicado exista
		if(emp.isPresent()) {
			try {
				emp.get().asignarTarea(new Tarea(idTarea, descripcion, duracionEstimada));
			} catch (TareaExistente e) {//Tarea ya asignada, existe una Tarea con el mismo id
				
				e.printStackTrace();
			}
		}else System.out.println("ERROR. No existe empleado "+ cuil);
		}
	
	public void empezarTarea(Integer cuil,Integer idTarea) {
		predicado = t -> t.getCuil().equals(cuil);
		Optional<Empleado> emp	= buscarEmpleado(predicado);
		if(emp.isPresent()) {//verifica que exista el empleado
		try {
			emp.get().comenzar(idTarea);
		} catch (noExisteTareaAsignada e) {//la Tarea buscada no existe
			
			e.printStackTrace();
		}
			
		}else System.out.println("ERROR. No existe empleado "+ cuil) ; //Siendo esta la clase App no generamos una Exception, simplemente imprimimos el Error
		// busca el empleado por cuil en la lista de empleados
		// con el método buscarEmpleado() actual de esta clase
		// e invoca al método comenzar tarea
		}
	
	public void terminarTarea(Integer cuil,Integer idTarea) {
		predicado = t -> t.getCuil().equals(cuil);
		Optional<Empleado> emp	= buscarEmpleado(predicado);
		if(emp.isPresent()) {
			try {
				emp.get().finalizar(idTarea);
			} catch (noExisteTareaAsignada e) {
				e.printStackTrace();
			}
		}else  System.out.println("ERROR. No existe empleado "+ cuil);
	}

	public void cargarEmpleadosContratadosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					this.agregarEmpleadoContratado(Integer.valueOf(fila[0]), String.valueOf(fila[1]), Double.valueOf(fila[2]));//Lee los datos , genera un empleado y lo agrega
				}
			}
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado
	}
 
	public void cargarEmpleadosEfectivosCSV(String nombreArchivo) throws FileNotFoundException, IOException {
		try(Reader fileReader = new FileReader(nombreArchivo)){
			try(BufferedReader in = new BufferedReader(fileReader)){
				String linea = null;
				while((linea = in.readLine()) != null) {
					String[] fila = linea.split(";");
					this.agregarEmpleadoEfectivo(Integer.valueOf(fila[0]), fila[1], Double.valueOf(fila[2]));//Lee los datos , genera un empleado y lo agrega
				}
			}
		}
		// leer datos del archivo
		// por cada fila invocar a agregarEmpleadoContratado		
	}

	public void cargarTareasCSV(String nombreArchivo) {
		
		try(Reader fileReader = new FileReader(nombreArchivo)) { 
			try(BufferedReader in = new BufferedReader(fileReader)){ 
				String linea = null; 
				while((linea = in.readLine())!=null) { 
					String[] fila = linea.split(";");
				 
				try {
					this.asignarTarea(Integer.valueOf(fila[0]), Integer.valueOf(fila[1]), String.valueOf(fila[2]), Integer.valueOf(fila[3]));//busca el idEmpleado y le asigna la tarea creada
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} 
			   } 
			  } 
			 }catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				// leer datos del archivo
				// cada fila del archivo tendrá:
				// cuil del empleado asignado, numero de la taera, descripcion y duración estimada en horas.
	}
	
	private void guardarTareasTerminadasCSV() {
		try(Writer fileWriter= new FileWriter("TareasFacturar"+cantReportes +".csv",true)) { 
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				out.write("Fecha y hora de Emision:" + LocalDateTime.now()+"\n");
				for(Empleado e: this.empleados) {
					for(Tarea t: e.getTareasAsignadas()) {
						if(t.getFechaFin()!= null && !t.getFacturada()) {
					out.write(t.getId()+";\""+ t.getDescripcion()+"\""+";"+ t.getDuracion()+ ";"+ e.costoTarea(t)+";\""+ e.getNombre()+"\""+";"+ e.getCuil()+ System.getProperty("line.separator"));
						}
					}
				}
			}
			this.cantReportes +=1;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// guarda una lista con los datos de la tarea que fueron terminadas
		// y todavía no fueron facturadas
		// y el nombre y cuil del empleado que la finalizó en formato CSV 
	}
	
	private Optional<Empleado> buscarEmpleado(Predicate<Empleado> p){
		return this.empleados.stream().filter(p).findFirst();
	}

	public Double facturar() {
		this.guardarTareasTerminadasCSV();
		return this.empleados.stream()				
				.mapToDouble(e -> e.salario())
				.sum();
	}

	
	public List<Empleado> getEmpleados() {
		return empleados;
	}

	
	public void setEmpleados(List<Empleado> empleados) {
		this.empleados = empleados;
	}
}
