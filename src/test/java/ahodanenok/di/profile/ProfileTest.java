package ahodanenok.di.profile;

import ahodanenok.di.DIContainer;
import ahodanenok.di.profile.classes.ClassWithMultipleProfiles;
import ahodanenok.di.profile.classes.ClassWithSingleProfile;
import ahodanenok.di.profile.classes.FieldProfiles;
import ahodanenok.di.profile.classes.MethodProfiles;
import ahodanenok.di.value.InstanceValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    @Test
    public void testSingleProfileResolvedForClass() {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("test", resolution.resolve(ClassWithSingleProfile.class));
    }

    @Test
    public void testMultipleProfilesResolvedForClass() {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("(a)&(!b)", resolution.resolve(ClassWithMultipleProfiles.class));
    }

    @Test
    public void testSingleProfileResolvedForField() throws Exception {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("abc", resolution.resolve(FieldProfiles.class.getDeclaredField("singleProfile")));
    }

    @Test
    public void testMultipleProfilesResolvedForField() throws Exception {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("(a & f)&(!b)&(c)", resolution.resolve(FieldProfiles.class.getDeclaredField("multipleProfiles")));
    }

    @Test
    public void testSingleProfileResolvedForMethod() throws Exception {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("abc", resolution.resolve(MethodProfiles.class.getDeclaredMethod("singleProfile")));
    }

    @Test
    public void testMultipleProfilesResolvedForMethod() throws Exception {
        AnnotatedProfileResolution resolution = new AnnotatedProfileResolution();
        Assertions.assertEquals("(a & f)&(!b)&(c)", resolution.resolve(MethodProfiles.class.getDeclaredMethod("multipleProfiles")));
    }

    @Test
    public void testValueNotIncludedWhenProfileNotActive() {
        InstanceValue<String> v = new InstanceValue<>("1");
        v.metadata().setProfilesCondition("test");

        DIContainer container = DIContainer.builder()
                .addValue(v)
                .build();
        Assertions.assertNull(container.instance(String.class));
    }
}
