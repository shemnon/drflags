package com.example.drflags.demo.lib;

import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.Flags;

public class AnotherLibraryFunction {

  @FlagDesc(description="Favorite Colour")
  static Flag<String> colour = Flags.create("Blue");

  @FlagDesc(description="Capital of Assyria")
  static Flag<String> capital = Flags.create("Nineveh");

  @FlagDesc(description="An example of collision")
  static Flag<String> simple = Flags.create("complex");

  @FlagDesc(description="An example of local collision")
  static Flag<Boolean> local = Flags.create(false);

  public static String staticToString() {
    return "AnotherLibraryFunction{\n" +
        "colour=" + colour.get() +
        ",\n capital=" + capital.get() +
        ",\n simple=" + simple.get() +
        ",\n local=" + local.get() +
        "\n}";
  }
}
