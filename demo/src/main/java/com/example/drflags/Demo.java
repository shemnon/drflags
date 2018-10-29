package com.example.drflags;

import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.Flags;

public class Demo {

  @FlagDesc(description = "This is a simple String flag.", category="Types")
  Flag<String> stringFlag = Flags.create("default");

  @FlagDesc(description = "This is a simple Boolean flag.", category="Types")
  Flag<Boolean> booleanFlag = Flags.create(true);

  @FlagDesc
  Flag<String> simple = Flags.create("");

  @FlagDesc(name = "pretty")
  Flag<Boolean> ugly = Flags.create(true);

  @FlagDesc(alternateNames = {"mountain-lion", "panther"})
  Flag<String> cougar = Flags.create("Go Cougs!");

}
