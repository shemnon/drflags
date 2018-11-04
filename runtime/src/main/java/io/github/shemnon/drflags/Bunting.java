package io.github.shemnon.drflags;

import io.github.shemnon.drflags.impl.FlagDescriptor;

public interface Bunting {

  Iterable<FlagDescriptor> getFlagDescriptors();
}
