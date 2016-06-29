package br.com.mblabs.facebookeasy.business;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.mblabs.facebookeasy.data.dao.FacebookDAO;
import br.com.mblabs.facebookeasy.data.dao.FacebookDAOException;
import br.com.mblabs.facebookeasy.data.model.FacebookFriend;
import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public class LibFacebookBO implements FacebookBO {

    private static final String TAG = LibFacebookBO.class.getSimpleName();

    private final FacebookDAO mFacebookDAO;
    private final LoginManager mLoginManager;

    private CallbackManager mCallbackManager;

    public LibFacebookBO(@NonNull final FacebookDAO facebookDAO, @NonNull final LoginManager loginManager) {
        mFacebookDAO = facebookDAO;
        mLoginManager = loginManager;
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void login(@NonNull List<String> permissions, @NonNull final AppFacebookLoginListener callback) {
        mLoginManager.registerCallback(mCallbackManager, new FacebookLoginCallback(callback));

        try {
            mFacebookDAO.login(mLoginManager, permissions);
        } catch (FacebookDAOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void logout() {
        mFacebookDAO.logout(mLoginManager);
    }

    @Override
    public AccessToken getAccessToken() {
        return mFacebookDAO.getAccessToken();
    }

    @Override
    public FacebookUser getFacebookUser() {
        return mFacebookDAO.getFacebookUser();
    }

    @Override
    public void getFacebookUserFriends(@NonNull final FacebookBO.AppFacebookFriendsListener callback) {
        mFacebookDAO.getFacebookUserFriends(new FacebookFriendsListener(callback));
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {

        private AppFacebookLoginListener mCallback;

        public FacebookLoginCallback(AppFacebookLoginListener callback) {
            mCallback = callback;
        }

        @Override
        public void onSuccess(@NonNull final LoginResult loginResult) {
            final AccessToken accessToken = loginResult.getAccessToken();
            mCallback.onFacebookSuccess(accessToken.getUserId(), accessToken.getToken());
        }

        @Override
        public void onError(final FacebookException e) {
            mCallback.onFacebookError();
        }

        @Override
        public void onCancel() {
            mCallback.onFacebookCancel();
        }
    }

    private class FacebookFriendsListener implements GraphRequest.GraphJSONArrayCallback {

        private final AppFacebookFriendsListener mCallback;

        public FacebookFriendsListener(AppFacebookFriendsListener callback) {
            mCallback = callback;
        }

        @Override
        public void onCompleted(JSONArray objects, GraphResponse response) {
            try {

                if (objects != null) {

                    List<FacebookFriend> friends = new ArrayList<>();

                    final Gson gson = new GsonBuilder().create();

                    for (int i = 0; i < objects.length(); i++) {
                        JSONObject object = objects.getJSONObject(i);
                        FacebookFriend friend = gson.fromJson(object.toString(), FacebookFriend.class);
                        friends.add(friend);
                    }

                    mCallback.onFriendsSuccess(friends);
                }

            } catch (Exception ex) {
                Log.e(TAG, "Error parsing facebook response: " + ex.getMessage());
                mCallback.onFriendsError();
            }
        }
    }
}
