package frsf.isi.died.guia08.problema01.modelo;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;


public class EmpleadoTest {

	// IMPORTANTE
	// ESTA CLASE ESTA ANOTADA COMO @IGNORE por lo que no ejecutará ningun test
	// hasta que no borre esa anotación.
	Empleado e1;
	Empleado e2;
	Empleado e3;
	Tarea t1;
	Tarea t2;
	Tarea t3;
	@Before
	public void cargar() {
		e1 = new Empleado(10, "Mateo", Tipo.CONTRATADO, 10d);
		e2 = new Empleado(20, "Fidel", Tipo.CONTRATADO, 10d);
		e3 = new Empleado(30, "Matias", Tipo.EFECTIVO, 10d);
		t1 = new Tarea(1," ", 4);
		t2 = new Tarea(2," ", 8);
		t3 = new Tarea(3," ",8);
	}
	
	
	@Test
	public void testSalario() throws noExisteTareaAsignada, TareaExistente {
			e1.asignarTarea(t1);
			e1.comenzar(t1.getId(), "03-05-2020 07:15");
			e1.finalizar(t1.getId(),"04-05-2020 07:15");// 4hs * 10d = 40d
			e1.asignarTarea(t2);
			
			Double salario = e1.salario();
			assertTrue(salario.equals(40d));
	}
	
	@Test (expected = noExisteTareaAsignada.class)
	public void testSalarioNoExisteTarea() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t2.getId(), "04-05-2020 07:15");	
	}
	
	@Test
	public void testCostoTareaNoFinalizada() {
		Double costo = e1.costoTarea(t1);
		assertTrue(costo.equals(40d));
	}
	
	@Test
	public void testCostoTareaEfectivo() throws noExisteTareaAsignada, TareaExistente {
		e3.asignarTarea(t1);
		e3.comenzar(t1.getId(), "03-05-2020 07:15");
		e3.finalizar(t1.getId(), "05-05-2020 07:15");
		assertTrue(e3.costoTarea(t1).equals(40d));// Duracion > DuracionEstimada
		e3.asignarTarea(t2);
		e3.comenzar(t2.getId(), "03-05-2020 07:15");
		e3.finalizar(t2.getId(), "04-05-2020 07:15");	
		assertTrue(e3.costoTarea(t2).equals(96d)); // Duracion < DuracionEstimada 
	}
	
	@Test
	public void testCostoTareaContratado() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "03-05-2020 07:15");
		e1.finalizar(t1.getId(),"06-05-2020 07:15");
		assertTrue(e1.costoTarea(t1).equals(30d));// finaliza la tarea con 2 dias de retraso
		e1.asignarTarea(t2);
		e1.comenzar(t2.getId(), "03-05-2020 07:15");
		e1.finalizar(t2.getId(),"06-05-2020 07:15");
		assertTrue(e1.costoTarea(t2).equals(80d)); // finaliza la tarea con 1 dia de retraso
		e1.asignarTarea(t3);
		e1.comenzar(t3.getId(), "03-05-2020 07:15");
		e1.finalizar(t3.getId(),"04-05-2020 07:15");
		assertTrue(e1.costoTarea(t3).equals(104d)); // finaliza la tarea antes de lo estimado
		
	}

	@Test
	public void testPuedeAsignarTareaContratado() throws TareaExistente {
		Boolean c = e1.asignarTarea(t1);
		assertTrue(c); //Retorna true si lo asigna
		assertTrue(e1.getTareasAsignadas().contains(t1));//Agrega la tarea a la lista de tareas asignadas
		assertEquals(t1.getEmpleadoAsignado(), e1);// La tarea asigna a e1 como empleado asignado
	}
	
	@Test
	public void testNoPuedeAsignarTareaContratado() throws TareaExistente, TareaNoInicializada {
		e1.asignarTarea(t1);
		assertFalse(e2.asignarTarea(t1));// No puede asignar una tarea que ya tiene un empleado .
		t2.setFechaInicio(LocalDateTime.of(2020,1,1,0,0));
		t2.setFechaFin(LocalDateTime.now());
		assertFalse(e1.asignarTarea(t2));// No puede asignar una tarea finalizada
		for(int i = 0 ; i<4 ; i++ ) {
			e1.asignarTarea( new Tarea(50+i,"",3));
		}
		assertFalse(e1.asignarTarea(new Tarea(6,"",3)));// No puede tener mas de 5 tareas sin finalizar
	} 
	
	@Test
	public void testPuedeAsignarTareaEfectivo() throws TareaExistente {
		assertTrue(e3.asignarTarea(t1));
		assertTrue(e3.asignarTarea(t2));
		assertTrue(e3.getTareasAsignadas().contains(t1));//Agrega la tarea a la lista de tareas asignadas
		assertEquals(t1.getEmpleadoAsignado(), e3);// La tarea asigna a e3 como empleado asignado
	}
	
	@Test
	public void testNoPuedeAsignarTareaEfectivo() throws TareaExistente, TareaNoInicializada {
		e1.asignarTarea(t1);
		assertFalse(e3.asignarTarea(t1));// No puede asignar una tarea que ya tiene un empleado .
		t2.setFechaInicio(LocalDateTime.of(2020,1,1,0,0));
		t2.setFechaFin(LocalDateTime.now());
		assertFalse(e3.asignarTarea(t2));// No puede asignar una tarea finalizada
		for(int i = 0 ; i<5 ; i++ ) {
			e3.asignarTarea( new Tarea(i,"",1));
		}
		assertFalse(e3.asignarTarea(new Tarea(6," ",20)));// la suma de las tareas sin finalizar debe ser menor/igual a 15
	}
	
	
	@Test (expected = noExisteTareaAsignada.class)
	public void testComenzarInteger() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId());
		assertFalse(t1.getFechaInicio().equals(null));
		e1.comenzar(t2.getId());
	}

	@Test (expected = noExisteTareaAsignada.class)
	public void testFinalizarInteger() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId());
		e1.finalizar(t1.getId());
		assertFalse(t1.getFechaInicio().equals(null));
		e1.finalizar(t2.getId());
	}

	@Test
	public void testComenzarIntegerString() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "16-06-2020 12:00");
		assertTrue(t1.getFechaInicio().equals(LocalDateTime.of(2020, 6,16,12,0)));
	}

	@Test
	public void testFinalizarIntegerString() throws noExisteTareaAsignada, TareaExistente {
		e1.asignarTarea(t1);
		e1.comenzar(t1.getId(), "16-06-2020 12:00");
		e1.finalizar(t1.getId(), "17-06-2020 12:00");
		assertTrue(t1.getFechaFin().equals(LocalDateTime.of(2020, 6,17,12,0)));
	}

}
