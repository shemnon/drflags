package io.github.shemnon.drflags;

public class AmbiguousFlagException extends FlagParsingException {

  private final Iterable<String> flagNames;
  private final String flagName;

  public AmbiguousFlagException(Iterable<String> flagNames, String flagName) {
    super("Ambiguous Flag: '" + flagName + "' could be any of " + String.join(", ", flagNames));
    this.flagNames = flagNames;
    this.flagName = flagName;
  }

  public Iterable<String> getFlagNames() {
    return flagNames;
  }

  public String getFlagName() {
    return flagName;
  }
}
