package io.github.noncat_lang;

import io.github.noncat_lang.internal.TokenImpl;

public interface Token {

  static Token of(String regex) {
    return TokenImpl.of(regex);
  }

  Token withEncoding(Encoding... encoding);

  Token withDecoding(Decoding... decoding);

  String getRegex();

  boolean validate(String value);

  String encode(String value);

  String decode(String value);

}
