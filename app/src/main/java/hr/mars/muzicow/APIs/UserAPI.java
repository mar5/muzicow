package hr.mars.muzicow.APIs;

import java.util.List;

import hr.mars.muzicow.Models.DJ;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by mars on 25/12/15.
 */
public interface UserAPI {

    @GET("/api/{ID}")
    void getDJ(@Path(value="ID", encode=false) String st, Callback<List<DJ>> cb);
}
