package io.github.noncat_lang.internal;

import io.github.noncat_lang.Decoding;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public final class DecodingImpl implements Decoding {
  @NonNull
  String regex;
  @NonNull
  String replacement;

  @Override
  public String decode(@NonNull String value) {
    return value.replaceAll(regex, replacement);
  }

}
