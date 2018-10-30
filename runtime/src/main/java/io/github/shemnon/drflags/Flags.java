package io.github.shemnon.drflags;

import com.google.common.collect.ImmutableList;
import io.github.shemnon.drflags.impl.AbseilStyleFlagsParser;

public class Flags {

  public static ImmutableList<String> parseFlags(String... args) {
    return AbseilStyleFlagsParser.parseFlags(args);
  }

  public static ImmutableList<String> parseFlagsStrictly(String... args)
      throws UnknownFlagException {
    return AbseilStyleFlagsParser.parseFlagsStrictly(args);
  }

  public static Flag<Boolean> create(boolean value) {
    return new BooleanFlag(value);
  }

  public static Flag<String> create(String value) {
    return new StringFlag(value);
  }
}
