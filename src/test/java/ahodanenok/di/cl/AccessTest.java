package ahodanenok.di.cl;

import javax.inject.Inject;

public class AccessTest {

    @Inject
    private String privateField;

    @Inject
    protected String protectedField;

    @Inject
    public String publicField;

    @Inject
    String defaultField;

    @Inject
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

    public String getDefaultField() {
        return defaultField;
    }
}
