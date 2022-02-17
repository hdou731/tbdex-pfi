package pfi;

import com.squareup.protos.tbd.pfi.CredentialsAskRequest;
import com.squareup.protos.tbd.pfi.ReceiveAskRequest;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AskHelper {
  public static final HashMap<String, ReceiveAskRequest> cache = new HashMap<>();

  private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

  public static void handleAskRequest(ReceiveAskRequest request) {
    cache.put(request.idempotence_token, request);
    System.out.println(String.format("received handle ask request %s", request));
  }

  public static void handleCredentialAsk(CredentialsAskRequest request) {

  }
}
