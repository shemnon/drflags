* DrFlags

A from-scratch open source implementation of GFlags for Java, mostly inspired by 
[Abseil Python flags](https://github.com/abseil/abseil-py/) and C++ 
[GFlags](https://github.com/gflags/gflags).

** Philosophy

Command lines flags should be configured as close to their use in the code as
possible.  That often means in the same class as their use or in the module
the values are injected from.

Developers should be subjected to a minimum of ceremony relating to the use of
the flags.  Ideally it should consist of an annotation on the flags and an 
annotation parser will collect the flags in a way retrievable from a single
static method call in the main method.

Flags are first class objects.  We won't be using any fancy reflection to
dynamically inject the values into the flags, but the flag object will have the
needed logic to parse a string and set it itself.

Annotations are for user facing non-code configuration, such as flag names and 
help text.  The "mechanical" aspects of the flag belong in the flag class 
itself, such as whether the flag was set, if it is a repeated flag, and other 
details.

** Aspirational Goal

The goal is to create a library where users can declare their flags in any Java
file with annotations and the Annotation Processing Tool detects these and 
gathers the available flags together so that a standard code statement can
parse a command line flag and automatically provide the values of the flags.

*** Roadmap

This is how I see it evolving into my vision.  Some steps may be skipped.

1. User has to specify what the flags are and provide them to the parser.
2. User has to specify what the classes are and provide them to the parser.
   Flag data is taken via reflection.
3. An annotation processor generates a peer class that creates the metadata.
   User still specifies the classes containing flags.
4. The generated metadata classes are enumerated in META-INF and read by the
   Flags processor.  User does not need to enumerate classes.
5. The generated metadata is aggregated in module-info.class files somehow
   and JPMS is used to gather flags.
6. Use fancy Java 11 fields like nested classes to get access to non-public 
   fields without unsafe reflection access.
   
** Usage

Flags are higher level classes, allowing for more introspection about whether 
the flag was set and if you have a default value.  This also allows the flag
parsing system to update the fields without needing reflection. 
   
Flag fields are annotated with the @Flag annotation.  This is where the enduser
documentation provided by command line help and the flag names are set.
