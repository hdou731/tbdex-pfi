package pfi;

import com.google.gson.Gson;
import com.squareup.protos.tbd.pfi.ConvertFundsRequest;
import com.squareup.protos.tbd.pfi.CredentialsAskRequest;
import com.squareup.protos.tbd.pfi.Message;
import com.squareup.protos.tbd.pfi.ReceiveAskRequest;
import com.squareup.protos.tbd.pfi.ReceiveCredentialsRequest;
import com.squareup.protos.tbd.pfi.Response;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RealPfiApi {
  private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

  public static Response handleMessage(Message message) {
    Gson gson = new Gson();
    switch (message.descriptor.schema) {
      case "receiveAsk":
        ReceiveAskRequest receiveAskRequest =
            gson.fromJson(message.data, ReceiveAskRequest.class);
        return receiveAsk(receiveAskRequest);
      case "credentialsAsk":
        CredentialsAskRequest credentialsAskRequest =
            gson.fromJson(message.data, CredentialsAskRequest.class);
        return credentialsAsk(credentialsAskRequest);
      case "receiveCredentials":
        ReceiveCredentialsRequest receiveCredentialsRequest =
            gson.fromJson(message.data, ReceiveCredentialsRequest.class);
        return receiveCredentials(receiveCredentialsRequest);
      case "convertFunds":
        ConvertFundsRequest convertFundsRequest =
            gson.fromJson(message.data, ConvertFundsRequest.class);
        return convertFunds(convertFundsRequest);
    }
    return null;
  }

  public static Response receiveAsk(ReceiveAskRequest request) {
    executorService.submit(() -> AskHelper.handleAskRequest(request));
    return new Response.Builder().build();
  }

  public static Response credentialsAsk(CredentialsAskRequest request) {
    executorService.submit(() -> AskHelper.handleCredentialAsk(request));
    return new Response.Builder().build();
  }

  public static Response receiveCredentials(ReceiveCredentialsRequest request) {
    executorService.submit(() -> CredentialHelper.handleReceiveCredentialRequest(request));
    return new Response.Builder().build();
  }

  public static Response convertFunds(ConvertFundsRequest request) {
    executorService.submit(() -> FundsHelper.convertFunds(request));
    return new Response.Builder().build();
  }
}
