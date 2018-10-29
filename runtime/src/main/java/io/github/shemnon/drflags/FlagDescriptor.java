package io.github.shemnon.drflags;

import java.util.List;
import javax.annotation.Nonnull;

import com.google.auto.value.AutoValue;

public interface FlagDescriptor {

  String getClassName();

  String getFieldName();

  String getFlagName();

  String getAnnotationName();

  String getAnnotationDescription();

  List<String> getAnnotationAlternateNames();

  String getCategory();

  Flag<?> getFlag();

  default String getFqn() {
    return getClassName() + '.' + getFlagName();
  }

}
