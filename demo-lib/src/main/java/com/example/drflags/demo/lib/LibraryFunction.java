package com.example.drflags.demo.lib;

import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.Flags;

public class LibraryFunction {

  @FlagDesc(description="Favorite Team")
  static Flag<String> team = Flags.create("Manchester");

  @FlagDesc(description="Favorite Sport")
  static Flag<String> sport = Flags.create("Football");

  @FlagDesc(description="An example of cross lib collision")
  static Flag<String> simple = Flags.create("complex");

  @FlagDesc(description="An example of local collision")
  static Flag<Boolean> local = Flags.create(false);

  public static String staticToString() {
    return "LibraryFunction{" +
        "\nteam=" + team.get() +
        ",\n sport=" + sport.get() +
        ",\n simple=" + simple.get() +
        ",\n local=" + local.get() +
        "\n}";
  }
}
