package api;

import java.util.List;

import model.MovieResponse;
import newDeviceModel.TvInfo;
import newDeviceModel.NewUserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import youtube_models.YoutubePrimeObject;

public interface MyProfileInterface {

    @POST("/api/profile")
    Call<ResponseBody> postUserProfile(@Body NewUserProfile userProfileObj);

    @GET("/api/profile/{googleId}")
    Call<NewUserProfile> getUserProfile(@Path("googleId") String googleId);


    @PUT("/api/profile/{googleId}")
    Call<NewUserProfile> modifyUserProfile(@Body NewUserProfile newUserProfile, @Path("googleId") String googleId);


    @PUT("/api/linkdevice/{googleId}")
    Call<TvInfo> postNewTvDevice(@Body TvInfo tvInfoListObj, @Path("googleId") String googleId);


    @GET("/api/linkdevice/{googleId}")
    Call<List<TvInfo>> getLinkDevices(@Path("googleId") String googleId);


    @DELETE("/api/linkdevice/{googleId}")
    Call<List<TvInfo>> removeTvDevice(@Body TvInfo tvInfoListObj, @Path("googleId") String googleId);

    @Headers({"Accept-Version: 1.0.0"})
    @GET("cats")
    Call<MovieResponse> getHomeScreenData();

    @Headers({"Accept: application/json"})
    @GET("/youtube/v3/search")
    Call<YoutubePrimeObject> getYoutubeFeeds(@Query("part") String id, @Query("maxResults") String maxResults,  @Query("q") String searchString, @Query("key") String developerKey);

}
























