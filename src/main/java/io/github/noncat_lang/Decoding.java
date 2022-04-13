package io.github.noncat_lang;

import io.github.noncat_lang.internal.DecodingImpl;

public interface Decoding {

  static Decoding of(String regex, String replacement) {
    return DecodingImpl.of(regex, replacement);
  }

  String decode(String value);

}
