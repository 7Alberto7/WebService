package master.actividades.servicioweb;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Baja extends Activity {
	private static final int DELETE  = 3;

	private int posicion = 1;
	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private ImageButton btnBorrar;
	private Bundle extras;
	private String entrada;
	private Datos datos;
    // Progress Dialog
    private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_baja);
		
        extras = getIntent().getExtras();
        entrada = extras.getString("datos");
		datos = new Datos();

		dni = (EditText) findViewById(R.id.editDNIBaja);		
		nombre = (EditText) findViewById(R.id.editNombreBaja);
		apellidos = (EditText) findViewById(R.id.editApellidosBaja);	
		direccion = (EditText) findViewById(R.id.editDireccionBaja);
		telefono = (EditText) findViewById(R.id.editTelefonoBaja);		
		equipo = (EditText) findViewById(R.id.editEquipoBaja);
		btnBorrar = (ImageButton) findViewById(R.id.btnAceptarBaja);
		
		btnBorrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		    	BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",dni.getText().toString());
		    	new DeleteDNI().execute(nameValuePairs);
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
        
    	dni.setText(datos.getDni());
    	nombre.setText(datos.getNombre());
    	apellidos.setText(datos.getApellidos());
    	direccion.setText(datos.getDireccion());
    	telefono.setText(datos.getTelefono());
    	equipo.setText(datos.getEquipo());
    }

    /**
     * Background Async Task to Query 
     * */
    class DeleteDNI extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Baja.this);
            pDialog.setMessage(getString(R.string.texto_progreso));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Obtaining info
         * */
        //Aquí NUNCA se pone nada que tenga que ver con aspectos visuales.
        //Es algo que se ejecuta en Background, es decir, no está conectado con la interfaz.
        protected HttpResponse doInBackground(BasicNameValuePair... params) {
        	HttpResponse response = null;
        	AndroidHttpClient httpclient = null;
        	
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
        	for(int i=0; i<params.length; i++){
        		nameValuePairs.add(params[i]);
        	}
       	
        	try{
    	        //HttpClient httpclient = new DefaultHttpClient();
    	        httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost httppost = new HttpPost(new Url(DELETE, Baja.this).toString());
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	        //httppost.setEntity(new StringEntity(content));
    	        response = httpclient.execute(httppost);
        	}catch(Exception e){
    	        Log.e(getString(R.string.nombre_app), R.string.errorHTTP+": "+e.toString());
        	}
        	if (httpclient != null) {
				httpclient.close();
			}

	        return response;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(HttpResponse response) {
        	String message = "";
        	
            // dismiss the dialog once done
            pDialog.dismiss();
            
            if (response != null) {
            
	            int responseCode = response.getStatusLine().getStatusCode();
	            String responseMessage = response.getStatusLine().getReasonPhrase();
	
		        HttpEntity entity = response.getEntity();
	            if (entity != null) {
	                String responseString;
					try {
						responseString = EntityUtils.toString(entity);
						message = responseString;
						JSONArray messageJson = new JSONArray(message);
			            volver(messageJson);
					} 
					catch (ParseException e) {} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
	            } else {
	            	message = responseCode+": "+responseMessage;
	            }
			} else {
				Toast.makeText(getApplicationContext(), getString(R.string.no_se_ha_podido_establecer_la_conexion), Toast.LENGTH_SHORT).show();
			}
        }
 
    }
	
	private void volver(JSONArray messageJson) throws JSONException{
		JSONObject json = messageJson.getJSONObject(0);
		int salida = json.getInt("NUMREG");

		Intent i = new Intent();
		i.putExtra("Salida", salida);
   		setResult(RESULT_OK, i);
		finish();
	}
	
    @Override
	public void onBackPressed() {
		setResult(RESULT_CANCELED);
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baja, menu);
		return true;
	}

}
