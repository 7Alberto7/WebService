package master.actividades.servicioweb;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Url {

	private static final String url_http = "http://";
	private static final String url_query = "/webservice/webService.query.php";
	private static final String url_conectar = "/webservice/webService.connect.php";
	private static final String url_insertar = "/webservice/webService.insert.php";
	private static final String url_borrar = "/webservice/webService.delete.php";
	private static final String url_editar = "/webservice/webService.update.php";
	private static final String url_propiedades_conexion = "/webservice/webService.connection_properties.php";

	private static final int QUERY  = 1;
	private static final int INSERT  = 2;
	private static final int DELETE  = 3;
	private static final int UPDATE  = 4;
	private static final int CONNECT  = 5;
	private static final int CONNECTION_PROP  = 6;
	
	private int accion;
	private Context contexto;
	

	public Url(int accion, Context contexto) {
		this.accion = accion;
		this.contexto = contexto;
	}
	
	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public Context getContexto() {
		return contexto;
	}

	public void setContexto(Context contexto) {
		this.contexto = contexto;
	}

	@Override
	public String toString() {
		String url = "";
	    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this.getContexto());
	    
	    if (this.getAccion() == QUERY) {
			url = url_http + pref.getString("url", "") + url_query;
		} else if (this.getAccion() == INSERT) {
			url = url_http + pref.getString("url", "") + url_insertar;
		} else if (this.getAccion() == DELETE) {
			url = url_http + pref.getString("url", "") + url_borrar;
		} else if (this.getAccion() == UPDATE) {
			url = url_http + pref.getString("url", "") + url_editar;
		} else if (this.getAccion() == CONNECT) {
			url = url_http + pref.getString("url", "") + url_conectar;
		} else if (this.getAccion() == CONNECTION_PROP) {
			url = url_http + pref.getString("url", "") + url_propiedades_conexion;
		}
		return url;
	}
}
