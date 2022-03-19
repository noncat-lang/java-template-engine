package io.github.noncat_lang;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor(staticName = "of")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Encoding {
  @NonNull
  String regex;
  @NonNull
  String replacement;

  String encode(@NonNull String value) {
    return value.replaceAll(regex, replacement);
  }

}
