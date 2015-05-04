package it.cnr.iit.thesapp.api;


import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public class Api {
	public static final String ENDPOINT = "http://146.48.65.88";
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
				   @Header("Accept-Language") String language, Callback<TermSearch> callback);

		@GET("/search")
		TermSearch query(@Query("query") String queryString, @Query("domain") String domain,
						 @Header("Accept-Language") String language);

		@GET("/terms")
		void term(@Query("descriptor") String descriptor, @Query("domain") String domain,
				  @Header("Accept-Language") String language, Callback<Term> callback);

		@GET("/domains")
		void domains(@Header("Accept-Language") String language, Callback<DomainSearch> callback);
	}
}
