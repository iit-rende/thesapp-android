package it.cnr.iit.thesapp.api;


import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import it.cnr.iit.thesapp.event.SetSearchDelayEvent;
import it.cnr.iit.thesapp.model.Category;
import it.cnr.iit.thesapp.model.CategoryList;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.model.TermSearch;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Query;

public class Api {

	public static final String ENDPOINT = "http://146.48.65.88";
	private final ThesAppService service;

	public Api() {

		RestAdapter restAdapter =
				new RestAdapter.Builder()//.setLogLevel(RestAdapter.LogLevel.BASIC)
						.setClient(new InterceptingOkClient()).setEndpoint(ENDPOINT).build();
		service = restAdapter.create(ThesAppService.class);
	}

	public static long getSearchInterval(Response response) {
		for (retrofit.client.Header header : response.getHeaders()) {
			if (header.getName().equals("X-Search-Interval")) {
				return Long.parseLong(header.getValue());
			}
		}
		return -1;
	}

	public ThesAppService getService() {
		return service;
	}

	public interface ThesAppService {

		@GET("/search")
		void query(@Query("query") String queryString, @Query("domain") String domain,
		           @Query("category") String category, @Header("Accept-Language") String language,
		           Callback<TermSearch> callback);

		@GET("/search")
		TermSearch query(@Query("query") String queryString, @Query("domain") String domain,
		                 @Query("category") String category,
		                 @Header("Accept-Language") String language);

		@GET("/terms")
		void term(@Query("descriptor") String descriptor, @Query("domain") String domain,
		          @Header("Accept-Language") String language, Callback<Term> callback);

		@GET("/hierarchy")
		void category(@Query("category") String descriptor, @Query("domain") String domain,
		              @Header("Accept-Language") String language, Callback<Category> callback);

		@GET("/hierarchy")
		void categoryList(@Query("domain") String domain,
		                  @Header("Accept-Language") String language,
		                  Callback<CategoryList> callback);

		@GET("/domains")
		void domains(@Header("Accept-Language") String language, Callback<DomainSearch> callback);
	}

	public class InterceptingOkClient extends OkClient {

		public InterceptingOkClient() {
		}

		public InterceptingOkClient(OkHttpClient client) {
			super(client);
		}

		@Override
		public Response execute(Request request) throws
				IOException {
			Response response = super.execute(request);
			EventBus.getDefault().post(new SetSearchDelayEvent(getSearchInterval(response)));

			return response;
		}
	}
}
