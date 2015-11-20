package jp.ac.it_college.std.ikemen.reachable_client;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

/**
 * Cognito経由でCredentialsを取得するAsyncTaskLoaderクラス
 */
public class CognitoAsyncTaskLoader extends AsyncTaskLoader<AWSCredentials>{

    public CognitoAsyncTaskLoader(Context context) {
        super(context);
    }

    @Override
    public AWSCredentials loadInBackground() {
        //Credentialsを取得
        CognitoCachingCredentialsProvider provider = new CognitoCachingCredentialsProvider(
                getContext(),
                Constants.IDENTITY_POOL_ID,
                Regions.AP_NORTHEAST_1
        );

        return provider.getCredentials();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
