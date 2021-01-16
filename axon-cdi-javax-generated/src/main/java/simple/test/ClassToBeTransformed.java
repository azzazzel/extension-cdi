package simple.test;

public class ClassToBeTransformed {

    private simple.test.oldpkg.AClass aClass;

    public void doSomethingMore () {
        aClass = new simple.test.oldpkg.AClass();
        System.out.println("did something before");
        aClass.doSomething();
        System.out.println("did something after");
    }

}
