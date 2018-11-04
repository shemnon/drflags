package com.example.drflags;

import java.util.List;

import com.example.drflags.demo.lib.AnotherLibraryFunction;
import com.example.drflags.demo.lib.LibraryFunction;
import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.Flags;

public class Demo {

  @FlagDesc(description = "This is a simple String flag.", category = "Types")
  static Flag<String> stringFlag = Flags.create("default");

  @FlagDesc(description = "This is a simple Boolean flag.", category = "Types")
  static Flag<Boolean> booleanFlag = Flags.create(true);

  @FlagDesc static Flag<String> simple = Flags.create("");

  @FlagDesc(name = "pretty")
  static Flag<Boolean> ugly = Flags.create(true);

  @FlagDesc(alternateNames = {"mountain-lion", "panther"})
  static Flag<String> cougar = Flags.create("Go Cougs!");

  public static void main(String[] args) {
    // The magic happens here
    List<String> unparsedArgs = Flags.parseFlags(args);

    // The magic has now happened.  Flags are parsed.
    System.out.println(String.join(" ", unparsedArgs));
    System.out.println("stringFlag - " + stringFlag.get());
    System.out.println("booleanFlag - " + booleanFlag.get());
    System.out.println("simple - " + simple.get());
    System.out.println("ugly - " + ugly.get());
    System.out.println("cougar - " + cougar.get());

    System.out.println(LibraryFunction.staticToString());
    System.out.println(AnotherLibraryFunction.staticToString());

  }
}
