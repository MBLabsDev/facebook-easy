package br.com.mblabs.facebookeasy.data.dao;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.internal.ImageRequest;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public class LibFacebookDAO implements FacebookDAO {

    private static final String TAG = LibFacebookDAO.class.getSimpleName();

    private final android.app.Fragment mFragment;
    private final Fragment mFragmentV4;
    private final Activity mActivity;

    public LibFacebookDAO(@NonNull final Activity activity) {
        mActivity = activity;
        mFragmentV4 = null;
        mFragment = null;
    }

    public LibFacebookDAO(@NonNull final Fragment fragment) {
        mFragmentV4 = fragment;
        mActivity = null;
        mFragment = null;
    }

    public LibFacebookDAO(@NonNull final android.app.Fragment fragment) {
        mFragment = fragment;
        mFragmentV4 = null;
        mActivity = null;
    }

    @Override
    public void login(@NonNull LoginManager loginManager, @NonNull List<String> permissions) throws FacebookDAOException {
        if (mFragmentV4 != null) {
            loginManager.logInWithReadPermissions(mFragmentV4, permissions);
        } else if (mFragment != null) {
            loginManager.logInWithReadPermissions(mFragment, permissions);
        } else if (mActivity != null) {
            loginManager.logInWithReadPermissions(mActivity, permissions);
        } else {
            throw new FacebookDAOException("Fragment and Activity instances both null");
        }
    }

    @Override
    public void logout(@NonNull LoginManager loginManager) {
        loginManager.logOut();
    }

    @Override
    public AccessToken getAccessToken() {
        return AccessToken.getCurrentAccessToken();
    }

    @Nullable
    @Override
    public FacebookUser getFacebookUser() {
        try {
            return new GraphResponseProfileAsyncTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    @Override
    public void getFacebookUserFriends(@NonNull final GraphRequest.GraphJSONArrayCallback callback) {
        try {
            new GraphResponseFriendsAsyncTask(callback).execute().get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e(TAG, e.toString());
        }
    }

    private class GraphResponseProfileAsyncTask extends AsyncTask<String, Integer, FacebookUser> {

        @Override
        protected FacebookUser doInBackground(String... params) {
            final AccessToken token = AccessToken.getCurrentAccessToken();
            final GraphRequest request = GraphRequest.newMeRequest(token, null);

            final Bundle parameters = new Bundle();
            parameters.putString("fields", "id, first_name, last_name, email, gender, birthday");
            request.setParameters(parameters);

            final GraphResponse response = request.executeAndWait();
            return parseUserFacebookFromResponse(response);
        }
    }

    private FacebookUser parseUserFacebookFromResponse(@NonNull final GraphResponse response) {
        final JSONObject object = response.getJSONObject();

        FacebookUser user;
        if (object == null) {
            Log.e(TAG, "Null JSONObject");
            return null;
        }

        final Gson gson = new GsonBuilder().create();
        user = gson.fromJson(object.toString(), FacebookUser.class);

        if (user.getId() != null && !user.getId().isEmpty()) {
            user.setPhoto(ImageRequest.getProfilePictureUri(user.getId(), 512, 512).toString());
        }

        return user;
    }

    private class GraphResponseFriendsAsyncTask extends AsyncTask<String, Integer, Void> {

        private final GraphRequest.GraphJSONArrayCallback mCallback;

        public GraphResponseFriendsAsyncTask(@NonNull final GraphRequest.GraphJSONArrayCallback callback) {
            mCallback = callback;
        }

        @Override
        protected Void doInBackground(String... params) {
            final AccessToken token = AccessToken.getCurrentAccessToken();
            final GraphRequest request = GraphRequest.newMyFriendsRequest(token, mCallback);
            request.executeAndWait();
            return null;
        }
    }
}
