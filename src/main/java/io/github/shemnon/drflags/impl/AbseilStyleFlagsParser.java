package io.github.shemnon.drflags.impl;

import static com.google.common.base.Preconditions.checkState;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import io.github.shemnon.drflags.BooleanFlag;
import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.UnknownFlagException;

/**
 * A flag parser inspired from the Abseil Python GFlags framework.
 * https://github.com/abseil/abseil-py/blob/master/absl/flags/tests/_flagvalues_test.py
 */
public class AbseilStyleFlagsParser {

  private static final String UNDEFINED_OK_FLAG_NAME = "undefok";

  public static ImmutableList<String> parseFlags(Map<String, Flag> flags, String... args) {
    AbseilStyleFlagsParser parser = new AbseilStyleFlagsParser(flags);
    parser.parseImpl(args);
    return parser.unparsedArgs;
  }

  public static ImmutableList<String> parseFlagsStrictly(Map<String, Flag> flags, String... args)
      throws UnknownFlagException {
    AbseilStyleFlagsParser parser = new AbseilStyleFlagsParser(flags, true, false);
    parser.parseImpl(args);
    if (!parser.unknownFlags.isEmpty()) {
      SimpleEntry<String, String> firstBadFlag = parser.unknownFlags.get(0);
      throw new UnknownFlagException(firstBadFlag.getKey(), firstBadFlag.getValue());
    }
    return parser.unparsedArgs;
  }

  private Map<String, Flag> flags;

  /** GNU style arg parsing; only stop at -- not first unknown arg */
  private final boolean gnuStyle;

  /**
   * Parse and remove known flags; keep the rest untouched. Unknown flags specified by --undefok are
   * not kept.
   */
  private final boolean knownOnly;

  private ImmutableList<SimpleEntry<String, String>> unknownFlags;
  private ImmutableList<String> unparsedArgs;

  private AbseilStyleFlagsParser(Map<String, Flag> flags) {
    this(flags, true, true);
  }

  private AbseilStyleFlagsParser(Map<String, Flag> flags, boolean gnuStyle, boolean knownOnly) {
    this.flags = flags;
    this.gnuStyle = gnuStyle;
    this.knownOnly = knownOnly;
  }

  public void addKnownFlag(String flagName, Flag<?> flag) {
    flags.put(flagName, flag);
  }

  private void parseImpl(String... args) {
    Iterator<String> argsIter = Arrays.asList(args).iterator();

    Set<String> undefokNames = new HashSet<>();

    List<SimpleEntry<String, String>> unparsedNamesAndArgs = new LinkedList<>();

    while (argsIter.hasNext()) {
      String thisArg = argsIter.next();

      if (!thisArg.startsWith("-")) {
        unparsedNamesAndArgs.add(new SimpleEntry<>(null, thisArg));
        if (gnuStyle) {
          continue;
        } else {
          break;
        }
      }

      if ("--".equals(thisArg)) {
        if (knownOnly) {
          unparsedNamesAndArgs.add(new SimpleEntry<>(null, thisArg));
        }
        break;
      }

      String argWithoutDashes =
          thisArg.startsWith("--") ? thisArg.substring(2) : thisArg.substring(1);

      String name;
      String value;
      if (argWithoutDashes.contains("=")) {
        String[] argParts = argWithoutDashes.split("=", 2);
        name = argParts[0];
        value = argParts[1];
      } else {
        name = argWithoutDashes;
        value = null;
      }

      Supplier<String> argValue =
          () -> {
            if (value == null) {
              if (argsIter.hasNext()) {
                return argsIter.next();
              } else {
                throw new RuntimeException("Missing value for flag " + name);
              }
            } else {
              return value;
            }
          };

      if ("".equals(name)) {
        // argument name is all dashes
        unparsedNamesAndArgs.add(new SimpleEntry<>(null, thisArg));
        if (gnuStyle) {
          continue;
        } else {
          break;
        }
      }

      if (UNDEFINED_OK_FLAG_NAME.equals(name)) {
        List<String> undefFlags = Arrays.asList(argValue.get().split(","));
        undefokNames.addAll(undefFlags);
        undefokNames.addAll(undefFlags.stream().map(s -> "no" + s).collect(Collectors.toList()));
        continue;
      }

      Flag flag = flags.get(name);

      // Boolean special cases.  --flag is parsed as --flag=true and --noflag as --flag=fakse
      if (flag instanceof BooleanFlag) {
        if (value == null) {
          flag.parse("true");
        } else {
          flag.parse(value);
        }
        continue;
      } else if (flag == null && name.startsWith("no")) {
        Flag noFlag = flags.get(name.substring(2));
        if (noFlag instanceof BooleanFlag) {
          checkState(value == null, name + "does not take an argument");
          noFlag.parse("false");
          continue;
        }
      }

      if (flag == null) {
        unparsedNamesAndArgs.add(new SimpleEntry<>(name, thisArg));
      } else {
        flag.parse(argValue.get());
      }
    }

    ImmutableList.Builder<SimpleEntry<String, String>> unknownFlagsBuilder =
        ImmutableList.builder();
    ImmutableList.Builder<String> unparsedArgsBuilder = ImmutableList.builder();

    // Calculate the unparsed positional parameters and the unknown flags.
    // This has to be done at the end of the loop so we can calculate all the undefok flags.
    for (SimpleEntry<String, String> entry : unparsedNamesAndArgs) {
      if (entry.getKey() == null) {
        unparsedArgsBuilder.add(entry.getValue());
      } else if (undefokNames.contains(entry.getKey())) {
        // Undefined OK flag, drop it and it's arg.
        continue;
      } else {
        // undefine flags and it's not OK.
        if (knownOnly) {
          unparsedArgsBuilder.add(entry.getValue());
        } else {
          unknownFlagsBuilder.add(entry);
        }
      }
    }

    // Pack the rest into the unparsedNames.  This can happen by a naked '--' or via non-gnu
    // position args.
    while (argsIter.hasNext()) {
      unparsedArgsBuilder.add(argsIter.next());
    }
    unparsedArgs = unparsedArgsBuilder.build();
    unknownFlags = unknownFlagsBuilder.build();
  }
}
