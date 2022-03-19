package io.github.noncat_lang;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Token {
  @NonNull
  String regex;
  List<Encoding> encodings = new ArrayList<>();
  List<Decoding> decodings = new ArrayList<>();

  public Token withEncoding(@NonNull Encoding encoding) {
    encodings.add(encoding);
    return this;
  }

  public Token withDecoding(@NonNull Decoding decoding) {
    decodings.add(decoding);
    return this;
  }

  protected String getRegex() {
    return regex;
  }

  public boolean validate(@NonNull String value) {
    return value.matches(regex);
  }

  public String encode(@NonNull String value) {
    String result = value;
    for (Encoding encoding : encodings) {
      result = encoding.encode(result);
    }
    if (!validate(result)) {
      throw new IllegalArgumentException(format("Error during encoding: value '%s' does not match token '%s'", result, regex));
    }
    return result;
  }

  public String decode(@NonNull String value) {
    if (!validate(value)) {
      throw new IllegalArgumentException(format("Error during decoding: value '%s' does not match token '%s'", value, regex));
    }
    String result = value;
    for (Decoding decoding : decodings) {
      result = decoding.decode(result);
    }
    return result;
  }

}
