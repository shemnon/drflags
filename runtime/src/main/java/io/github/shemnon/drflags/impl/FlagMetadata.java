package io.github.shemnon.drflags.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.auto.value.AutoValue;
import io.github.shemnon.drflags.Flag;

@AutoValue
public abstract class FlagMetadata implements Comparable<FlagMetadata> {
  public static <T> FlagMetadata create(
      String className, String flagName, String desc, Flag<?> flag) {
    return new AutoValue_FlagMetadata(FlagID.create(className, flagName), desc, flag);
  }

  public abstract FlagID flagID();

  @Nullable
  public abstract String desc();

  public abstract Flag<?> flag();

  public final int compareTo(@Nonnull FlagMetadata that) {
    return this.flagID().compareTo(that.flagID());
  }
}
