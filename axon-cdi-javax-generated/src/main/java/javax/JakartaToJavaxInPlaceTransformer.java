package javax;


import de.icongmbh.oss.maven.plugin.javassist.ClassTransformer;
import javassist.*;
import javassist.build.JavassistBuildException;
import javassist.bytecode.*;
import javassist.bytecode.annotation.Annotation;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JakartaToJavaxInPlaceTransformer extends ClassTransformer {

    private static final String FROM = "jakarta";
    private static final String TO = "javax";
    private static final ClassMap MAP = getTransformationMap();
    private static final String ORIG_FIELD_SUFFIX = "$orig";
    private static final String NEW_FIELD_SUFFIX = "$generated";

    private static final ClassMap getTransformationMap() {
        ClassMap map = new ClassMap();
        map.put("jakarta.enterprise.context.ApplicationScoped", "javax.enterprise.context.ApplicationScoped");
        map.put("jakarta.enterprise.inject.Default", "javax.enterprise.inject.Default");
        map.put("jakarta.enterprise.inject.Typed", "javax.enterprise.inject.Typed");
        map.put("jakarta.enterprise.inject.Produces", "javax.enterprise.inject.Produces");
        map.put("jakarta.enterprise.inject.Instance", "javax.enterprise.inject.Instance");
        map.put("jakarta.inject.Inject", "javax.inject.Inject");


        System.out.println("MAP: " + map);

        return map;
    }


    @Override
    public void applyTransformations(CtClass ctClass) throws JavassistBuildException {

        try {

            ClassPool classPool = ctClass.getClassPool();

            // Common references

            String className = ctClass.getName();
            ClassFile classFile = ctClass.getClassFile();
            ConstPool constPool = classFile.getConstPool();
            System.out.println("--- " + className + " ---");


            // package

            String newClassName = transformPackage(className);
            if (!newClassName.equals(className)) {
                ctClass.setName(newClassName);
                System.out.println("\t name: " + newClassName);
            }

            // parent

            // TODO: check if parent has jakarta generic type

            // interfaces

            // TODO: check if any interface has jakarta generic type

            // exceptions

            // TODO check if any exception has jakarta generic type

            // annotations

            // TODO check if any annotation uses jakarta types

            List<AttributeInfo> attributes = classFile.getAttributes();

            transformAnnotations(constPool, attributes);


            //  TODO check if  static initializer references jakarta types

            // fields

            for (CtField field : ctClass.getDeclaredFields()) {

                // transform if needed

                if (shouldTransform(field)) {
                    transform(field, MAP);
                }

            }

            for (CtMethod method : ctClass.getDeclaredMethods()) {

                CtMethod newMethod = CtNewMethod.copy(method, ctClass, MAP);
                ctClass.removeMethod(method);
                ctClass.addMethod(newMethod);

                newMethod.getMethodInfo().getAttributes().clear();
                method.getMethodInfo().getAttributes().stream()
                        .map(attributeInfo -> attributeInfo.copy(attributeInfo.getConstPool(), MAP))
                        .forEach(attributeInfo -> newMethod.getMethodInfo().addAttribute(attributeInfo));


                // transform if needed

//                if (shouldTransform(method)) {
//                    transform(method, MAP);
//                }

//                Bytecode code = new Bytecode(constPool);
//                CodeAttribute ca = method.getMethodInfo().getCodeAttribute();
//                CodeIterator ci = ca.iterator();
//                while (ci.hasNext()) {
//                    int index = ci.next();
//                    int op = ci.byteAt(index);
//                    System.out.println("CI index: " + index);
//                    System.out.println("CI OPCODE:" + Mnemonic.OPCODE[op]);
//
//                    CodeAttribute codeAttribute = ci.get();
//                    List<AttributeInfo> attributeInfos = codeAttribute.getAttributes().stream()
//                            .map(attributeInfo -> {
//                                System.out.println("CI attribute: " + attributeInfo);
//                                if (attributeInfo instanceof LocalVariableAttribute) {
//                                    LocalVariableAttribute localVariableAttribute = (LocalVariableAttribute) attributeInfo;
//                                    return localVariableAttribute.copy(constPool, MAP);
//                                }
//                                return attributeInfo;
//                            })
//                            .collect(Collectors.toList());
//
//                    codeAttribute.getAttributes().clear();
//                    codeAttribute.getAttributes().addAll(attributeInfos);
//                    codeAttribute.computeMaxStack();
//                    ci.write(codeAttribute.getCode(), index);
//                }
//                method.getMethodInfo().setCodeAttribute(ca);


//                List<AttributeInfo> attributeInfos = method.getMethodInfo().getAttributes()
//                        .stream().map(attributeInfo -> attributeInfo.copy(constPool, MAP))
//                        .collect(Collectors.toList());
//
//                method.getMethodInfo().getAttributes().clear();
//                method.getMethodInfo().getAttributes().addAll(attributeInfos);
//
//
//                method.instrument(new ExprEditor() {
//
//                    public void edit(MethodCall methodCall) throws CannotCompileException {
//                        String methodName = methodCall.getMethodName();
//                        System.out.println(" !!!! MethodCall !!!! " + methodName);
//                        System.out.println(" !!!! MethodCall class name !!!! " + methodCall.getClassName());
//
//                        if (shouldTransform(methodCall)) {
//                            String transformedClass = transformPackage(methodCall.getClassName());
//
////                            methodCall.replace("$_=$;");
////                            methodCall.replace("$_=java.util.stream.Stream.of(externalCommandHandlers$generated);");
////                            methodCall.replace("$_=java.util.stream.Stream.empty();");
////                            methodCall.replace("$_=" + transformedClass
////                                    + "." + methodName + "($$);");
////                            methodCall.replace("$_=(" + transformedClass
////                                    + ")" + methodName + "($$);");
//                        }
//
//                    }
//
//                    public void edit(FieldAccess fieldAccess) throws CannotCompileException {
//                        String fieldName = fieldAccess.getFieldName();
//
//                        System.out.println(" !!!! FieldAccess !!!! " + fieldName);
//                        System.out.println(" !!!! FieldAccess sig !!!! " + fieldAccess.getSignature());
//
////                        if (fieldAccess.isReader()) {
////                            fieldAccess.replace("$_=" + fieldName + NEW_FIELD_SUFFIX + ";");
////                        } else if (fieldAccess.isWriter()) {
////                            fieldAccess.replace(fieldName+ NEW_FIELD_SUFFIX + "= $1;");
////                        }
//
////                        if (fieldAccess.isReader()) {
////                            fieldAccess.replace("$_=" + fieldName + ";");
////                        } else if (fieldAccess.isWriter()) {
//////                            fieldAccess.replace(fieldName+ NEW_FIELD_SUFFIX + "= $1;");
////                        }
//
//                    }
//                });
            }

        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }

    }

    private void transformAnnotations(ConstPool constPool, List<AttributeInfo> attributes) {
        List<AttributeInfo> annotationAttributes = removeAnnotationAttributes(attributes);
        addTransformedAnnotationAttributes(constPool, attributes, annotationAttributes);
    }

    private void addTransformedAnnotationAttributes(ConstPool constPool, List<AttributeInfo> attributes, List<AttributeInfo> annotationAttributes) {
        List<AttributeInfo> transformedAttributes = annotationAttributes.stream()
                .peek(attributeInfo -> System.out.println("\t transforming annotation(s): " + attributeInfo))
                .map(attributeInfo -> attributeInfo.copy(constPool, MAP))
                .peek(attributeInfo -> System.out.println("\t \t to: " + attributeInfo))
                .collect(Collectors.toList());
        attributes.addAll(transformedAttributes);
    }

    private List<AttributeInfo> removeAnnotationAttributes(List<AttributeInfo> attributes) {
        List<AttributeInfo> annotationAttributes = attributes.stream()
                .filter(attributeInfo -> attributeInfo.getClass().isAssignableFrom(AnnotationsAttribute.class))
                .collect(Collectors.toList());
        attributes.removeAll(annotationAttributes);
        return annotationAttributes;
    }


    private void transform(CtMethod method, ClassMap transformationMap) throws CannotCompileException {
        String methodName = method.getName();
        MethodInfo methodInfo = method.getMethodInfo();
        ConstPool constPool = methodInfo.getConstPool();
        ClassPool classPool = method.getDeclaringClass().getClassPool();
        CtClass ctClass = method.getDeclaringClass();

        System.out.println("--- " + ctClass.getName() + " :: " + method.getSignature() + " ---");

//        List<AttributeInfo> attributes = methodInfo.getAttributes();
//        if (shouldTransformAnnotations(attributes)) {
//            transformAnnotations(method);
//        }

    }

    private void transform(CtField field, ClassMap transformationMap) throws CannotCompileException, NotFoundException {

        String fieldName = field.getName();
        String fieldType = field.getType().getName();
        FieldInfo fieldInfo = field.getFieldInfo();
        ConstPool constPool = fieldInfo.getConstPool();
        ClassPool classPool = field.getDeclaringClass().getClassPool();
        CtClass ctClass = field.getDeclaringClass();
        String changedOriginalFieldName = fieldName + ORIG_FIELD_SUFFIX;
        String newFieldType = transformPackage(fieldType);
//        String newFieldName = fieldName + NEW_FIELD_SUFFIX;

        System.out.println("--- " + ctClass.getName() + " :: " + fieldName + " ---");


        List<AttributeInfo> attributes = fieldInfo.getAttributes();
        if (shouldTransformAnnotations(attributes)) {
            transformAnnotations(constPool, attributes);
        }

        if (shouldTransformSignature(field)) {
            System.out.println("\t transforming signature: " + fieldType);
            System.out.println("\t \t to: " + newFieldType);
            ctClass.removeField(field);
//            field.setName(changedOriginalFieldName);
            CtField newField = new CtField(
                    classPool.get(newFieldType),
//                    newFieldName,
                    fieldName,
                    ctClass);

            ctClass.addField(newField);

            String genericSignature = field.getGenericSignature();
            if (genericSignature != null) {
                newField.setGenericSignature(transformSignature(genericSignature));
            }

            attributes.stream()
                    .filter(attributeInfo -> attributeInfo.getClass().isAssignableFrom(AnnotationsAttribute.class))
                    .forEach(attributeInfo -> newField.getFieldInfo().addAttribute(attributeInfo));

        }


    }

    private String transformSignature(String name) {
        if (name == null) return null;
        return name.replace("L" + FROM + "/", "L" + TO + "/");
    }

    private String transformPackage(String name) {
        if (name == null) {
            return null;
        }
        return name.replace(FROM + ".", TO + ".");
    }

    private boolean shouldTransform(CtMethod method) {
        // TODO check type, parameters, body references and annotations
        return shouldTransformAnnotations(method.getMethodInfo().getAttributes());
    }

    private boolean shouldTransform(CtField field) {
        return shouldTransformSignature(field) || shouldTransformAnnotations(field.getFieldInfo().getAttributes());
    }

    private boolean shouldTransformSignature(CtField field) {
        return field.getSignature().contains(FROM);
    }

    private boolean shouldTransformAnnotations(List<AttributeInfo> attributes) {
        if (attributes == null) return false;
        return attributes.stream().anyMatch(attribute -> shouldTransformAnnotation(attribute));
    }

    private boolean shouldTransformAnnotation(AttributeInfo attributeInfo) {
        if (attributeInfo.getClass().isAssignableFrom(AnnotationsAttribute.class)) {
            AnnotationsAttribute annotationsAttribute = (AnnotationsAttribute) attributeInfo;
            return Arrays.stream(annotationsAttribute.getAnnotations())
                    .map(a -> a.getTypeName())
                    .anyMatch(a -> a.startsWith(FROM + "."))
                    ;
        }
        return false;
    }

    private boolean shouldTransform(MethodCall methodCall) {
        return methodCall.getClassName().startsWith(FROM + ".");
    }

    @Override
    public boolean shouldTransform(CtClass ctClass) throws JavassistBuildException {
        System.out.println("checking " + ctClass.getName());
        return ctClass.getName().startsWith("org.axonframework.extensions.cdi.jakarta.");
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
        Annotation newAnnotation = new Annotation(transformPackage(annotation.getTypeName()), constPool);
        if (annotation.getMemberNames() != null) {
            annotation.getMemberNames().forEach(name -> {
                newAnnotation.addMemberValue(name, annotation.getMemberValue(name));
            });
        }
        annotationsAttribute.addAnnotation(newAnnotation);
        System.out.println("\t " + annotation + "  -->  " + newAnnotation);

    }

}


