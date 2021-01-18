package example.javacompiler.processor;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

@SupportedAnnotationTypes({ "example.javacompiler.classes.MyComponent" })
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class AnnotationProcessorDemo extends AbstractProcessor {

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		for (final TypeElement te : annotations) {
			for (final TypeElement e : ElementFilter
					.typesIn(roundEnv.getElementsAnnotatedWith(te))) {
				System.out.println("********************************");
				System.out.println(e.getQualifiedName());
				System.out.println(e.getSuperclass());
				System.out.println(e.getInterfaces());
				for (final ExecutableElement constructor : ElementFilter
						.constructorsIn(e.getEnclosedElements())) {
					System.out.println(constructor);
				}
			}
		}
		return false;
	}
}
