package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class Empleado {

	public enum Tipo { CONTRATADO,EFECTIVO}; 
	
	private Integer cuil;
	private String nombre;
	private Tipo tipo  ;
	private Double costoHora;
	private List<Tarea> tareasAsignadas ;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
	
	private Function<Tarea, Double> calculoPagoPorTarea;		
	private Predicate<Tarea> puedeAsignarTarea ;
	
	public Empleado (Integer cuil, String name, Tipo type, Double costo) {
		this.cuil = cuil;
		this.nombre = name;
		this.tipo = type;
		this.costoHora = costo;
		this.tareasAsignadas = new ArrayList<Tarea>();
	}
	
	public Double salario() {
		Double  salarioTotal;
		salarioTotal = this.tareasAsignadas.stream() // suma los salarios de las tareas no facturadas 
							.filter((t) -> (!t.getFacturada() && t.getFechaFin() != null))// filtrar todas las tareas no facturadas que esten finalizadas 
							.map (t -> {
								if(t.getFechaFin() != null ) {
									return t.setFacturada(true);// marcar como facturadas todas las tareas que TENGAN FECHA DE FIN
								}
								else {
									return t;
								}
							})
							.mapToDouble(t -> this.costoTarea(t))// calcular el costo
							.sum() //Los suma
							;
		
		return salarioTotal;
	}
	
	/**
	 * Si la tarea ya fue terminada nos indica cuaal es el monto según el algoritmo de calculoPagoPorTarea
	 * Si la tarea no fue terminada simplemente calcula el costo en base a lo estimado.
	 * @param t
	 * @return
	 */
	public Double costoTarea(Tarea t) {
		if(t.getFechaFin() != null) { // tareas finalizadas
			switch(tipo) {
			case EFECTIVO :
				if(t.getDuracion() >= t.getDuracionEstimada() ) {
				calculoPagoPorTarea = n -> n.getDuracionEstimada() * this.costoHora ;
				}else {
					Double costoHoraAumentado = (this.costoHora)/5 + this.costoHora;
					calculoPagoPorTarea = n -> n.getDuracionEstimada() * costoHoraAumentado ;
							}
				break;
			case CONTRATADO :
				if(t.getDuracion() - t.getDuracionEstimada() >= 8 ) {
					Double costoHoraAumentado =  (this.costoHora)* 75/100 ;
					calculoPagoPorTarea = n -> n.getDuracionEstimada() * costoHoraAumentado ;
					}else if(t.getDuracion() >= t.getDuracionEstimada() ) {
						calculoPagoPorTarea = n -> n.getDuracionEstimada() * this.costoHora ;
					}else {
						Double costoHoraAumentado = (this.costoHora)* 3/10 + this.costoHora;
						calculoPagoPorTarea = n -> n.getDuracionEstimada() * costoHoraAumentado ;
								}
				break;
			}
		}else {
			calculoPagoPorTarea = n -> n.getDuracionEstimada() * this.costoHora;// tareas NO finalizadas
		}
		return calculoPagoPorTarea.apply(t); 
	}
		
	public Boolean asignarTarea(Tarea t) throws TareaExistente {
		Optional<Tarea> opcional = this.tareasAsignadas.stream()
														.filter(s -> s.getId().equals(t.getId()))
														.findFirst();//verifica q no exista una tarea con el mismo ID
		if(opcional.isEmpty()) {
		switch(this.tipo) {
		case CONTRATADO :
			puedeAsignarTarea = (Tarea n) -> (this.tareasAsignadas.stream().filter(c -> c.getFechaFin() == null).count() < 5);
			
			if (puedeAsignarTarea.test(t)) {
			try {
				t.asignarEmpleado(this);
			}catch (tareaAsignada a) {
				System.out.println(a);
					return false;
				}catch (tareaFinalizada f) {
					System.out.println(f);
					return false;
				}
				tareasAsignadas.add(t);
				
				return true;
			}else {
				System.out.println("La Tarea " + t.getId()+" NO se puede asignar.");
			}
			break;
		case EFECTIVO : 

			puedeAsignarTarea = (Tarea c) -> ((this.tareasAsignadas.stream()
					.filter(n -> n.getFechaFin() == null)
					.mapToInt(n -> n.getDuracionEstimada())
					.reduce((acum,tot) -> {return acum+tot;})
					.orElse(0)) + c.getDuracionEstimada() <= 15);
			
			if (puedeAsignarTarea.test(t)) {
				try {
					t.asignarEmpleado(this);
				}catch (tareaAsignada a) {
					System.out.println(a);
						return false;
					}catch (tareaFinalizada f) {
						System.out.println(f);
						return false;
					}
				tareasAsignadas.add(t);
				
				return true;
			}else {
				System.out.println("La Tarea " + t.getId()+" NO se puede asignar.");
			}
		}
		}else {
			throw new TareaExistente();//cuando se quiere ingresar una tarea con un id perteneciente a una tarea ya registrada
		}
		return false;
	}
	
	public void comenzar(Integer idTarea) throws noExisteTareaAsignada {
		Optional<Tarea> opcional = this.tareasAsignadas.stream()
														.filter(t -> t.getId().equals(idTarea))
														.findAny();
		if(opcional.isPresent()) {
			opcional.get().setFechaInicio(LocalDateTime.now());
		}else {
			throw new noExisteTareaAsignada();
		}
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de inicio la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea) throws noExisteTareaAsignada {
		Optional<Tarea> opcional = this.tareasAsignadas.stream()
														.filter(t -> t.getId().equals(idTarea))
														.findAny();
		if(opcional.isPresent()) {
			try {
				opcional.get().setFechaFin(LocalDateTime.now());
			} catch (TareaNoInicializada e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}else {
			throw new noExisteTareaAsignada();
		}
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}

	public void comenzar(Integer idTarea,String fecha) throws noExisteTareaAsignada {
		Optional<Tarea> opcional = this.tareasAsignadas.stream()
						.filter(t -> t.getId() == idTarea)
						.findAny();
		if(opcional.isPresent()) {
		opcional.get().setFechaInicio(LocalDateTime.parse(fecha, this.formatter));
		}else {
		throw new noExisteTareaAsignada();
		}
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public void finalizar(Integer idTarea,String fecha) throws noExisteTareaAsignada {
		Optional<Tarea> opcional = this.tareasAsignadas.stream()
				.filter(t -> t.getId() == idTarea)
				.findAny();
			if(opcional.isPresent()) {
			try {
				opcional.get().setFechaFin(LocalDateTime.parse(fecha, this.formatter));
			} catch (TareaNoInicializada e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			}else {
			throw new noExisteTareaAsignada();
			}
		// busca la tarea en la lista de tareas asignadas 
		// si la tarea no existe lanza una excepción
		// si la tarea existe indica como fecha de finalizacion la fecha y hora actual
	}
	
	public List<Tarea> getTareasAsignadas() {
		return tareasAsignadas;
	}

	public void setTareasAsignadas(List<Tarea> tareasAsignadas) {
		this.tareasAsignadas = tareasAsignadas;
	}

	public Integer getCuil() {
		return cuil;
	}

	public void setCuil(Integer cuil) {
		this.cuil = cuil;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
}

