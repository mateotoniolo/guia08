package frsf.isi.died.guia08.problema01.modelo;

import java.time.Duration;
import java.time.LocalDateTime;

public class Tarea {

	private Integer id;
	private String descripcion;
	private Integer duracionEstimada ;
	private Empleado empleadoAsignado;
	private LocalDateTime fechaInicio = null;
	private LocalDateTime fechaFin = null;
	private Boolean facturada;
	private Duration duracion;
	
	
	public Tarea(int iden, String desc, int dura) {
		this.id = iden;
		this.descripcion = desc;
		this.duracionEstimada = dura;
		this.facturada = false;
		this.empleadoAsignado = null;
	}
	
	public Long getDuracion() {
		return duracion.toDays()*4;
	}

	public void setDuracion() {
		this.duracion =  Duration.between(fechaInicio, fechaFin);
	}

	public void asignarEmpleado(Empleado e) throws tareaAsignada, tareaFinalizada{
		if(this.fechaFin != null) {
			throw new tareaFinalizada();
		}else if(this.empleadoAsignado != null) {
			throw new tareaAsignada();
		}else {
		this.empleadoAsignado = e;
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionEstimada() {
		return duracionEstimada;
	}

	public void setDuracionEstimada(Integer duracionEstimada) {
		this.duracionEstimada = duracionEstimada;
	}

	public LocalDateTime getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDateTime fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDateTime getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDateTime fechaFin) throws TareaNoInicializada {
		if(this.fechaInicio != null) {
		this.fechaFin = fechaFin;
		this.setDuracion();}
		else {
			throw new TareaNoInicializada();
		}
	}

	public Boolean getFacturada() {
		return facturada;
	}

	public Tarea setFacturada(Boolean facturada) {
		this.facturada = facturada;
		return this;
	}

	public Empleado getEmpleadoAsignado() {
		return empleadoAsignado;
	}
	
	
}
