package io.github.noncat_lang.internal;

import io.github.noncat_lang.Encoding;
import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public final class EncodingImpl implements Encoding {
  @NonNull
  String regex;
  @NonNull
  String replacement;

  @Override
  public String encode(@NonNull String value) {
    return value.replaceAll(regex, replacement);
  }

}
