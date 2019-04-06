package api;

import model.UserProfileBody;
import model.profile.Profile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface MyProfileInterface {

    @GET("profile")
    Call<Profile> getUserProfile();

    @POST("profile")
    Call<ResponseBody> postUserProfile(@Body Profile user);

    @PUT("profile/{userId}")
    Call<ResponseBody> modifyUserProfile(@Body Profile user, @Path("userId") String userId);

    @DELETE("tvusers/{userId}")
    Call<ResponseBody> deleteUserProfile(@Path("userId") String userId);

}
