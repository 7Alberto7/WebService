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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ConfiguracionConexion extends PreferenceActivity {

	private static final int CONNECTION_PROP  = 6;
    private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.configuracion);
	}
/*
	public static class OpcionesFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        addPreferencesFromResource(R.xml.configuracion);
	    }
	}*/
	
    @Override
	public void onBackPressed() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

		BasicNameValuePair nameValuePairs1 = new BasicNameValuePair("usuario",pref.getString("usuario", ""));
		BasicNameValuePair nameValuePairs2 = new BasicNameValuePair("clave",pref.getString("contrasenia", ""));

		new ConnectionProp().execute(nameValuePairs1, nameValuePairs2);
	}
    
    
    /**
     * Background Async Task to Query 
     * */
    class ConnectionProp extends AsyncTask <BasicNameValuePair, Void, HttpResponse> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ConfiguracionConexion.this);
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
        	
        	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(params.length);
        	for(int i=0; i<params.length; i++){
        		nameValuePairs.add(params[i]);
        	}
        	
        	try{
    	        //HttpClient httpclient = new DefaultHttpClient();
    	        AndroidHttpClient httpclient = AndroidHttpClient.newInstance("AndroidHttpClient");
    	        HttpPost httppost = new HttpPost(new Url(CONNECTION_PROP, ConfiguracionConexion.this).toString());
    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
    	        //httppost.setEntity(new StringEntity(params[0]));
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
        }
 
    }
	
	private void lanzarActividad(JSONArray messageJson) throws JSONException{
		JSONObject json = messageJson.getJSONObject(0);
		int salida = json.getInt("NUMREG");

		if (salida == 0) {
			Intent i = new Intent();
	   		setResult(RESULT_OK, i);
			finish();
		} else {
			Toast.makeText(this, getString(R.string.no_se_ha_podido_establecer_la_conexion), Toast.LENGTH_SHORT).show();
		}
	}
}
