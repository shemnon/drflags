package io.github.shemnon.drflags;

public class UnknownFlagException extends Exception {

  private final String flagName;
  private final String flagValue;

  public UnknownFlagException(String flagName, String flagValue) {
    super("Unknown Flag: '" + flagName + "'");
    this.flagName = flagName;
    this.flagValue = flagValue;
  }

  public String getFlagName() {
    return flagName;
  }

  public String getFlagValue() {
    return flagValue;
  }
}
