package wyre;

import com.squareup.protos.tbd.pfi.DepositAddresses;
import com.squareup.protos.tbd.pfi.WyreWallet;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class WyreClient {
  public static Request.Builder requestBuilder = new Request.Builder()
      .addHeader("Accept", "application/json")
      .addHeader("Content-Type", "application/json")
      .addHeader("Authorization", WyreController.BEARER);

  public static WyreWallet createWallet(String walletName) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(
        mediaType, String.format("{\"type\":\"DEFAULT\",\"name\":\"account:%s\"}", walletName)
    );
    Request request = requestBuilder
        .url("https://api.testwyre.com/v2/wallets")
        .post(body)
        .build();

    Response response = client.newCall(request).execute();
    JSONObject jsonResponse = new JSONObject(response.body().string());
    JSONObject jsonDepositAddresses = jsonResponse.getJSONObject("depositAddresses");
    DepositAddresses depositAddresses = new DepositAddresses.Builder()
        .btc_address(jsonDepositAddresses.getString("BTC"))
        .eth_address(jsonDepositAddresses.getString("ETH"))
        .build();
    return new WyreWallet.Builder()
        .id(jsonResponse.getString("id"))
        .deposit_addresses(depositAddresses)
        .srn(jsonResponse.getString("srn"))
        .build();
  }

  public static String createWalletOrderReservation(String account) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");

    RequestBody body = RequestBody.create(
        mediaType,
        String.format("{\"referrerAccountId\":\"%s\"}", account)
    );
    Request request = new Request.Builder()
        .url("https://api.testwyre.com/v3/orders/reserve")
        .post(body)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer SK-WUEY3J78-3FJBFVEM-MAZM7XHC-NGCW2G4F")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getString("reservation");
  }

  public static String executeWalletOrderReservation(
      String reservationId
  ) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType,
        String.format("{\"debitCard\":"
            + "{\"number\":\"4111111111111111\","
            + "\"year\":\"2023\","
            + "\"month\":\"01\","
            + "\"cvv\":\"123\"},"
            + "\"address\":{\"city\":\"Los Angeles\",\"state\":\"CA\",\"postalCode\":\"91423\",\"street1\":\"2000 E Madison St\",\"country\":\"US\"},"
            + "\"reservationId\":\"%s\","
            + "\"amount\":\"100\","
            + "\"sourceCurrency\":\"USD\","
            + "\"destCurrency\":\"USDC\","
            + "\"dest\":\"wallet:WA_8FPWBHUXMWR\","
            + "\"referrerAccountId\":\"AC_NE6PC8GTUYT\","
            + "\"givenName\":\"Hellen\","
            + "\"familyName\":\"Bandicoot\","
            + "\"email\":\"hdou@squareup.com\","
            + "\"phone\":\"8473733106\","
            + "\"ipAddress\":\"1.1.1.1\","
            + "\"referenceId\":\"AC_NE6PC8GTUYT\"}", reservationId)
    );
    Request request = new Request.Builder()
        .url("https://api.testwyre.com/v3/debitcard/process/partner")
        .post(body)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer SK-WUEY3J78-3FJBFVEM-MAZM7XHC-NGCW2G4F")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getString("id");
  }

  public static String getWalletOrder(String orderID) throws Exception {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(String.format("https://api.testwyre.com/v3/orders/%s", orderID))
        .get()
        .addHeader("Accept", "application/json")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getString("reservation");
  }

  public static String payout(String amount) throws Exception {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, String.format("{"
        + "\"autoConfirm\":true,"
        + "\"source\":\"wallet:WA_8FPWBHUXMWR\","
        + "\"sourceCurrency\":\"USDC\","
        + "\"destCurrency\":\"USD\","
        + "\"destAmount\":%s,"
        + "\"dest\":{"
        + "\"paymentMethodType\":\"INTERNATIONAL_TRANSFER\","
        + "\"country\":\"US\","
        + "\"currency\":\"USD\","
        + "\"paymentType\":\"LOCAL_BANK_WIRE\","
        + "\"firstNameOnAccount\":\"Billy-Bob\","
        + "\"lastNameOnAccount\":\"Jones\","
        + "\"accountNumber\":\"0000000000000\","
        + "\"routingNumber\":\"0000000000\","
        + "\"accountType\":\"CHECKING\","
        + "\"bankName\":\"JP Morgan\""
        + "}}", amount
    ));
    Request request = new Request.Builder()
        .url("https://api.testwyre.com/v2/transfers")
        .post(body)
        .addHeader("Accept", "application/json")
        .addHeader("Content-Type", "application/json")
        .addHeader("Authorization", "Bearer SK-WUEY3J78-3FJBFVEM-MAZM7XHC-NGCW2G4F")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getString("id");
  }

  public static String getTransferStatus(String transferId) throws Exception {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(String.format("https://api.testwyre.com/v3/transfers/%s", transferId))
        .get()
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", "Bearer SK-WUEY3J78-3FJBFVEM-MAZM7XHC-NGCW2G4F")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getString("status");
  }

  public static Integer getWalletBalance(String walletToken) throws Exception {
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder()
        .url(String.format("https://api.testwyre.com/v2/wallet/%s", walletToken))
        .get()
        .addHeader("Accept", "application/json")
        .addHeader("Authorization", "Bearer SK-WUEY3J78-3FJBFVEM-MAZM7XHC-NGCW2G4F")
        .build();

    Response response = client.newCall(request).execute();

    JSONObject jsonResponse = new JSONObject(response.body().string());
    return jsonResponse.getJSONObject("balances").getInt("USDC");
  }
}
