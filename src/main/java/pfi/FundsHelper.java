package pfi;

import circle.CircleClient;
import com.squareup.protos.tbd.pfi.Amount;
import com.squareup.protos.tbd.pfi.BankAccount;
import com.squareup.protos.tbd.pfi.ConvertFundsRequest;
import com.squareup.protos.tbd.pfi.CreateBankAccountRequest;
import com.squareup.protos.tbd.pfi.CreateWirePaymentRequest;
import com.squareup.protos.tbd.pfi.CurrencyCode;
import com.squareup.protos.tbd.pfi.Destination;
import com.squareup.protos.tbd.pfi.Metadata;
import com.squareup.protos.tbd.pfi.PayoutRequest;
import com.squareup.protos.tbd.pfi.ReceiveAskRequest;
import com.squareup.protos.tbd.pfi.Source;
import com.squareup.protos.tbd.pfi.TransferRequest;
import java.util.UUID;

public class FundsHelper {
  public static void convertFunds(ConvertFundsRequest request) {
    ReceiveAskRequest receiveAskRequest = AskHelper.cache.get(request.ask_idempotence_token);

    BankAccount bankAccount = createBankAccount(request);
    System.out.println(bankAccount);

    Amount amount = new Amount.Builder()
        .amount(receiveAskRequest.amount_cents.toString())
        .currency(CurrencyCode.USD)
        .build();

    if (receiveAskRequest.destination_currency_code == CurrencyCode.USDC) {
      CreateWirePaymentRequest createWirePaymentRequest = new CreateWirePaymentRequest.Builder()
          .amount(amount)
          .trackingRef(bankAccount.trackingRef)
          .build();

      TransferRequest transferRequest = new TransferRequest.Builder()
          .source(new Source.Builder().id("1000594591").type("wallet").build())
          .destination(new Destination.Builder()
              .type("blockchain")
              .address(request.wallet_address)
              .chain("ETH")
              .build())
          .idempotencyKey(UUID.randomUUID().toString())
          .amount(amount)
          .build();
      try {
        CircleClient.createWirePayment(createWirePaymentRequest);
        CircleClient.transfer(transferRequest);
      } catch (Exception e) {
        System.out.println("wire payment failed");
      }
    } else if(receiveAskRequest.destination_currency_code == CurrencyCode.USD){
      try {
        PayoutRequest payoutRequest = new PayoutRequest.Builder()
            .source(new Source.Builder().id("1000594591").type("wallet").build())
            .destination(new Destination.Builder()
                .type("wire")
                .id(bankAccount.id)
                .build())
            .idempotencyKey(UUID.randomUUID().toString())
            .metadata(new Metadata.Builder().beneficiaryEmail("email@email.com").build())
            .amount(amount)
            .build();
        CircleClient.payout(payoutRequest);
      } catch (Exception e) {
        System.out.println("payout failed");
      }
    }
  }

  private static BankAccount createBankAccount(ConvertFundsRequest request) {
    CreateBankAccountRequest createBankAccountRequest = new CreateBankAccountRequest.Builder()
        .accountNumber(request.account_number)
        .routingNumber(request.routing_number)
        .idempotencyKey(request.idempotency_key)
        .billingDetails(request.billing_details)
        .bankAddress(request.bank_address)
        .build();

    BankAccount bankAccount;
    try {
      bankAccount = CircleClient.createBankAccount(createBankAccountRequest);
    } catch (Exception e) {
      bankAccount = null;
    }
    return bankAccount;
  }
}
