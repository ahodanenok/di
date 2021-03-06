package ahodanenok.di.metadata;

import ahodanenok.di.DIContainer;
import ahodanenok.di.metadata.classes.*;
import ahodanenok.di.scope.ScopeIdentifier;
import ahodanenok.di.value.FieldProviderValue;
import ahodanenok.di.value.InstantiatingValue;
import ahodanenok.di.value.MethodProviderValue;
import ahodanenok.di.value.metadata.MutableValueMetadata;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class MetadataTest {

    @Test
    public void testClassDefaultMetadata() {
        InstantiatingValue<DefaultClassMetadata> v = new InstantiatingValue<>(DefaultClassMetadata.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testClassEager() {
        InstantiatingValue<EagerClass> v = new InstantiatingValue<>(EagerClass.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testClassEagerPhased() {
        InstantiatingValue<EagerClassPhased> v = new InstantiatingValue<>(EagerClassPhased.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(20, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testClassPrimary() {
        InstantiatingValue<PrimaryClass> v = new InstantiatingValue<>(PrimaryClass.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertTrue(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testClassDefault() {
        InstantiatingValue<DefaultClass> v = new InstantiatingValue<>(DefaultClass.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testClassInterceptor() {
        InstantiatingValue<InterceptorClass> v = new InstantiatingValue<>(InterceptorClass.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.singleton(InterceptorClass.class.getAnnotation(InterceptStuff.class)), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertTrue(metadata.isInterceptor());
    }

    @Test
    public void testClassFull() {
        InstantiatingValue<LotOfMetadataClass> v = new InstantiatingValue<>(LotOfMetadataClass.class);
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.SINGLETON, metadata.getScope());
        assertEquals("lotOfMetadataClass", metadata.getName());
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierA.class)));
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierB.class)));
        assertEquals(Collections.singleton(LotOfMetadataClass.class.getAnnotation(PackedStereotype.class)), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(1, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodDefaultMetadata() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("defaultMetadata"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodEager() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("eager"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodEagerPhase() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("eagerPhased"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(10, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodPrimary() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("primaryValue"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertTrue(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodDefault() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("defaultValue"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodInterceptorBindings() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("intercepted"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(
                Collections.singleton(
                    MethodMetadata.class.getDeclaredMethod("intercepted").getAnnotation(InterceptStuff.class)),
                metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testMethodFull() throws Exception {
        MethodProviderValue<String> v = new MethodProviderValue<>(
                String.class, MethodMetadata.class.getDeclaredMethod("lotOfMetadata"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.SINGLETON, metadata.getScope());
        assertEquals("lotOfMetadata", metadata.getName());
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierA.class)));
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierB.class)));
        assertEquals(Collections.singleton(LotOfMetadataClass.class.getAnnotation(PackedStereotype.class)), metadata.getStereotypes());
        assertEquals(
                Collections.singleton(
                        MethodMetadata.class.getDeclaredMethod("lotOfMetadata").getAnnotation(InterceptStuff.class)),
                metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testFieldDefaultMetadata() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(
                String.class, FieldMetadata.class.getDeclaredField("defaultMetadata"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testFieldPrimary() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(
                String.class, FieldMetadata.class.getDeclaredField("primaryValue"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertTrue(metadata.isPrimary());
        assertFalse(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testFieldDefault() throws Exception {
        FieldProviderValue<String> v = new FieldProviderValue<>(
                String.class, FieldMetadata.class.getDeclaredField("defaultValue"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.NOT_SCOPED, metadata.getScope());
        assertNull(metadata.getName());
        assertEquals(Collections.emptySet(), metadata.getQualifiers());
        assertEquals(Collections.emptySet(), metadata.getStereotypes());
        assertEquals(Collections.emptySet(), metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertFalse(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }

    @Test
    public void testFieldFull() throws Exception {
        FieldProviderValue<Object> v = new FieldProviderValue<>(
                Object.class, FieldMetadata.class.getDeclaredField("lotOfMetadata"));
        v.bind(DIContainer.builder().build());
        MutableValueMetadata metadata = v.metadata();

        assertEquals(ScopeIdentifier.SINGLETON, metadata.getScope());
        assertEquals("lotOfMetadata", metadata.getName());
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierA.class)));
        assertTrue(metadata.getQualifiers().contains(LotOfMetadataClass.class.getAnnotation(QualifierB.class)));
        assertEquals(Collections.singleton(LotOfMetadataClass.class.getAnnotation(PackedStereotype.class)), metadata.getStereotypes());
        assertEquals(
                Collections.singleton(
                        MethodMetadata.class.getDeclaredMethod("intercepted").getAnnotation(InterceptStuff.class)),
                metadata.getInterceptorBindings());
        assertEquals(0, metadata.getInitializationPhase());
        assertEquals(0, metadata.getPriority());
        assertTrue(metadata.isEager());
        assertFalse(metadata.isPrimary());
        assertTrue(metadata.isDefault());
        assertFalse(metadata.isInterceptor());
    }
}
