package circle;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.protos.tbd.pfi.CreateBankAccountRequest;
import com.squareup.protos.tbd.pfi.CreateWirePaymentRequest;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class CircleClient {
  private static final Gson gson = new GsonBuilder()
      .create();
  private static final String BEARER_KEY =
      "QVBJX0tFWTo4YTkzN2I3ZDE3NjIwYjFlN2I2ZmI0YzI3NjFhOWU5Njo2OTdmMDgwYjI1Y2RlNTE3OTg5NGEwNmNlY2M2YWVjMg==";

  public static Request.Builder requestBuilder = new Request.Builder()
      .addHeader("Accept", "application/json")
      .addHeader("Content-Type", "application/json")
      .addHeader("Authorization", "Bearer " + BEARER_KEY);

  public static String createBankAccount(CreateBankAccountRequest createBankAccountRequest)
      throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, gson
        .toJson(createBankAccountRequest));

    Request request = requestBuilder
        .url("https://api-sandbox.circle.com/v1/banks/wires")
        .post(body)
        .build();

    Response response = client.newCall(request).execute();
    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getJSONObject("data").getString("trackingRef");
  }

  public static void createWirePayment(CreateWirePaymentRequest createWirePaymentRequest)
      throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, gson.toJson(createWirePaymentRequest));

    Request request = requestBuilder
        .url("https://api-sandbox.circle.com/v1/mocks/payments/wire")
        .post(body)
        .build();

    Response response = client.newCall(request).execute();

    System.out.println(response.body().string());
  }
}
