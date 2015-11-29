package jp.ac.it_college.std.ikemen.reachable.client;

import android.content.Context;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;

import java.util.concurrent.ExecutionException;

public class AwsManager {
    private static CognitoCachingCredentialsProvider sCredProvider;
    private static AmazonS3Client sS3Client;
    private static TransferUtility sTransferUtility;


    private static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if (sCredProvider == null) {
            sCredProvider = new CognitoCachingCredentialsProvider(
                    context,
                    Constants.IDENTITY_POOL_ID,
                    Regions.AP_NORTHEAST_1
            );
        }

        return sCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if (sS3Client == null) {
            sS3Client = new AmazonS3Client(getCredProvider(context));
            sS3Client.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
        }
        return sS3Client;
    }

    public static TransferUtility getTransferUtility(Context context) {
        if (sTransferUtility == null) {
            sTransferUtility = new TransferUtility(getS3Client(context), context);
        }
        return sTransferUtility;
    }
}
