package com.example.drflags;

import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.Flags;

public class Demo {

  @FlagDesc(description = "This is a simple String flag.", category="Types")
  static Flag<String> stringFlag = Flags.create("default");

  @FlagDesc(description = "This is a simple Boolean flag.", category="Types")
  static Flag<Boolean> booleanFlag = Flags.create(true);

  @FlagDesc
  static Flag<String> simple = Flags.create("");

  @FlagDesc(name = "pretty")
  static Flag<Boolean> ugly = Flags.create(true);

  @FlagDesc(alternateNames = {"mountain-lion", "panther"})
  static Flag<String> cougar = Flags.create("Go Cougs!");

  public static void main(String[] args) {
    Flags.parseFlags(args);
  }
}
