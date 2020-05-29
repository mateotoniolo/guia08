package frsf.isi.died.guia08.problema01.modelo;




import frsf.isi.died.guia08.problema01.AppRRHH;
public class main {

	public static void main(String[] args)  {
		//prueba
		AppRRHH rh = new AppRRHH();
		rh.agregarEmpleadoContratado(20, "mateo", 10d);
		rh.agregarEmpleadoContratado(20, "matias", 10d);//asigna el mismo empleado segun id
		rh.agregarEmpleadoEfectivo(30, "nicolas", 20d);
		rh.asignarTarea(20, 200, " ", 8);
		rh.asignarTarea(30, 300, " ", 8);
		rh.asignarTarea(20, 400, " ", 8);
		rh.asignarTarea(30, 500, " ", 2);
		rh.asignarTarea(20, 200, " ", 8);//asignar la misma tarea a un mismo emleado
		rh.asignarTarea(10, 2200, " ", 8);// empleado que no existe
		
		rh.terminarTarea(20, 200);//no puede finalizar una tarea que no empezo
		rh.empezarTarea(20, 200);
		rh.empezarTarea(30, 300);
		rh.empezarTarea(20, 400);
		rh.empezarTarea(30, 500);
		
		
		rh.terminarTarea(30, 300);
		rh.terminarTarea(20, 400);
		rh.terminarTarea(30, 500);
		
		System.out.println(rh.facturar());
		for(Empleado e: rh.getEmpleados()) {
			System.out.println(e.getNombre());
			for(Tarea t: e.getTareasAsignadas()) {
				System.out.println(t.getId()+"   "+t.getFechaInicio()+"    "+ t.getFechaFin()+"   "+t.getFacturada());
			}// verifica que solo facture las tareas finalizadas
		}
		System.out.println(rh.facturar());// este reporte debe estar en blanco

	}
}
