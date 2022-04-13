package io.github.noncat_lang.internal;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import io.github.noncat_lang.Decoding;
import io.github.noncat_lang.Encoding;
import io.github.noncat_lang.Token;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TokenImpl implements Token {
  @NonNull
  @Getter
  String regex;
  List<Encoding> encodings = new ArrayList<>();
  List<Decoding> decodings = new ArrayList<>();

  public static TokenImpl of(@NonNull String regex) {
    if (regex.isBlank()) {
      throw new IllegalArgumentException("Token regex should not be blank/empty");
    }
    return new TokenImpl(regex);
  }

  @Override
  public Token withEncoding(@NonNull Encoding... encoding) {
    Stream.of(encoding).forEach(encodings::add);
    return this;
  }

  @Override
  public Token withDecoding(@NonNull Decoding... decoding) {
    Stream.of(decoding).forEach(decodings::add);
    return this;
  }

  @Override
  public boolean validate(@NonNull String value) {
    return value.matches(regex);
  }

  @Override
  public String encode(@NonNull String value) {
    String result = value;
    for (Encoding encoding : encodings) {
      result = encoding.encode(result);
    }
    if (!validate(result)) {
      throw new IllegalArgumentException(
          format("Error after encoding: value '%s' does not match token '%s'", result, regex));
    }
    return result;
  }

  @Override
  public String decode(@NonNull String value) {
    if (!validate(value)) {
      throw new IllegalArgumentException(
          format("Error before decoding: value '%s' does not match token '%s'", value, regex));
    }
    String result = value;
    for (Decoding decoding : decodings) {
      result = decoding.decode(result);
    }
    return result;
  }

}
