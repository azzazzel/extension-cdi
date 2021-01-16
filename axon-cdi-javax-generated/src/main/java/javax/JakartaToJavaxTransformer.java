package javax;


import de.icongmbh.oss.maven.plugin.javassist.ClassTransformer;
import javassist.*;
import javassist.build.JavassistBuildException;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JakartaToJavaxTransformer extends ClassTransformer {

    @Override
    public void applyTransformations(CtClass ctClass) throws JavassistBuildException {

        try {

            ClassPool classPool = ctClass.getClassPool();

            // Common references to the old class

            String className = ctClass.getName();
            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            System.out.println("--- " + className + " ---");

            // Make new class

            CtClass newCtClass = ctClass.getClassPool().makeClass(transform(className));

            // Common references to the new class

            String newClassName = newCtClass.getName();
            ClassFile newClassFile = newCtClass.getClassFile();
            ConstPool newConstPool = newClassFile.getConstPool();

            System.out.println("created class: " + newClassName);

            // transformation map

            ClassMap transformationMap = new ClassMap();
            transformationMap.put(ctClass.getName(), newCtClass.getName());
            transformationMap.put("jakarta.enterprise.context.ApplicationScoped", "javax.enterprise.context.ApplicationScoped");

            // add parent

            newCtClass.setSuperclass(ctClass.getSuperclass());
            System.out.println("\t extends: " + newCtClass.getSuperclass().getName());

            // add interfaces

            Arrays.stream(ctClass.getInterfaces()).forEach(iface -> {
                newCtClass.addInterface(iface);
                System.out.println("\t implements: " + iface.getName());
            });

            // Copy exceptions

            /*
                TODO Copy exceptions
             */

            // Copy attributes

            classFile.getAttributes().forEach(attributeInfo -> {
                AttributeInfo newAttributeInfo = attributeInfo.copy(newConstPool, transformationMap);
                newClassFile.addAttribute(newAttributeInfo);
                System.out.println("\t attribute: " + newAttributeInfo);
            });

            // Copy static initializer

            /*
                TODO Copy static initializer
             */

//            Arrays.stream(ctClass.getDeclaredBehaviors()).forEach(ctBehavior -> {
//                System.out.println("ctBehavior: " + ctBehavior);
//            });


            // Copy the fields

            for (CtField field : ctClass.getDeclaredFields()) {

                // Common references to the old field

                String fieldName = field.getName();
                String fieldType = field.getType().getName();
                FieldInfo fieldInfo = field.getFieldInfo();

                // create new field

                String newFieldType = transform(fieldType);
                CtField newField = new CtField(
                        classPool.get(newFieldType),
                        field.getName(),
                        newCtClass);

                System.out.println("\t field: " + fieldName + " of type " + newFieldType);

                // Common references to the new field

                FieldInfo newFieldInfo = newField.getFieldInfo();

                // configure field

                newField.setModifiers(field.getModifiers());
                System.out.println("\t\t modifiers: " + newField.getModifiers());

                newFieldInfo.setAccessFlags(fieldInfo.getAccessFlags());
                System.out.println("\t\t access flags: " + newFieldInfo.getAccessFlags());

                newFieldInfo.setDescriptor(fieldInfo.getDescriptor());
                System.out.println("\t\t descriptor: " + newFieldInfo.getDescriptor());

                String genericSignature = field.getGenericSignature();
                if (genericSignature != null) {
                    newField.setGenericSignature(transformSignature(genericSignature));
                    System.out.println("\t\t signature: " + newField.getSignature());
                }

                // Copy field's attributes

                fieldInfo.getAttributes().forEach(attributeInfo -> {
                    AttributeInfo newFieldAttributeInfo = attributeInfo.copy(newConstPool, transformationMap);
                    newField.getFieldInfo().addAttribute(newFieldAttributeInfo);
                    System.out.println("\t\t attribute: " + newFieldAttributeInfo);
                });



                System.out.println("?????: " + field.getFieldInfo().getConstPool());
                System.out.println("?????: " + field.getFieldInfo().getConstantValue());

//                ctClass.toClass().getDeclaredField(field.getName()).get


                newCtClass.addField(newField, CtField.Initializer.byExpr("null;"));
            }

            for (CtMethod method : ctClass.getDeclaredMethods()) {
                System.out.println("--- " + className + " : method " + method.getName() + " ---");
                transformMethod(ctClass, method);


//                CtMethod newMethod = new CtMethod(method, newCtClass, transformationMap);
                CtMethod newMethod = CtNewMethod.copy(method, newCtClass, null);
//                for(AttributeInfo attribute: method.getMethodInfo().getAttributes()) {
//                    newMethod.getMethodInfo().addAttribute(attribute);
//                }
//                newMethod.addParameter(ctClass.getClassPool().getOrNull("java.lang.String"));


//                if (method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag) != null) {
//                    newMethod.getMethodInfo().addAttribute(
//                            new AttributeInfo()
//                            method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag)
//                    );
//                }

                copyAnnotationsAttribute(newMethod, method);

                newCtClass.addMethod(newMethod);
            }

//            ctClass.setName(transform(className));

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

            newCtClass.writeFile(getSaveLocation(ctClass));
//            Paths.get(ctClass.getClassPool().find(ctClass.getName()).toURI()).toFile().delete();


            System.out.println("\t " + className + "  -->  " + ctClass.getName());
        } catch (NotFoundException | CannotCompileException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }


    private void copyAnnotationsAttribute(CtMethod newMethod, CtMethod oldMethod) {

        AnnotationsAttribute annotationsAttributeOld = (AnnotationsAttribute) oldMethod.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (annotationsAttributeOld != null) {
            MethodInfo methodInfoNew = newMethod.getMethodInfo();
            ConstPool cp = methodInfoNew.getConstPool();

            AnnotationsAttribute annotationsAttributeNew = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
            Arrays.stream(annotationsAttributeOld.getAnnotations()).forEach(annotation -> {
                Annotation newAnnotation = new Annotation(annotation.getTypeName(), cp);
                if (annotation.getMemberNames() != null) {
                    annotation.getMemberNames().forEach(member -> {
                        newAnnotation.addMemberValue(member, annotation.getMemberValue(member));
                    });
                }
                annotationsAttributeNew.addAnnotation(newAnnotation);
            });

            methodInfoNew.addAttribute(annotationsAttributeNew);
        }
//        annotationNew.addMemberValue(name, new BooleanMemberValue(value, cp));
//        attributeNew.setAnnotation(annotationNew);

    }

    private void copyAnnotationsAttribute(CtField newField, CtField oldField) {

        AnnotationsAttribute annotationsAttributeOld = (AnnotationsAttribute) oldField.getFieldInfo().getAttribute(AnnotationsAttribute.visibleTag);
        if (annotationsAttributeOld != null) {
            FieldInfo fieldInfoNew = newField.getFieldInfo();
            ConstPool cp = fieldInfoNew.getConstPool();

            AnnotationsAttribute annotationsAttributeNew = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
            Arrays.stream(annotationsAttributeOld.getAnnotations()).forEach(annotation -> {
                Annotation newAnnotation = new Annotation(transform(annotation.getTypeName()), cp);
                if (annotation.getMemberNames() != null) {
                    annotation.getMemberNames().forEach(member -> {
                        newAnnotation.addMemberValue(member, annotation.getMemberValue(member));
                    });
                }
                annotationsAttributeNew.addAnnotation(newAnnotation);
            });

            fieldInfoNew.addAttribute(annotationsAttributeNew);
        }
//        annotationNew.addMemberValue(name, new BooleanMemberValue(value, cp));
//        attributeNew.setAnnotation(annotationNew);

    }


    private String getSaveLocation(CtClass ctClass) throws URISyntaxException {
        Path saveLocation = Paths.get(ctClass.getClassPool().find(ctClass.getName()).toURI());
        while (!saveLocation.endsWith("classes")) {
            System.out.println(saveLocation.toString());
            saveLocation = saveLocation.getParent();
        }
        System.out.println(saveLocation.toString());

        return saveLocation.toString();
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
        if (name == null) return null;
        return name.replace("Ljakarta/", "Ljavax/");
    }

    @Override
    public boolean shouldTransform(CtClass ctClass) throws JavassistBuildException {
        System.out.println("checking " + ctClass.getName());
        return ctClass.getName().startsWith("org.axonframework.extensions.cdi.jakarta.");
    }
}


