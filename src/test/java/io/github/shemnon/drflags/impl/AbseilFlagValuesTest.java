package io.github.shemnon.drflags.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.Flags;
import org.junit.Test;

public class AbseilFlagValuesTest {

  @Test
  public void booleanFlags() {
    Flag<Boolean> boolFlag = Flags.create(true);
    Map<String, Flag> testFlags = ImmutableMap.of("nothing", boolFlag);

    assertThat(boolFlag.get()).isTrue();
    boolFlag.parse("false");
    assertThat(boolFlag.get()).isFalse();

    assertThat(AbseilStyleFlagsParser.parseFlags(testFlags, "--nothing")).isEmpty();
    assertThat(boolFlag.get()).isTrue();
    assertThat(AbseilStyleFlagsParser.parseFlags(testFlags, "--nonothing")).isEmpty();
    assertThat(boolFlag.get()).isFalse();

    assertThat(AbseilStyleFlagsParser.parseFlags(testFlags, "--nothing=True")).isEmpty();
    assertThat(boolFlag.get()).isTrue();
    assertThat(AbseilStyleFlagsParser.parseFlags(testFlags, "--nothing=False")).isEmpty();
    assertThat(boolFlag.get()).isFalse();

    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> AbseilStyleFlagsParser.parseFlags(testFlags, "--nonothing=true"));
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> AbseilStyleFlagsParser.parseFlags(testFlags, "--nonothing=false"));
  }

  private static void knownFlagsTest(String[] args, List<String> definedFlags, String[] expected) {
    Map<String, Flag> flags =
        definedFlags
            .stream()
            .collect(
                Collectors.toMap(
                    s -> s, s -> s.startsWith("b") ? Flags.create(false) : Flags.create("")));
    assertThat(AbseilStyleFlagsParser.parseFlags(flags, args)).containsExactly(expected);
  }

  @Test
  public void knownOnlyFlagsInGnuStyle() {
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        Collections.emptyList(),
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "));
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("f1"),
        "0 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "));
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("f2"),
        "0 --f1=v1 cmd --b1 --f3 v3 --nob2".split(" "));
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("f3"),
        "0 --f1=v1 cmd --f2 v2 --b1 --nob2".split(" "));
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("b1"),
        "0 --f1=v1 cmd --f2 v2 --f3 v3 --nob2".split(" "));
    knownFlagsTest(
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("b2"),
        "0 --f1=v1 cmd --f2 v2 --b1 --f3 v3".split(" "));

    knownFlagsTest(
        "0 --f1=v1 cmd --undefok=f1 --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("b2"),
        "0 cmd --f2 v2 --b1 --f3 v3".split(" "));

    knownFlagsTest(
        "0 --f1=v1 cmd --undefok f1,f2 --f2 v2 --b1 --f3 v3 --nob2".split(" "),
        ImmutableList.of("b2"),
        // Note v2 is preserved here, since undefok requires the flag being
        // specified in the form of --flag=value.
        "0 cmd v2 --b1 --f3 v3".split(" "));
  }
}
