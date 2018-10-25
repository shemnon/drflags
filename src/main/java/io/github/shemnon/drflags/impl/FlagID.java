package io.github.shemnon.drflags.impl;

import javax.annotation.Nonnull;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class FlagID implements Comparable<FlagID> {
  public static FlagID create(String className, String flagName) {
    return new AutoValue_FlagID(className, flagName);
  }

  public abstract String className();

  public abstract String flagName();

  public final String fqn() {
    return className() + '.' + flagName();
  }

  public int compareTo(@Nonnull FlagID that) {
    return fqn().compareTo(that.fqn());
  }
}
