package master.actividades.servicioweb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class Consulta extends Activity {
	private int posicion = 1;
	private TextView numRegistros;
	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private ImageButton btnPrimero;
	private ImageButton btnAnterior;
	private ImageButton btnSiguiente;
	private ImageButton btnUltimo;
	private Bundle extras;
	private String entrada;
	private int totalRegistros;
	private Datos datos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_consulta);
		
        extras = getIntent().getExtras();
        entrada = extras.getString("datos");
		totalRegistros = extras.getInt("numRegistros");
		datos = new Datos();

		numRegistros = (TextView) findViewById(R.id.numeroRegistros);
		dni = (EditText) findViewById(R.id.editDNIConsulta);		
		nombre = (EditText) findViewById(R.id.editNombreConsulta);
		apellidos = (EditText) findViewById(R.id.editApellidosConsulta);	
		direccion = (EditText) findViewById(R.id.editDireccionConsulta);
		telefono = (EditText) findViewById(R.id.editTelefonoConsulta);		
		equipo = (EditText) findViewById(R.id.editEquipoConsulta);
		btnPrimero = (ImageButton) findViewById(R.id.btnPrimero);
		btnAnterior = (ImageButton) findViewById(R.id.btnAnterior);
		btnSiguiente = (ImageButton) findViewById(R.id.btnSiguiente);
		btnUltimo = (ImageButton) findViewById(R.id.btnUltimo);
		
		btnPrimero.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				posicion = 1;
		        cargarDatos(entrada, posicion, datos);
			}
		});
		
		btnAnterior.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (posicion > 1) {
					posicion--;
			        cargarDatos(entrada, posicion, datos);
				}
			}
		});
		
		btnSiguiente.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (posicion < totalRegistros) {
					posicion++;
			        cargarDatos(entrada, posicion, datos);
				}
			}
		});
		
		btnUltimo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				posicion = totalRegistros;
		        cargarDatos(entrada, posicion, datos);
			}
		});
        
        cargarDatos(entrada, posicion, datos);
        
        
	}
    
    private void cargarDatos(String entrada, int posicion, Datos datos) {
    	
        try {
			JSONArray datosJson = new JSONArray(entrada);

			JSONObject json = datosJson.getJSONObject(posicion);

			datos.setDni(json.getString("DNI"));
			datos.setNombre(json.getString("Nombre"));
			datos.setApellidos(json.getString("Apellidos"));
			datos.setDireccion(json.getString("Direccion"));
			datos.setTelefono(json.getString("Telefono"));
			datos.setEquipo(json.getString("Equipo"));

		} catch (JSONException e) {
			e.printStackTrace();
		}
        
    	numRegistros.setText(getString(R.string.registro)+" "+posicion+" "+ getString(R.string.de)+" "+Integer.toString(totalRegistros));
    	dni.setText(datos.getDni());
    	nombre.setText(datos.getNombre());
    	apellidos.setText(datos.getApellidos());
    	direccion.setText(datos.getDireccion());
    	telefono.setText(datos.getTelefono());
    	equipo.setText(datos.getEquipo());
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.consulta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
