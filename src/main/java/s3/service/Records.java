package s3.service;

import com.google.gson.*;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by root on 1/11/18.
 */
@Component
public class Records {

        String eventName;
        String eventTime;
        String eventSource;
        s31 s3;
        String awsRegion;
        RequestParameters requestParameters;




    public HashMap<String, String> deserializeObject(String messageBody) {
        HashMap<String, String> hmap = new HashMap<String, String>();

        String recordJson = messageBody;
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject)jsonParser.parse(recordJson);
        JsonArray arr = jo.getAsJsonArray("Records");
       JsonElement recordObject=  arr.get(0);
        Gson gson = new Gson();
        Records record = gson.fromJson(recordObject, Records.class);
        hmap.put("eventName",record.eventName);hmap.put("eventTime",record.eventTime);hmap.put("eventSource",record.eventSource);hmap.put("awsRegion",record.awsRegion);hmap.put("requestParameters",record.requestParameters.sourceIPAddress);
        hmap.put("eTag",record.s3.object.eTag);hmap.put("key",record.s3.object.key);hmap.put("sequencer",record.s3.object.sequencer);hmap.put("configurationId",record.s3.configurationId);hmap.put("s3SchemaVersion",record.s3.s3SchemaVersion);hmap.put("bucket-arn",record.s3.bucket.arn);hmap.put("bucket-name",record.s3.bucket.name);
        return hmap;

    }

}
class Bucket {
    String name;
    String arn;
}
class Object1 {
    String size;
    String key;
    String eTag;
    String sequencer;
}

class s31 {
    String s3SchemaVersion;
    String configurationId;
    Bucket bucket;
    Object1 object;

}
class RequestParameters {
    String sourceIPAddress;
}


