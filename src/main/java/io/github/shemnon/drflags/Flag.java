package io.github.shemnon.drflags;

import java.util.function.Supplier;
import javax.annotation.Nonnull;

public interface Flag<T> extends Supplier<T> {

  T parse(@Nonnull String s);
}
