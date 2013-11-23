package master.actividades.servicioweb;

public class Datos {
	private String dni;
	private String nombre;
	private String apellidos;
	private String direccion;
	private String telefono;
	private String equipo;

	public Datos() {
		this.dni = "";
		this.nombre = "";
		this.apellidos = "";
		this.direccion = "";
		this.telefono = "";
		this.equipo = "";
	}
	
	public Datos(String dni, String nombre, String apellidos, String direccion,
			String telefono, String equipo) {
		this.dni = dni;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.direccion = direccion;
		this.telefono = telefono;
		this.equipo = equipo;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEquipo() {
		return equipo;
	}

	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	
}
