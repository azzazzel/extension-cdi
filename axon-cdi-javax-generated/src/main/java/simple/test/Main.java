package simple.test;

import javassist.*;
import javassist.bytecode.AttributeInfo;

import java.util.List;

public class Main {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, NoSuchFieldException {

        ClassPool pool = ClassPool.getDefault();
        CtClass cc = pool.get("simple.test.ClassToBeTransformed");

        ClassMap classMap = new ClassMap();
        classMap.put("simple.test.oldpkg.AClass","simple.test.newpkg.AClass");



        CtField field = cc.getDeclaredField("aClass");
        CtField newField = new CtField(
                cc.getClassPool().get("simple.test.newpkg.AClass"),
                field.getName(),
                cc);

        cc.removeField(field);
        cc.addField(newField);

        newField.getFieldInfo().getAttributes().clear();
        field.getFieldInfo().getAttributes().stream()
                .map(attributeInfo -> attributeInfo.copy(attributeInfo.getConstPool(), classMap))
                .forEach(attributeInfo -> newField.getFieldInfo().addAttribute(attributeInfo));


        CtMethod method = cc.getDeclaredMethod("doSomethingMore");
        CtMethod newMethod = CtNewMethod.copy(method, cc,classMap);
        cc.removeMethod(method);
        cc.addMethod(newMethod);

        newMethod.getMethodInfo().getAttributes().clear();
        method.getMethodInfo().getAttributes().stream()
                .map(attributeInfo -> attributeInfo.copy(attributeInfo.getConstPool(), classMap))
                .forEach(attributeInfo -> newMethod.getMethodInfo().addAttribute(attributeInfo));


//        ClassToBeTransformed classToBeTransformed = new ClassToBeTransformed();
//        classToBeTransformed.doSomethingMore();

        Class clazz = cc.toClass();
        System.out.println("type is " + clazz.getDeclaredField("aClass").getType());
        ClassToBeTransformed classToBeTransformed = (ClassToBeTransformed) clazz.newInstance();
        classToBeTransformed.doSomethingMore();

    }
}
