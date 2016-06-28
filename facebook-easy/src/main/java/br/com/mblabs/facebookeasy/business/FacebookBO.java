package br.com.mblabs.facebookeasy.business;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import java.util.List;

import br.com.mblabs.facebookeasy.data.model.FacebookFriend;
import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public interface FacebookBO {

    void login(@NonNull List<String> permissions, @NonNull AppFacebookLoginListener listener);

    void logout();

    AccessToken getAccessToken();

    FacebookUser getFacebookUser();

    void getFacebookUserFriends(@NonNull final FacebookBO.AppFacebookFriendsListener callback);

    void onActivityResult(final int requestCode, final int resultCode, final Intent data);

    interface AppFacebookLoginListener {

        void onFacebookSuccess(@NonNull final String facebookId, @NonNull final String token);

        void onFacebookError();

        void onFacebookCancel();
    }

    interface AppFacebookFriendsListener {

        void onFriendsSuccess(@NonNull final List<FacebookFriend> friends);

        void onFriendsError();
    }
}
