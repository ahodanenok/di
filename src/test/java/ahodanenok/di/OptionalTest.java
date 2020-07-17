package ahodanenok.di;

import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

public class OptionalTest {

    @Test
    public void testOptionalMethodParameter_1() throws Exception {
        class M {
            @Inject
            void method(@OptionalDependency int a) { }
        }

        DIContainer container = DIContainer.builder().build();
        assertThrows(RuntimeException.class, () -> new InjectableMethod(container, M.class.getDeclaredMethod("method", int.class)).inject(new M()));
    }

    @Test
    public void testOptionalMethodParameter_2() throws Exception {
        class M {
            Integer f = 10;

            @Inject
            void method(@OptionalDependency Integer a) {
                this.f = a;
            }
        }

        DIContainer container = DIContainer.builder().build();
        M obj = new M();
        new InjectableMethod(container, M.class.getDeclaredMethod("method", Integer.class)).inject(obj);
        assertNull(obj.f);
    }

    @Test
    public void testOptionalMethodParameter_3() throws Exception {
        class M {
            String s = "s";
            Integer f = 10;

            @Inject
            void method(String str, @OptionalDependency Integer a) {
                this.s = str;
                this.f = a;
            }
        }

        DIContainer container = DIContainer.builder().addValue(new DependencyInstanceValue<Object>("check")).build();
        M obj = new M();
        new InjectableMethod(container, M.class.getDeclaredMethod("method", String.class, Integer.class)).inject(obj);
        assertEquals("check", obj.s);
        assertNull(obj.f);
    }


    @Test
    public void testOptionalConstructorParameter_1() throws Exception {
        class M {
            @Inject
            M(@OptionalDependency int a) { }
        }

        DIContainer container = DIContainer.builder().build();
        assertThrows(RuntimeException.class, () -> new InjectableConstructor<>(container, M.class.getDeclaredConstructor(OptionalTest.class, int.class)).inject());
    }

    @Test
    public void testOptionalConstructorParameter_2() throws Exception {
        class M {
            Integer f = 10;

            @Inject
            M(@OptionalDependency Integer a) {
                this.f = a;
            }
        }

        DIContainer container = DIContainer.builder().build();
        M obj = new InjectableConstructor<>(container, M.class.getDeclaredConstructor(OptionalTest.class, Integer.class)).inject();
        assertNull(obj.f);
    }

    @Test
    public void testOptionalConstructorParameter_3() throws Exception {
        class M {
            String s = "s";
            Integer f = 10;

            @Inject
            M(String str, @OptionalDependency Integer a) {
                this.s = str;
                this.f = a;
            }
        }

        DIContainer container = DIContainer.builder().addValue(new DependencyInstanceValue<Object>("check")).build();
        M obj = new InjectableConstructor<>(container, M.class.getDeclaredConstructor(OptionalTest.class, String.class, Integer.class)).inject();
        assertEquals("check", obj.s);
        assertNull(obj.f);
    }

    @Test
    public void testOptionalField_1() throws Exception {
        class M {
            @Inject
            @OptionalDependency
            int a;
        }

        DIContainer container = DIContainer.builder().build();
        assertThrows(RuntimeException.class, () -> new InjectableField(container, M.class.getDeclaredField("a")).inject(new M()));
    }

    @Test
    public void testOptionalField_2() throws Exception {
        class M {
            @Inject
            @OptionalDependency
            Integer f = 10;
        }

        DIContainer container = DIContainer.builder().build();
        M obj = (M) new InjectableField(container, M.class.getDeclaredField("f")).inject(new M());
        assertNull(obj.f);
    }
}
