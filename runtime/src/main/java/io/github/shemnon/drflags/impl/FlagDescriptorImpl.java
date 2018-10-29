package io.github.shemnon.drflags.impl;

import java.util.List;
import javax.annotation.Nonnull;

import com.google.auto.value.AutoValue;
import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDescriptor;

@AutoValue
public abstract class FlagDescriptorImpl implements FlagDescriptor, Comparable<FlagDescriptor> {

  public static FlagDescriptor create(
      String className,
      String fieldName,
      String flagName,
      String annotationName,
      String annotationDescription,
      List<String> annotationAlternateNames,
      String category,
      Flag<?> flag) {
    return new AutoValue_FlagDescriptorImpl(
        className,
        fieldName,
        flagName,
        annotationName,
        annotationDescription,
        annotationAlternateNames,
        category,
        flag);
  }

  public int compareTo(@Nonnull FlagDescriptor that) {
    return getFqn().compareTo(that.getFqn());
  }
}
