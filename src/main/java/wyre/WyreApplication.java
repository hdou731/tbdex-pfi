package wyre;

import io.javalin.Javalin;

// figure out the approach we want to take for config files
//  - json config file vs. a properties file
// figure out what database we want to use
//

public class WyreApplication {
  public static void main(String[] args) {
    Javalin app = Javalin.create().start(9004);

    app.get("/", ctx -> ctx.result("hello world"));

    app.get("/createWallet/{walletName}", WyreController.createWallet);

    app.get("/createReservation/{account}", WyreController.createWalletOrderReservation);

    app.get("/executeReservation/{reservationId}", WyreController.executeWalletOrderReservation);

    app.get("/getWalletOrder/{orderId}", WyreController.getWalletOrder);

    app.get("/payout/{amount}", WyreController.payout);

    app.get("/getTransferStatus/{transferId}", WyreController.getTransferStatus);

    app.get("/getWalletBalance/{walletToken}", WyreController.getWalletBalance);
  }
}
