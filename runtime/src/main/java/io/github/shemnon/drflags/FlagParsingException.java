package io.github.shemnon.drflags;

public class FlagParsingException extends RuntimeException {
  FlagParsingException(String s) {
    super(s);
  }
}
