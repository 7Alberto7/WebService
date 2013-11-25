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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class Principal extends Activity {
	private final int ACTIVIDAD_CONSULTA  = 1;
	private final int ACTIVIDAD_ALTA  = 2;
	private final int ACTIVIDAD_EDITAR  = 3;
	private final int ACTIVIDAD_BAJA  = 4;
	private final int ACTIVIDAD_CONECTAR  = 5;
	private static final int QUERY  = 1;
	private static final int CONNECT  = 5;
	
	private int actividad;
	private EditText dni;
	private ImageButton btnConsulta;
	private ImageButton btnAlta;
	private ImageButton btnEdicion;
	private ImageButton btnBaja;
    // Progress Dialog
    private ProgressDialog pDialog;
    private boolean onPause = false;
    private boolean inicio = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
    	new Connect().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		onPause = true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (onPause) {
			if (inicio) {
				crearVistaPrincipal();
				
				inicio = false;
			}
			
			onPause = false;
		}
	}

	private void crearVistaPrincipal() {
		setContentView(R.layout.activity_principal);
		
		btnConsulta = (ImageButton) findViewById(R.id.btnConsulta);
		btnAlta = (ImageButton) findViewById(R.id.btnAgregar);
		btnEdicion = (ImageButton) findViewById(R.id.btnEditar);
		btnBaja = (ImageButton) findViewById(R.id.btnBorrar);
		dni = (EditText) findViewById(R.id.editDNIPrincipal);
		dni.setText("");
		
		btnConsulta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setActividad(ACTIVIDAD_CONSULTA);
				conectar(dni.getText().toString());				
			}
		});
		
		btnAlta.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!dni.getText().toString().equals("")) {
					setActividad(ACTIVIDAD_ALTA);
					conectar(dni.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.debe_introducir_dni), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		btnEdicion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!dni.getText().toString().equals("")) {
					setActividad(ACTIVIDAD_EDITAR);
					conectar(dni.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.debe_introducir_dni), Toast.LENGTH_SHORT).show();
				};
			}
		});
		
		btnBaja.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!dni.getText().toString().equals("")) {
					setActividad(ACTIVIDAD_BAJA);
					conectar(dni.getText().toString());
				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.debe_introducir_dni), Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public int getActividad() {
		return actividad;
	}

	public void setActividad(int actividad) {
		this.actividad = actividad;
	}
    
    private void conectar(String valor){
    	// env’o de informaci—n v’a par‡metros
    	BasicNameValuePair nameValuePairs = new BasicNameValuePair("DNI",valor);
    	// env’o de informaci—n v’a entidad
    	// String content = "[{\"DNI\":\"04864868\"}]";
    	new QueryDNI().execute(nameValuePairs);
    }

	private void lanzarActividad(JSONArray respJSON) throws JSONException {
		if (getActividad() == ACTIVIDAD_CONSULTA) {
			consultar(respJSON);
		} else if (getActividad() == ACTIVIDAD_ALTA) {
			agregar(respJSON);
		} else if (getActividad() == ACTIVIDAD_EDITAR) {
			editar(respJSON);
		} else if (getActividad() == ACTIVIDAD_BAJA) {
			borrar(respJSON);
		}
	}
	
	private void consultar(JSONArray respJSON) throws JSONException {
		int numRegistros = respJSON.getJSONObject(0).getInt("NUMREG");
		
		if (numRegistros == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.registro_no_existente), Toast.LENGTH_SHORT).show();
		} else {
	   		Intent i = new Intent(this, Consulta.class);		
			i.putExtra("numRegistros", numRegistros);
			i.putExtra("datos", respJSON.toString());
	   		startActivityForResult(i,ACTIVIDAD_CONSULTA);
		}
	}
	
	private void agregar(JSONArray respJSON) throws JSONException {
		int numRegistros = respJSON.getJSONObject(0).getInt("NUMREG");
		
		if (numRegistros == 0) {
	   		Intent i = new Intent(this, Alta.class);
	   		i.putExtra("dni", dni.getText().toString());
	   		startActivityForResult(i,ACTIVIDAD_ALTA);
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.registro_existente), Toast.LENGTH_SHORT).show();
		}
	}
	
	private void editar(JSONArray respJSON) throws JSONException {
		int numRegistros = respJSON.getJSONObject(0).getInt("NUMREG");
		
		if (numRegistros == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.registro_no_existente), Toast.LENGTH_SHORT).show();
		} else {
	   		Intent i = new Intent(this, Edicion.class);		
			i.putExtra("numRegistros", numRegistros);
			i.putExtra("datos", respJSON.toString());
	   		startActivityForResult(i,ACTIVIDAD_EDITAR);
		}
	}
	
	private void borrar(JSONArray respJSON) throws JSONException {
		int numRegistros = respJSON.getJSONObject(0).getInt("NUMREG");
		
		if (numRegistros == 0) {
			Toast.makeText(getApplicationContext(), getString(R.string.registro_no_existente), Toast.LENGTH_SHORT).show();
		} else {
	   		Intent i = new Intent(this, Baja.class);
			i.putExtra("datos", respJSON.toString());
	   		startActivityForResult(i,ACTIVIDAD_BAJA);
		}
	}
	
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)	{
    	if (requestCode == ACTIVIDAD_ALTA) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
        	        int salida = extras.getInt("Salida");
        	        if (salida == 1) {
        	        	Toast.makeText(this, getString(R.string.insercion_realizada), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, getString(R.string.insercion_cancelada), Toast.LENGTH_SHORT).show();
					}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.insercion_cancelada), Toast.LENGTH_SHORT).show();
    		}
		} else if (requestCode == ACTIVIDAD_BAJA) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
	    	        int salida = extras.getInt("Salida");
	    	        if (salida == 1) {
	    	        	Toast.makeText(this, getString(R.string.borrado_realizado), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, getString(R.string.borrado_cancelado), Toast.LENGTH_SHORT).show();
					}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.borrado_cancelado), Toast.LENGTH_SHORT).show();
    		}
		} else if (requestCode == ACTIVIDAD_EDITAR) {
    		if (resultCode == RESULT_OK) {
    	        Bundle extras = data.getExtras();
    	        if(extras != null) {
	    	        int salida = extras.getInt("Salida");
	    	        if (salida == 1) {
	    	        	Toast.makeText(this, getString(R.string.actualizacion_realizada), Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(this, getString(R.string.actualizacion_cancelada), Toast.LENGTH_SHORT).show();
					}
    	        }
    		} else {
    			Toast.makeText(this, getString(R.string.actualizacion_cancelada), Toast.LENGTH_SHORT).show();
    		}
		} else if (requestCode == ACTIVIDAD_CONECTAR) {
    		if (resultCode == RESULT_OK) {
				Toast.makeText(this, getString(R.string.servicio_conectado), Toast.LENGTH_SHORT).show();

    		} else {
    			Toast.makeText(this, getString(R.string.no_se_ha_podido_establecer_la_conexion), Toast.LENGTH_SHORT).show();
    		}
		}
	}
    
    
    /**
     * Background Async Task to Query 
     * */
    class QueryDNI extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Principal.this);
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
    	        HttpPost httppost = new HttpPost(new Url(QUERY, Principal.this).toString());
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
	            	//InputStream is = entity.getContent();
	                String responseString;
					try {
						responseString = EntityUtils.toString(entity);
						message = responseString;
						JSONArray messageJson = new JSONArray(message);
						lanzarActividad(messageJson);
					} 
					catch (ParseException e) {}
					catch (IOException e) {} catch (JSONException e) {
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.principal, menu);
		return true;
	}
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
    	super.onMenuItemSelected(featureId, item);
    	switch(item.getItemId()) {
           case R.id.configurarConexion:
               configurarConexion();
               break;
    	}
    	return true;
    }
	
	private void configurarConexion() {
   		Intent i = new Intent(this, ConfiguracionConexion.class);
   		startActivity(i);
	}
    
    
    /**
     * Background Async Task to Query 
     * */
    class Connect extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Principal.this);
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
       	
        	try{
        		
    	        //HttpClient httpclient = new DefaultHttpClient();
    	        httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost httppost = new HttpPost(new Url(CONNECT, Principal.this).toString());
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
	            	//InputStream is = entity.getContent();
	                String responseString;
					try {
						responseString = EntityUtils.toString(entity);
						message = responseString;
						JSONArray messageJson = new JSONArray(message);
						comprobarConexion(messageJson);
					} 
					catch (ParseException e) {}
					catch (IOException e) {} catch (JSONException e) {
						e.printStackTrace();
					}
	            } else {
	            	message = responseCode+": "+responseMessage;
	            }
			} else {
				try {
					comprobarConexion(null);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
        }
 
    }
	
	private void comprobarConexion(JSONArray messageJson) throws JSONException{
		int salida = -1;
		
		if (messageJson != null) {
			JSONObject json = messageJson.getJSONObject(0);
			salida = json.getInt("NUMREG");
		}

		if (salida == -1) {
			Intent i = new Intent(this, ConfiguracionConexion.class);
			startActivity(i);
			Toast.makeText(getApplicationContext(), getString(R.string.no_se_ha_podido_establecer_la_conexion), Toast.LENGTH_SHORT).show();
		} else {
			crearVistaPrincipal();
		}
	}

}
