package it.cnr.iit.thesapp.api;


import java.util.List;

import it.cnr.iit.thesapp.model.Term;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public class Api {
	public static final String ENDPOINT = "http://nsapi.e-lios.eu/App/API/";
	private final ThesAppService service;

	public Api() {

		RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(ENDPOINT).build();
		service = restAdapter.create(ThesAppService.class);
	}

	public ThesAppService getService() {
		return service;
	}

	public interface ThesAppService {
		@GET("/search")
		void query(@Query("query") String queryString, @Query("domain") String domain,
				   @Header("Accept-Language") String language, Callback<List<Term>> callback);

		@GET("/terms")
		void term(@Query("descriptor") String descriptor, @Query("domain") String domain,
				  @Header("Accept-Language") String language, Callback<Term> callback);
	}
}
