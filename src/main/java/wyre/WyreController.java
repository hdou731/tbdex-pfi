package wyre;

import com.squareup.protos.tbd.pfi.WyreWallet;
import io.javalin.http.Handler;
import java.util.Objects;

public class WyreController {
  public static String BEARER = "Bearer SK-A8TG7YAU-NCMZXR2G-VCYGWHB8-J89NNDG2";

  public static Handler createWallet = ctx -> {
    String walletName = Objects.requireNonNull(ctx.pathParam("walletName"));
    WyreWallet wyreWallet = WyreClient.createWallet(walletName);
    ctx.result(wyreWallet.toString());
  };

  public static Handler createWalletOrderReservation = ctx -> {
    String account = Objects.requireNonNull(ctx.pathParam("account"));
    String walletOrderReservation = WyreClient.createWalletOrderReservation(account);
    ctx.result(walletOrderReservation);
  };

  public static Handler executeWalletOrderReservation = ctx -> {
    String reservationId = Objects.requireNonNull(ctx.pathParam("reservationId"));
    String walletOrderReservation = WyreClient.executeWalletOrderReservation(reservationId);
    ctx.result(walletOrderReservation);
  };

  public static Handler getWalletOrder = ctx -> {
    String orderId = Objects.requireNonNull(ctx.pathParam("orderId"));
    String walletOrder = WyreClient.getWalletOrder(orderId);
    ctx.result(walletOrder);
  };

  public static Handler payout = ctx -> {
    String amount = Objects.requireNonNull(ctx.pathParam("amount"));
    String transferId = WyreClient.payout(amount);
    ctx.result(transferId);
  };

  public static Handler getTransferStatus = ctx -> {
    String transferId = Objects.requireNonNull(ctx.pathParam("transferId"));
    String status = WyreClient.getTransferStatus(transferId);
    ctx.result(String.format("Transfer Status: %s", status));
  };

  public static Handler getWalletBalance = ctx -> {
    String walletToken = Objects.requireNonNull(ctx.pathParam("walletToken"));
    Integer balance = WyreClient.getWalletBalance(walletToken);
    ctx.result(String.format("USDC Balance: %s", balance.toString()));
  };
}
