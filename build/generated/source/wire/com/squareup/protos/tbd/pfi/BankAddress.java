// Code generated by Wire protocol buffer compiler, do not edit.
// Source: squareup.protos.tbd.pfi.BankAddress in pfi.proto
package com.squareup.protos.tbd.pfi;

import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.ReverseProtoWriter;
import com.squareup.wire.Syntax;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import okio.ByteString;

public final class BankAddress extends Message<BankAddress, BankAddress.Builder> {
  public static final ProtoAdapter<BankAddress> ADAPTER = new ProtoAdapter_BankAddress();

  private static final long serialVersionUID = 0L;

  public static final String DEFAULT_COUNTRY = "";

  @WireField(
      tag = 1,
      adapter = "com.squareup.wire.ProtoAdapter#STRING"
  )
  public final String country;

  public BankAddress(String country) {
    this(country, ByteString.EMPTY);
  }

  public BankAddress(String country, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.country = country;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.country = country;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof BankAddress)) return false;
    BankAddress o = (BankAddress) other;
    return unknownFields().equals(o.unknownFields())
        && Internal.equals(country, o.country);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + (country != null ? country.hashCode() : 0);
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    if (country != null) builder.append(", country=").append(Internal.sanitize(country));
    return builder.replace(0, 2, "BankAddress{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<BankAddress, Builder> {
    public String country;

    public Builder() {
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    @Override
    public BankAddress build() {
      return new BankAddress(country, super.buildUnknownFields());
    }
  }

  private static final class ProtoAdapter_BankAddress extends ProtoAdapter<BankAddress> {
    public ProtoAdapter_BankAddress() {
      super(FieldEncoding.LENGTH_DELIMITED, BankAddress.class, "type.googleapis.com/squareup.protos.tbd.pfi.BankAddress", Syntax.PROTO_2, null, "pfi.proto");
    }

    @Override
    public int encodedSize(BankAddress value) {
      int result = 0;
      result += ProtoAdapter.STRING.encodedSizeWithTag(1, value.country);
      result += value.unknownFields().size();
      return result;
    }

    @Override
    public void encode(ProtoWriter writer, BankAddress value) throws IOException {
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.country);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public void encode(ReverseProtoWriter writer, BankAddress value) throws IOException {
      writer.writeBytes(value.unknownFields());
      ProtoAdapter.STRING.encodeWithTag(writer, 1, value.country);
    }

    @Override
    public BankAddress decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.country(ProtoAdapter.STRING.decode(reader)); break;
          default: {
            reader.readUnknownField(tag);
          }
        }
      }
      builder.addUnknownFields(reader.endMessageAndGetUnknownFields(token));
      return builder.build();
    }

    @Override
    public BankAddress redact(BankAddress value) {
      Builder builder = value.newBuilder();
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
