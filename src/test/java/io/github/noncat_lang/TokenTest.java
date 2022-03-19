package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class TokenTest {

  @Test
  void validate() throws Exception {
    // given
    Token token = Token.of("[a-z]+");
    // when
    boolean valid = token.validate("hello");
    // then
    assertThat(valid).isTrue();
  }

  @Test
  void encode() throws Exception {
    // given
    Token token = Token.of("[a-z]+").withEncoding(Encoding.of("E", "e"));
    // when
    String encoded = token.encode("hEllo");
    // then
    assertThat(encoded).isEqualTo("hello");
  }

  @Test
  void decode() throws Exception {
    // given
    Token token = Token.of("[a-z]+").withDecoding(Decoding.of("e", "E"));
    // when
    String decoded = token.decode("hello");
    // then
    assertThat(decoded).isEqualTo("hEllo");
  }

  @Test
  void validationErrorAfterEncoding() throws Exception {
    ThrowingCallable call = () -> {
      Token token = Token.of("[a-z]+").withEncoding(Encoding.of("o", "0"));
      token.encode("hello");
    };
    assertThatThrownBy(call).isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void validationErrorBeforeDecoding() throws Exception {
    ThrowingCallable call = () -> {
      Token token = Token.of("[a-z]+").withDecoding(Decoding.of("any", "any"));
      token.decode("hell0");
    };
    assertThatThrownBy(call).isExactlyInstanceOf(IllegalArgumentException.class);
  }

}
