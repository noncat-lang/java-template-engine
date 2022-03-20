package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DecodingTest {

  static Stream<Arguments> happyParams() {
    return Stream.of(
        Arguments.of("o", "0", "hello world", "hell0 w0rld"),
        Arguments.of("", "", "hello world", "hello world"),
        Arguments.of("[A-Z]", "($0)", "Hello World", "(H)ello (W)orld")
        );
  }

  @ParameterizedTest
  @MethodSource("happyParams")
  void happyPath(String regex, String replacement, String input, String expected) {
    // given
    Decoding decoding = Decoding.of(regex, replacement);
    // when
    String result = decoding.decode(input);
    // then
    assertThat(result).isEqualTo(expected);
  }

  static Stream<Arguments> unhappyParams() {
    return Stream.of(
        Arguments.of(null, "any", "any", NullPointerException.class),
        Arguments.of("any", null, "any", NullPointerException.class),
        Arguments.of("any", "any", null, NullPointerException.class));
  }

  @ParameterizedTest
  @MethodSource("unhappyParams")
  void unhappyPath(String regex, String replacement, String input, Class<Exception> expected) {
    ThrowingCallable call = () -> {
      Decoding decoding = Decoding.of(regex, replacement);
      decoding.decode(input);
    };
    assertThatThrownBy(call).isExactlyInstanceOf(expected);
  }

}
