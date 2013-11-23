package master.actividades.servicioweb;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
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

public class Alta extends Activity {
	private final int INSERT  = 2;

	private EditText dni;
	private EditText nombre;
	private EditText apellidos;
	private EditText direccion;
	private EditText telefono;
	private EditText equipo;
	private ImageButton btnAgregar;
	private Datos datos;

    // Progress Dialog
    private ProgressDialog pDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alta);

		datos = new Datos();

		dni = (EditText) findViewById(R.id.editDNIAlta);		
		nombre = (EditText) findViewById(R.id.editNombreAlta);
		apellidos = (EditText) findViewById(R.id.editApellidosAlta);	
		direccion = (EditText) findViewById(R.id.editDireccionAlta);
		telefono = (EditText) findViewById(R.id.editTelefonoAlta);		
		equipo = (EditText) findViewById(R.id.editEquipoAlta);
		btnAgregar = (ImageButton) findViewById(R.id.btnAceptarAlta);
		
		dni.setText(getIntent().getExtras().getString("dni"));
		
		btnAgregar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				datos.setDni(dni.getText().toString());
				datos.setNombre(nombre.getText().toString());
				datos.setApellidos(apellidos.getText().toString());
				datos.setDireccion(direccion.getText().toString());
				datos.setTelefono(telefono.getText().toString());
				datos.setEquipo(equipo.getText().toString());
				
				JSONObject json = new JSONObject();
				try {
					json.put("DNI", datos.getDni());
					json.put("Nombre", datos.getNombre());
					json.put("Apellidos", datos.getApellidos());
					json.put("Direccion", datos.getDireccion());
					json.put("Telefono", datos.getTelefono());
					json.put("Equipo", datos.getEquipo());
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
				String content = json.toString();
		    	// env�o de informaci�n v�a entidad
				/*String content = "{\"DNI\":\""+ valorDni +"\"," +
									"\"Nombre\":\""+ valorNombre +"\"," +
									"\"Apellidos\":\""+ valorApellidos +"\"," +
									"\"Direccion\":\""+ valorDireccion +"\"," +
									"\"Telefono\":\""+ valorTelefono +"\"," +
									"\"Equipo\":\""+ valorEquipo +"\"" +
									"}";*/
		    	new Insert().execute(content);			
			}
		});
	}
	
    /**
     * Background Async Task to Insert 
     * */
    class Insert extends AsyncTask <String, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Alta.this);
            pDialog.setMessage(getString(R.string.texto_progreso));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Obtaining info
         * */
        //Aqu� NUNCA se pone nada que tenga que ver con aspectos visuales.
        //Es algo que se ejecuta en Background, es decir, no est� conectado con la interfaz.
        protected HttpResponse doInBackground(String... params) {
        	HttpResponse response = null;
        	
        	try{
    	        //HttpClient httpclient = new DefaultHttpClient();
    	        AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost httppost = new HttpPost(new Url(INSERT, Alta.this).toString());
    	        httppost.addHeader(new BasicHeader("Content-Type", "application/json"));
    	        //httppost.setEntity(new UrlEncodedFormEntity(content));
    	        httppost.setEntity(new StringEntity(params[0]));
    	        response = httpclient.execute(httppost);
        	}catch(Exception e){
    	        Log.e(getString(R.string.nombre_app), R.string.errorHTTP+": "+e.toString());
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
		getMenuInflater().inflate(R.menu.alta, menu);
		return true;
	}

}