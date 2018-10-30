package s3.service;

import com.google.gson.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by root on 29/10/18.
 */
public class Decode {
    static Logger logger = LoggerFactory.getLogger(Decode.class);

    public static void main(String[] args) throws JSONException {
        String jsonbobj="{\"Records\":[{\"eventVersion\":\"2.0\",\"eventSource\":\"aws:s3\",\"awsRegion\":\"ap-south-1\",\"eventTime\":\"2018-10-29T16:30:27.433Z\",\"eventName\":\"ObjectCreated:Put\",\"userIdentity\":{\"principalId\":\"ARJEK1Q4JXHM1\"},\"requestParameters\":{\"sourceIPAddress\":\"106.51.136.38\"},\"responseElements\":{\"x-amz-request-id\":\"B8B1F482AD213225\",\"x-amz-id-2\":\"zhCRDywQkVEfH2perwekfzAcpNzhLnbB6ffaLyzJuZkb4IFnO6ci5eZ5R/JU81eYYUgKTBMWAbk=\"},\"s3\":{\"s3SchemaVersion\":\"1.0\",\"configurationId\":\"s3putObject-sns\",\"bucket\":{\"name\":\"s3objectbucket\",\"ownerIdentity\":{\"principalId\":\"ARJEK1Q4JXHM1\"},\"arn\":\"arn:aws:s3:::s3objectbucket\"},\"object\":{\"key\":\"uploads/a.txt\",\"size\":3,\"eTag\":\"d404401c8c6495b206fc35c95e55a6d5\",\"sequencer\":\"005BD735A35B217A2E\"}}}]}";
        JSONObject jsonObject = new JSONObject(jsonbobj);
        JSONArray array = jsonObject.getJSONArray("Records");
        JSONObject object = array.getJSONObject(0);
        logger.info(array.getClass().getName());
        logger.info(object.getClass().getName());
        logger.info("\n");
        JSONArray standard =object.getJSONArray("s3");
        for (int i = 0; i < standard.length(); i++) {
            String firstName = standard.getJSONObject(i).getString("object");
        }




    }
}
