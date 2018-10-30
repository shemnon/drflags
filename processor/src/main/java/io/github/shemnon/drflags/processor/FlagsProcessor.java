package io.github.shemnon.drflags.processor;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.WARNING;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import com.google.auto.service.AutoService;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import io.github.shemnon.drflags.Bunting;
import io.github.shemnon.drflags.Flag;
import io.github.shemnon.drflags.FlagDesc;
import io.github.shemnon.drflags.FlagDescriptor;
import io.github.shemnon.drflags.impl.FlagDescriptorImpl;

@SupportedAnnotationTypes("io.github.shemnon.drflags.FlagDesc")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class FlagsProcessor extends AbstractProcessor {


  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (!roundEnv.processingOver()) {
      Multimap<Element, VariableElement> flagFields = ArrayListMultimap.create();
      for (TypeElement annotation : annotations) {
        Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);

        annotatedElements
            .stream()
            .map(e -> (VariableElement) e)
            .filter(this::validateElement)
            .forEach(e -> flagFields.put(e.getEnclosingElement(), e));
      }
      generateBunting(flagFields);
    }
    return true;
  }

  private void generateBunting(Multimap<Element, VariableElement> flagFields) {

    ClassName flagDescriptorClass = ClassName.get(FlagDescriptor.class);
    ClassName iterableClass = ClassName.get(Iterable.class);
    ClassName listClass = ClassName.get(List.class);
    TypeName iterableOfFlagDescriptor =
        ParameterizedTypeName.get(iterableClass, flagDescriptorClass);

    for (Element parentElement : flagFields.keySet()) {
      MethodSpec.Builder getFlagDescriptorsBuilder =
          MethodSpec.methodBuilder("getFlagDescriptors")
              .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
              .returns(iterableOfFlagDescriptor)
              .addStatement("$T flagDescriptors = new $T<>()", listClass, ArrayList.class);
      for (VariableElement varElement : flagFields.get(parentElement)) {
        FlagDesc annotation = varElement.getAnnotation(FlagDesc.class);

        String flagName = annotation.name();
        if (flagName.isEmpty()) {
          flagName = varElement.getSimpleName().toString();
        }
        getFlagDescriptorsBuilder.addStatement(
            "flagDescriptors.add(\n$T.create(\n$S,\n$S,\n$S,\n$S,\n$S,\n$T.of($L),\n$S,\n$L))",
            FlagDescriptorImpl.class,
            varElement.getEnclosingElement().toString(),
            varElement.getSimpleName(),
            flagName,
            annotation.name(),
            annotation.description(),
            ImmutableList.class,
            annotation.alternateNames().length > 0
              ? "\n    \"" + String.join("\",\n    \"", annotation.alternateNames()) + "\""
              : "",
            annotation.category(),
            varElement.getEnclosingElement().getSimpleName() + "." + varElement.getSimpleName());
      }
      getFlagDescriptorsBuilder.addStatement("return flagDescriptors");

      try {
        JavaFile.builder(
                processingEnv.getElementUtils().getPackageOf(parentElement).asType().toString(),
                TypeSpec.classBuilder(parentElement.getSimpleName() + "$DrFlags")
                    .addAnnotation(
                        AnnotationSpec.builder(AutoService.class)
                            .addMember("value", "$T.class", Bunting.class)
                            .build())
                    .addSuperinterface(ClassName.get(Bunting.class))
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(getFlagDescriptorsBuilder.build())
                    .build())
            .build()
            .writeTo(processingEnv.getFiler());
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(ERROR, e.toString(), parentElement);
      }
    }
    processingEnv.getMessager().printMessage(WARNING, "Generating Bunting");
  }

  private boolean validateElement(VariableElement e) {
    if (!e.getModifiers().contains(Modifier.STATIC)) {
      processingEnv.getMessager().printMessage(ERROR, "@FlagDesc must annotate a static field.", e);
      return false;
    }
    if (e.getModifiers().contains(Modifier.PRIVATE)) {
      processingEnv
          .getMessager()
          .printMessage(ERROR, "@FlagDesc may not annotate a private field.", e);
      return false;
    }
    if (!e.asType().toString().startsWith(Flag.class.getCanonicalName())) {
      processingEnv
          .getMessager()
          .printMessage(
              ERROR,
              "@FlagDesc must annotate a static " + Flag.class.getCanonicalName() + " field.",
              e);
      return false;
    }
    return true;
  }
}
