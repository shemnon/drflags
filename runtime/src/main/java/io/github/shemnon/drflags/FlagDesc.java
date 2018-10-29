package io.github.shemnon.drflags;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.SOURCE)
@Target(value = ElementType.FIELD)
public @interface FlagDesc {

  /**
   * The name of the flag on the command line.  If not set it will be the snake-case version of the
   * field holding the flag.
   */
  String name() default "";

  /**
   * The help description of the flag.
   */
  String description() default "";

  /**
   * Alternate names for the flag.
   */
  String[] alternateNames() default {};

  /**
   * Category to group the flag under in help
   */
  String category() default "";

}
