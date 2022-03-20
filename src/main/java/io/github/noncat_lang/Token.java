package io.github.noncat_lang;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Token {
  @NonNull
  @Getter(AccessLevel.PACKAGE)
  String regex;
  List<Encoding> encodings = new ArrayList<>();
  List<Decoding> decodings = new ArrayList<>();

  public static Token of(@NonNull String regex) {
    if (regex.isBlank()) {
      throw new IllegalArgumentException("Token regex should not be blank/empty");
    }
    return new Token(regex);
  }

  public Token withEncoding(@NonNull Encoding... encoding) {
    Stream.of(encoding).forEach(encodings::add);
    return this;
  }

  public Token withDecoding(@NonNull Decoding... decoding) {
    Stream.of(decoding).forEach(decodings::add);
    return this;
  }

  boolean validate(@NonNull String value) {
    return value.matches(regex);
  }

  String encode(@NonNull String value) {
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

  String decode(@NonNull String value) {
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
