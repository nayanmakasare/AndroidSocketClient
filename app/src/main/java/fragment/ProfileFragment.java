package fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import Utils.PreferenceManager;
import de.hdodenhof.circleimageview.CircleImageView;
import tv.cloudwalker.androidsocketclient.R;

public class ProfileFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_profile_layout, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PreferenceManager preferenceManager = new PreferenceManager(view.getContext());

        Glide.with(this).load(preferenceManager.getProfileImageUrl()).into((CircleImageView)view.findViewById(R.id.profileImageFrag));

        ((TextView)view.findViewById(R.id.userNameTV)).setText(preferenceManager.getUserName());
        ((TextView)view.findViewById(R.id.userMobileTV)).setText(preferenceManager.getMobileNumber());
        ((TextView)view.findViewById(R.id.userGenderTV)).setText(preferenceManager.getGender());
        ((TextView)view.findViewById(R.id.userGenreTV)).setText(preferenceManager.getGenre());
        ((TextView)view.findViewById(R.id.userLanguageTV)).setText(preferenceManager.getLanguage());
        ((TextView)view.findViewById(R.id.userTypeTV)).setText(preferenceManager.getType());
        ((TextView)view.findViewById(R.id.userDobTV)).setText(preferenceManager.getDob());
        ((TextView)view.findViewById(R.id.userEmailTV)).setText(preferenceManager.getUserEmail());
        ((TextView)view.findViewById(R.id.userDeviceInfoTV)).setText(preferenceManager.getTvInfo());
    }
}
