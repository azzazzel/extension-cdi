package javax;


import de.icongmbh.oss.maven.plugin.javassist.ClassTransformer;
import javassist.*;
import javassist.build.JavassistBuildException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class JakartaToJavaxTransformer extends ClassTransformer {

    @Override
    public void applyTransformations(CtClass ctClass) throws JavassistBuildException {

        try {
            String originalClassName = ctClass.getName();

            System.out.println("--- " + originalClassName + " ---");

            transformAnnotations(ctClass);

            for (CtField field : ctClass.getFields()) {
                System.out.println("--- " + originalClassName + " : field " + field.getName() + " ---");
                transformField(field);
            }

            for (CtMethod method : ctClass.getMethods()) {
                System.out.println("--- " + originalClassName + " : method " + method.getName() + " ---");
                transformMethod(ctClass, method);
            }

            ctClass.setName(transform(originalClassName));

//            Set<String> jakartaClasses = new HashSet<>();
//
//            ctClass.getClassFile().getConstPool().getClassNames().forEach(c -> {
//                System.out.println("\t class: " + c);
//            });
//
//            jakartaClasses.addAll(
//                    ctClass.getClassFile().getConstPool().getClassNames().stream()
//                            .filter(className -> className.startsWith("jakarta/"))
//                            .collect(Collectors.toSet()));
//
//            System.out.println("jakarta classes: " + jakartaClasses);
//
//            jakartaClasses.forEach(c -> {
//                ctClass.getClassFile().getConstPool().renameClass(c, transform(c));
//            });

            System.out.println("\t " + originalClassName + "  -->  " + ctClass.getName());
        } catch (NotFoundException | CannotCompileException | BadBytecode e) {
            e.printStackTrace();
        }

    }

    private void transformAnnotations(CtClass ctClass) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) ctClass
                .getClassFile()
                .getAttribute(AnnotationsAttribute.visibleTag);
        if (annotationsAttribute == null) return;
        transformAnnotations(ctClass.getClassFile().getConstPool(), annotationsAttribute);
    }

    private void transformAnnotations(CtField field) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) field
                .getFieldInfo()
                .getAttribute(AnnotationsAttribute.visibleTag);
        if (annotationsAttribute == null) return;
        transformAnnotations(field.getFieldInfo().getConstPool(), annotationsAttribute);
    }

    private void transformAnnotations(CtMethod method) {
        AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) method
                .getMethodInfo()
                .getAttribute(AnnotationsAttribute.visibleTag);
        if (annotationsAttribute == null) return;
        transformAnnotations(method.getMethodInfo().getConstPool(), annotationsAttribute);
    }


    private void transformAnnotations(ConstPool constPool, AnnotationsAttribute annotationsAttribute) {
        Collection<Annotation> jakartaAnnotations = Arrays.stream(annotationsAttribute.getAnnotations())
                .filter(a -> a.getTypeName().startsWith("jakarta."))
                .collect(Collectors.toList());

        jakartaAnnotations.forEach(a -> {
            transformAnnotation(constPool, annotationsAttribute, a);
        });
    }

    private void transformAnnotation(ConstPool constPool, AnnotationsAttribute annotationsAttribute, Annotation annotation) {
        annotationsAttribute.removeAnnotation(annotation.getTypeName());
        Annotation newAnnotation = new Annotation(transform(annotation.getTypeName()), constPool);
        if (annotation.getMemberNames() != null) {
            annotation.getMemberNames().forEach(name -> {
                newAnnotation.addMemberValue(name, annotation.getMemberValue(name));
            });
        }
        annotationsAttribute.addAnnotation(newAnnotation);
        System.out.println("\t " + annotation + "  -->  " + newAnnotation);

    }

    private void transformField(CtField field) throws NotFoundException, CannotCompileException, BadBytecode {
        transformAnnotations(field);
        String type = field.getType().getName();
        if (subjectToChange(type)) {
            String newType = transform(type);
            field.getFieldInfo().getConstPool().addClassInfo(newType);
            field.setType(field.getDeclaringClass().getClassPool().get(newType));
            field.setGenericSignature(transformSignature(field.getGenericSignature()));
            System.out.println("\t " + type + "  -->  " + field.getType().getName());
        }


    }

    private void transformMethod(CtClass ctClass, CtMethod method) throws NotFoundException {
        transformAnnotations(method);

//        String returnType = method.getReturnType().getName();
//
//        if (subjectToChange(returnType)) {
//
//            ctClass.removeMethod(method);
//
//            CtNewMethod.
//
//            CtClass newType = method.getReturnType().getClassPool().get(changePackage(returnType));
//            CtMethod newMethod = new CtMethod(newType, method.getName(), method.getParameterTypes(), ctClass);
//            method.setWrappedBody();
//            cc.addMethod(m);


//            method.set(newType);
//            System.out.println(method.getDeclaringClass().getName() + ": " + returnType + " " + method.getName()
//                    + " --> " + newType.getName());
//        }
    }


    private boolean subjectToChange(String name) {
        return name.startsWith("jakarta.");
    }

    private String transform(String name) {
        return name.replace("jakarta.", "javax.");
    }

    private String transformSignature(String name) {
        return name.replace("Ljakarta/", "Ljavax/");
    }

    @Override
    public boolean shouldTransform(CtClass ctClass) throws JavassistBuildException {
        System.out.println("checking " + ctClass.getName());
        return ctClass.getName().startsWith("org.axonframework.extensions.cdi.jakarta.");
    }
}


