package io.github.noncat_lang.internal;

import io.github.noncat_lang.Decoding;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
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
