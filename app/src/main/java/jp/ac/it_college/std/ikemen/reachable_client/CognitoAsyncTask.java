package jp.ac.it_college.std.ikemen.reachable_client;

import android.content.Context;
import android.os.AsyncTask;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

/**
 * Cognito経由でCredentialsを取得するAsyncTaskクラス
 */
public class CognitoAsyncTask extends AsyncTask<Void, Void, AWSCredentials> {
    private Context mContext;

    public CognitoAsyncTask(Context context) {
        this.mContext = context;
    }

    @Override
    protected AWSCredentials doInBackground(Void... voids) {
        CognitoCachingCredentialsProvider provider = new CognitoCachingCredentialsProvider(
                getContext(),
                Constants.IDENTITY_POOL_ID,
                Regions.AP_NORTHEAST_1
        );

        return provider.getCredentials();
    }

    @Override
    protected void onPostExecute(AWSCredentials awsCredentials) {
        super.onPostExecute(awsCredentials);
    }

    public Context getContext() {
        return mContext;
    }
}
