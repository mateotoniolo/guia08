package frsf.isi.died.guia08.problema01;

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import frsf.isi.died.guia08.problema01.modelo.Empleado;
import frsf.isi.died.guia08.problema01.modelo.Empleado.Tipo;
import frsf.isi.died.guia08.problema01.modelo.Tarea;
import frsf.isi.died.guia08.problema01.modelo.noExisteTareaAsignada;

public class AppRRHHTest {

	AppRRHH rh ;
	@Before
	public void init() {
	rh = new AppRRHH();
	}
	
	@Test
	public void testAgregarContratado() {
		rh.agregarEmpleadoContratado(20, "Mateo", 10d);
		Empleado e = rh.getEmpleados().get(0);
		assertTrue(e.getCuil().equals(20));// verificamos que el empleado agregado sea el indicado
		assertTrue(e.getTipo().equals(Tipo.CONTRATADO));
		rh.agregarEmpleadoContratado(20, "Mateo", 10d);//este empleado ya existe, no lo registra
		int size = rh.getEmpleados().size();
		assertEquals(size,1);// no lo agrega
	}
	
	@Test 
	public void testAgregarEfectivo() {
		rh.agregarEmpleadoEfectivo(20, "Mateo", 10d);
		Empleado e = rh.getEmpleados().get(0);
		assertTrue(e.getCuil().equals(20));// verificamos que el empleado agregado sea el indicado
		assertTrue(e.getTipo().equals(Tipo.EFECTIVO));
		rh.agregarEmpleadoEfectivo(20, "Mateo", 10d);//este empleado ya existe, no lo registra
		int size = rh.getEmpleados().size();
		assertEquals(size,1);// no lo agrega
	}
	
	@Test
	public void asignarTareaTest() {
		
		rh.agregarEmpleadoContratado(100, "mateo", 50d);
		rh.asignarTarea(100, 300, "dddddd", 16);
		Optional<Tarea> opcion = rh.getEmpleados().stream().filter(e -> e.getCuil().equals(100)).findFirst().get()
									.getTareasAsignadas().stream()
									.filter(t -> t.getId().equals(300)).findAny();
		assertTrue(opcion.isPresent());
	}
	
	@Test
	public void cargarContratadoTest() throws FileNotFoundException, IOException {
		try(Writer fileWriter= new FileWriter("ContratadosTest.csv",true)) { 
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				
				out.write((int)30+";\""+ "ftytfjs"+"\""+";"+ 50d + System.getProperty("line.separator"));
			
			}
		}
		rh.cargarEmpleadosContratadosCSV("ContratadosTest.csv");
		assertTrue(rh.getEmpleados().get(0).getCuil().equals(30));
	}
	
	@Test
	public void cargarEfectivoTest() throws FileNotFoundException, IOException {
		try(Writer fileWriter= new FileWriter("EfectivosTest.csv",true)) { 
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				
				out.write((int)30+";\""+ "ftytfjs"+"\""+";"+ 50d + System.getProperty("line.separator"));
			
			}
		}
		rh.cargarEmpleadosEfectivosCSV("EfectivosTest.csv");
		assertTrue(rh.getEmpleados().get(0).getCuil().equals(30));
	}
	
	@Test
	public void cargarTareasTest() throws IOException {
		Empleado empleado = new Empleado(40,"mateo",Tipo.CONTRATADO ,30d);
		rh.getEmpleados().add(empleado);
		try(Writer fileWriter= new FileWriter("CargarTareasTest.csv",true)) { 
			try(BufferedWriter out = new BufferedWriter(fileWriter)){
				
			out.write((int) 40+";"+(int)30+";\""+ "ftytfjs"+"\""+";"+ 50 + System.getProperty("line.separator"));
			
			}
		} 
		rh.cargarTareasCSV("CargarTareasTest.csv");
		assertTrue(rh.getEmpleados().get(0).getTareasAsignadas().get(0).getId().equals(30));
	}
	
	@Test
	public void FacturarTest() throws  noExisteTareaAsignada {
		rh.agregarEmpleadoContratado(20, "mateo", 10d);
		rh.agregarEmpleadoEfectivo(30, "nicolas", 20d);
		rh.asignarTarea(20, 200, " ", 8);//asigna tarea Contratado
		rh.asignarTarea(30, 300, " ", 2);//asigna tarea Efectivo
		rh.asignarTarea(20, 400, " ", 8);//asigna tarea Contratado
		rh.asignarTarea(30, 500, " ", 2);//asigna tarea Efectivo
		rh.asignarTarea(20, 700, " ", 2);//Tarea que no sera finalizada
		rh.empezarTarea(20, 200);
		rh.empezarTarea(30, 300);
		rh.empezarTarea(20, 400);
		rh.empezarTarea(30, 500);
		rh.empezarTarea(20, 700);//Esta tarea se inicia pero no se finaliza
		rh.terminarTarea(20, 200); 
		rh.terminarTarea(30, 300);
		rh.terminarTarea(20, 400);
		rh.terminarTarea(30, 500);
		assertTrue(rh.facturar().equals(304d));
		assertTrue(rh.facturar().equals(0d));//En la funcion anterior todas las tareas se registran como facturadas
	}
}
