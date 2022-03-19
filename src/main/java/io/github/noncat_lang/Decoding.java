package io.github.noncat_lang;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Decoding {
  @NonNull
  String regex;
  @NonNull
  String replacement;

  String decode(@NonNull String value) {
    return value.replaceAll(regex, replacement);
  }

}
