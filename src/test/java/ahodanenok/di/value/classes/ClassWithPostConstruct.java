package ahodanenok.di.value.classes;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class ClassWithPostConstruct {

    @Inject
    public int n;
    public String s;
    public float f;

    public Object[] objects;

    @Inject
    public ClassWithPostConstruct(String s) {
        this.s = s;
    }

    @Inject
    public void setF(float f) {
        this.f = f;
    }

    @PostConstruct
    private void init() {
        objects = new Object[] { n, s, f};
    }
}
