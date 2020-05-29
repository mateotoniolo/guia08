package frsf.isi.died.guia08.problema01.modelo;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;

public class TareaTest {
	Empleado e1;
	Empleado e2;
	Tarea t1;
	@Before
	public void init() {
		e1 = new Empleado(10, "Mateo", Tipo.CONTRATADO, 10d);
		e2 = new Empleado(10, "Mati", Tipo.CONTRATADO, 10d);
		t1 = new Tarea(1," ", 3);
	}
	@Test (expected = tareaFinalizada.class)
	public void asignarEmpleadoTareaFinalizada() throws tareaAsignada, tareaFinalizada, TareaNoInicializada {
		t1.setFechaInicio(LocalDateTime.now());
		t1.setFechaFin(LocalDateTime.now());
		t1.asignarEmpleado(e1);
		}
	@Test (expected = tareaAsignada.class)
	public void asignarEmpleadoTareaAsignada() throws tareaAsignada, tareaFinalizada {
		t1.asignarEmpleado(e2);
		t1.asignarEmpleado(e1);
		}
	@Test (expected = TareaNoInicializada.class)
	public void finalizarTareaSinEmpezarlaTest() throws TareaNoInicializada {
		t1.setFechaFin(LocalDateTime.now());
	}
}
