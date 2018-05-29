package co.sjsu.xuy87cmpe277certpin.retrofit;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;

public interface httpServ {

    @GET("/users/{user}")
    @Headers("User-Agent: hello-pinnedcerts")
    void getCallBack(
            @Path("user") String user,
            Callback<User> callback
    );
}
