package api;

import model.ProfileResponse;
import model.UserProfileBody;
import model.profileModel.UserProfile;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by cognoscis on 6/4/18.
 */

public interface ProfileApiInterface {

//    @Headers({"Accept-Version: 1.0.0"})
//    @GET("profile-pref")
//    Call<ProfilePreferencesResponse> getProfileConfig();

//    @Headers({"Accept-Version: 1.0.0"})
//    @POST("new-tvusers")
//    Call<ResponseBody> postUserProfile(@Body UserProfileBody user);
//
//    @Headers({"Accept-Version: 1.0.0"})
//    @PUT("tvusers/{userId}")
//    Call<ResponseBody> modifyUserProfile(@Body UserProfileBody user, @Path("userId") String userId);
//
//    @Headers({"Accept-Version: 1.0.0"})
//    @DELETE("tvusers/{userId}")
//    Call<ResponseBody> deleteUserProfile(@Path("userId") String userId);

//    @Headers({"Accept-Version: 1.0.0"})
//    @GET("tvusers/all")
//    Call<ProfileResponse> getAllProfiles();



    @POST("profile")
    Call<ResponseBody> postUserProfile(@Body UserProfile userProfileObj);

    @GET("profile/{googleId}")
    Call<UserProfile> getUserProfile(@Path("googleId") String googleId);


    @PUT("profile/{googleId}")
    Call<UserProfile> modifyUserProfile(@Body UserProfile userProfile, @Path("googleId") String googleId);

//
//    @POST("linkdevice")
//    Call<tvInfoList> postNewTvDevice(@Body tvinfo tvInfoListObj);
//
//
//    @GET("linkdevice/{googleId}")
//    Call<tvInfoList> getLinkDevices(@Path("googleId") String googleId);
//
//
//    @DELETE("linkdevice/{googleId")
//    Call<tvInfoList> removeTvDevice(@Path("googleId") String googleId);

}
