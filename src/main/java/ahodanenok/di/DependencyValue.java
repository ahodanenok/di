package ahodanenok.di;

import ahodanenok.di.scope.ScopeIdentifier;

import javax.inject.Provider;

// todo: some api to describe values (builders maybe), that they could be created by container lately
// todo: must have some sort of identifier by which it will be picked for providing dependency
public interface DependencyValue<T> {

    DependencyIdentifier<T> id();

    ScopeIdentifier scope();

    // todo: refactor to make this method without parameters
    Provider<? extends T> provider(DIContainer container);

//    public static <T> DIBindingSupplier<T>.Builder bind(Class<T> forType) {
//        DIBindingSupplier<T> binding = new DIBindingSupplier<>();
//
//        DIBindingSupplier.Builder builder = binding.new Builder();
//        return builder;
//    }
//
//    public class Builder {
//
//        private Description description;
//
//        public Builder(Class<T> type) {
//            description = new Description();
//            description.type = type;
//        }
//
//        public Builder withSupplier(Supplier<T> supplier) {
//            description.supplier = supplier;
//            return this;
//        }
//
//        Builder withInstance(T instance) {
//            description.instance = instance;
//            return this;
//        }
//
//        Builder withClass(Class<? extends T> clazz) {
//            description.instanceClass = clazz;
//            return this;
//        }
//
//        void in(DIBindings bindings) {
//            bindings.add(description);
//        }
//    }
//
//    class Description {
//
//        Class<T> type;
//        Supplier<? extends T> supplier;
//        T instance;
//        Class<?> instanceClass;
//    }
}
