package io.github.shemnon.drflags;

import javax.annotation.Nonnull;

public class StringFlag implements Flag<String> {
  private String value;

  StringFlag(String value) {
    this.value = value;
  }

  @Override
  public String parse(@Nonnull String s) {
    value = s;
    return value;
  }

  @Override
  public String get() {
    return value;
  }
}
