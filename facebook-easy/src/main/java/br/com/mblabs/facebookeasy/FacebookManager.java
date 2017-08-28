package br.com.mblabs.facebookeasy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.util.List;

import br.com.mblabs.facebookeasy.business.FacebookBO;
import br.com.mblabs.facebookeasy.business.LibFacebookBO;
import br.com.mblabs.facebookeasy.data.dao.LibFacebookDAO;
import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public class FacebookManager {

    private static final String TAG = FacebookManager.class.getSimpleName();

    private List<String> mPermission;
    private FacebookBO mFacebookBO;

    public FacebookManager(@NonNull final Activity activity, @NonNull final List<String> permissions) {
        this(activity.getBaseContext());
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(activity), LoginManager.getInstance());
    }

    public FacebookManager(@NonNull final Fragment fragment, @NonNull final List<String> permissions) {
        this(fragment.getActivity().getBaseContext());
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(fragment), LoginManager.getInstance());
    }

    public FacebookManager(@NonNull final android.support.v4.app.Fragment fragment, @NonNull final List<String> permissions) {
        this(fragment.getContext());
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(fragment), LoginManager.getInstance());
    }

    private FacebookManager(@NonNull final Context context) {
        FacebookSdk.sdkInitialize(context);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mFacebookBO.onActivityResult(requestCode, resultCode, data);
    }

    public AccessToken requestToken() throws FacebookManagerException {
        if(mFacebookBO.getAccessToken() != null) {
            return mFacebookBO.getAccessToken();
        } else {
            throw new FacebookManagerException("User not logged");
        }
    }

    public FacebookUser requestFacebookUser() throws FacebookManagerException {
        if(mFacebookBO.getAccessToken() != null) {
            return mFacebookBO.getFacebookUser();
        } else {
            throw new FacebookManagerException("User not logged");
        }
    }

    public void disconnect() {
        if(mFacebookBO.getAccessToken() != null) {
            mFacebookBO.logout();
        }
    }

    public void connect(final FacebookBO.AppFacebookLoginListener listener) {
        mFacebookBO.login(mPermission, listener);
    }

    public void requestMyAppFriends(final FacebookBO.AppFacebookFriendsListener listener) {
        mFacebookBO.getFacebookUserFriends(listener);
    }
}
