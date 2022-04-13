package io.github.noncat_lang;

import io.github.noncat_lang.internal.EncodingImpl;

public interface Encoding {

  static Encoding of(String regex, String replacement) {
    return EncodingImpl.of(regex, replacement);
  }

  String encode(String value);

}
