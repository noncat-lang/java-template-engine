package io.github.noncat_lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class TokenTest {

  @Test
  void validate() {
    // given
    Token token = Token.of("[a-z]+");
    // when
    boolean valid = token.validate("hello");
    // then
    assertThat(valid).isTrue();
  }

  @Test
  void encode() {
    // given
    Token token = Token.of("[a-z]+").withEncoding(Encoding.of("E", "e"));
    // when
    String encoded = token.encode("hEllo");
    // then
    assertThat(encoded).isEqualTo("hello");
  }

  @Test
  void encodeWithMultipleEncodingsInOrder() {
    // given
    Token token = Token.of("[a-z]+").withEncoding(Encoding.of("E", "e")).withEncoding(Encoding.of("e", "ee"));
    // when
    String encoded = token.encode("hEllo");
    // then
    assertThat(encoded).isEqualTo("heello");
  }

  @Test
  void encodeWithMultipleEncodingsInOrderAlt() {
    // given
    Token token = Token.of("[a-z]+").withEncoding(Encoding.of("E", "e"), Encoding.of("e", "ee"));
    // when
    String encoded = token.encode("hEllo");
    // then
    assertThat(encoded).isEqualTo("heello");
  }

  @Test
  void decode() {
    // given
    Token token = Token.of("[a-z]+").withDecoding(Decoding.of("e", "E"));
    // when
    String decoded = token.decode("hello");
    // then
    assertThat(decoded).isEqualTo("hEllo");
  }

  @Test
  void decodeWithMultipleDecodingsInOrder() {
    // given
    Token token = Token.of("[a-z]+").withDecoding(Decoding.of("e", "E")).withDecoding(Decoding.of("E", "EE"));
    // when
    String decoded = token.decode("hello");
    // then
    assertThat(decoded).isEqualTo("hEEllo");
  }

  @Test
  void decodeWithMultipleDecodingsInOrderAlt() {
    // given
    Token token = Token.of("[a-z]+").withDecoding(Decoding.of("e", "E"), Decoding.of("E", "EE"));
    // when
    String decoded = token.decode("hello");
    // then
    assertThat(decoded).isEqualTo("hEEllo");
  }

  @Test
  void validationErrorAfterEncoding() {
    ThrowingCallable call = () -> {
      // given
      Token token = Token.of("[a-z]+").withEncoding(Encoding.of("o", "0"));
      // when
      token.encode("hello");
    };
    // then
    assertThatThrownBy(call).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Error after encoding: value 'hell0' does not match token '[a-z]+'");
  }

  @Test
  void validationErrorBeforeDecoding() {
    ThrowingCallable call = () -> {
      // given
      Token token = Token.of("[a-z]+").withDecoding(Decoding.of("0", "o"));
      // when
      token.decode("hell0");
    };
    // then
    assertThatThrownBy(call).isExactlyInstanceOf(IllegalArgumentException.class)
        .hasMessage("Error before decoding: value 'hell0' does not match token '[a-z]+'");
  }

  @Test
  void regexNull() {
    String regex = null;
    assertThatThrownBy(() -> Token.of(regex)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void regexEmpty() {
    String regex = "";
    assertThatThrownBy(() -> Token.of(regex)).isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void regexBlank() {
    String regex = " ";
    assertThatThrownBy(() -> Token.of(regex)).isExactlyInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void encodingNull() {
    Encoding[] encoding = null;
    assertThatThrownBy(() -> Token.of("any").withEncoding(encoding)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void decodingNull() {
    Decoding[] decoding = null;
    assertThatThrownBy(() -> Token.of("any").withDecoding(decoding)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void encodeNull() {
    String value = null;
    assertThatThrownBy(() -> Token.of("any").encode(value)).isExactlyInstanceOf(NullPointerException.class);
  }

  @Test
  void decodeNull() {
    String value = null;
    assertThatThrownBy(() -> Token.of("any").decode(value)).isExactlyInstanceOf(NullPointerException.class);
  }
}
