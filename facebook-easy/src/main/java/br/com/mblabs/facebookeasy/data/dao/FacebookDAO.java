package br.com.mblabs.facebookeasy.data.dao;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;

import java.util.List;

import br.com.mblabs.facebookeasy.data.model.FacebookUser;

public interface FacebookDAO {

    void login(@NonNull LoginManager loginManager, @NonNull List<String> permissions) throws FacebookDAOException;

    void logout(@NonNull LoginManager loginManager);

    AccessToken getAccessToken();

    @Nullable
    FacebookUser getFacebookUser();

    void getFacebookUserFriends(@NonNull final GraphRequest.GraphJSONArrayCallback callback);

}
