import com.google.gson.Gson;
import com.squareup.protos.tbd.pfi.BankAddress;
import com.squareup.protos.tbd.pfi.BillingDetails;
import com.squareup.protos.tbd.pfi.ConvertFundsRequest;
import com.squareup.protos.tbd.pfi.CurrencyCode;
import com.squareup.protos.tbd.pfi.Descriptor;
import com.squareup.protos.tbd.pfi.Message;
import com.squareup.protos.tbd.pfi.ReceiveAskRequest;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import java.util.UUID;
import pfi.RealPfiApi;

public class TbdexApplication {
  public static void main(String[] args) throws InterruptedException {
    Javalin app = Javalin.create().start(9004);
    app.post("/handleMessage", handleMessage);

    Gson gson = new Gson();
    String askToken = UUID.randomUUID().toString();
    String dataString = gson.toJson(new ReceiveAskRequest.Builder()
        .source_currency_code(CurrencyCode.USDC)
        .destination_currency_code(CurrencyCode.USD)
        .amount_cents(10)
        .idempotence_token(askToken)
        .build());

    Message message = new Message.Builder()
        .data(dataString)
        .descriptor(new Descriptor.Builder().schema("receiveAsk").build())
        .build();

    RealPfiApi.handleMessage(message);

    Thread.sleep(1000);

    String convertFundsRequest = gson.toJson(new ConvertFundsRequest.Builder()
        .account_number("12340010")
        .routing_number("121000248")
        .idempotency_key(UUID.randomUUID().toString())
        .billing_details(new BillingDetails.Builder()
            .name("Satoshi Nakamoto")
            .city("Boston")
            .country("US")
            .line1("100 Money Street")
            .postalCode("01234")
            .district("MA")
            .build())
        .bank_address(new BankAddress.Builder().country("US").build())
        .ask_idempotence_token(askToken)
        .wallet_address("0xb3fa65823DCf60f68E21b9c365e52902f4685200")
        .build());

    Message message2 = new Message.Builder()
        .data(convertFundsRequest)
        .descriptor(new Descriptor.Builder().schema("convertFunds").build())
        .build();

    RealPfiApi.handleMessage(message2);
  }

  public static Handler handleMessage = ctx ->
      RealPfiApi.handleMessage(Message.ADAPTER.decode(ctx.body().getBytes()));
}
