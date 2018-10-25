package io.github.shemnon.drflags;

import javax.annotation.Nonnull;

public class BooleanFlag implements Flag<Boolean> {
  private boolean value;

  public BooleanFlag(boolean value) {
    this.value = value;
  }

  @Override
  public Boolean parse(@Nonnull String s) {
    value = Boolean.parseBoolean(s);
    return value;
  }

  @Override
  public Boolean get() {
    return value;
  }
}
