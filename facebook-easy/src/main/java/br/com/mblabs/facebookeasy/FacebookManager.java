package br.com.mblabs.facebookeasy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.mblabs.facebookeasy.business.FacebookBO;
import br.com.mblabs.facebookeasy.business.LibFacebookBO;
import br.com.mblabs.facebookeasy.data.dao.LibFacebookDAO;
import br.com.mblabs.facebookeasy.data.model.FacebookFriend;
import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public class FacebookManager {

    private static final String TAG = FacebookManager.class.getSimpleName();
    private final List<String> mPermission;

    private final FacebookBO mFacebookBO;

    public FacebookManager(@NonNull final Activity activity, @NonNull final List<String> permissions) {
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(activity), LoginManager.getInstance());
    }

    public FacebookManager(@NonNull final Fragment fragment, @NonNull final List<String> permissions) {
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(fragment), LoginManager.getInstance());
    }

    public FacebookManager(@NonNull final android.support.v4.app.Fragment fragment, @NonNull final List<String> permissions) {
        mPermission = permissions;
        mFacebookBO = new LibFacebookBO(new LibFacebookDAO(fragment), LoginManager.getInstance());
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
