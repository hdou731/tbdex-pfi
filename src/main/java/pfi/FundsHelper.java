package pfi;

import circle.CircleClient;
import com.squareup.protos.tbd.pfi.ConvertFundsRequest;
import com.squareup.protos.tbd.pfi.CreateBankAccountRequest;
import com.squareup.protos.tbd.pfi.CreateWirePaymentRequest;
import com.squareup.protos.tbd.pfi.ReceiveAskRequest;

public class FundsHelper {
  public static void convertFunds(ConvertFundsRequest request) {
    ReceiveAskRequest receiveAskRequest = AskHelper.cache.get(request.ask_idempotence_token);

    CreateBankAccountRequest createBankAccountRequest = new CreateBankAccountRequest.Builder()
        .accountNumber(request.account_number)
        .routingNumber(request.routing_number)
        .idempotencyKey(request.idempotency_key)
        .billingDetails(request.billing_details)
        .bankAddress(request.bank_address)
        .build();

    String trackingRef;
    try {
      trackingRef = CircleClient.createBankAccount(createBankAccountRequest);
    } catch (Exception e) {
      trackingRef = "";
    }

    System.out.println(trackingRef);

    CreateWirePaymentRequest createWirePaymentRequest = new CreateWirePaymentRequest.Builder()
        .amount(receiveAskRequest.amount_cents.toString())
        .currency(receiveAskRequest.destination_currency_code)
        .tracking_ref(trackingRef)
        .build();

    try {
      CircleClient.createWirePayment(createWirePaymentRequest);
    } catch (Exception e) {

    }
  }
}
