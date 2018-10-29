package io.github.shemnon.drflags;

import com.google.common.collect.ImmutableList;

public class Flags {

  public static ImmutableList<String> parseFlags(String... args) {
    throw new RuntimeException("Not Implemented Yet");
  }

  public static ImmutableList<String> parseFlagsStrictly(String... args)
      throws UnknownFlagException {
    throw new UnknownFlagException("", "Not Implemented Yet");
  }

  public static Flag<Boolean> create(boolean value) {
    return new BooleanFlag(value);
  }

  public static Flag<String> create(String value) {
    return new StringFlag(value);
  }
}
