package ahodanenok.di.cl;

public class AccessTest {

    private String privateField;

    protected String protectedField;

    public String publicField;

    String packageField;

    final String finalField = "final";

    static String staticField;

    public static String getStaticField() {
        return staticField;
    }

    public String getPrivateField() {
        return privateField;
    }

    public String getProtectedField() {
        return protectedField;
    }

    public String getPublicField() {
        return publicField;
    }

    public String getPackageField() {
        return packageField;
    }

    private String privateMethod() {
        return "private";
    }

    protected String protectedMethod() {
        return "protected";
    }

    String packageMethod() {
        return "package";
    }

    String publicMethod() {
        return "public";
    }
}
