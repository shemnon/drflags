package io.github.shemnon.drflags.impl;

import java.util.List;
import javax.annotation.Nonnull;

import com.google.auto.value.AutoValue;
import io.github.shemnon.drflags.Flag;

@AutoValue
public abstract class FlagDescriptor implements Comparable<FlagDescriptor> {

    public static FlagDescriptor create(
        String className,
        String fieldName,
        String flagName,
        String annotationName,
        String annotationDescription,
        List<String> annotationAlternateNames,
        String category,
        Flag<?> flag) {
      return new AutoValue_FlagDescriptor(
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

  public abstract String getClassName();

  public abstract String getFieldName();

  public abstract String getFlagName();

  public abstract String getAnnotationName();

  public abstract String getAnnotationDescription();

  public abstract List<String> getAnnotationAlternateNames();

  public abstract String getCategory();

  public abstract Flag<?> getFlag();

  public String getFqn() {
    return getClassName() + '.' + getFlagName();
  }

}
