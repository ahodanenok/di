package ahodanenok.di;

import ahodanenok.di.value.Value;

import java.lang.reflect.Member;

public final class InjectionPoint {

    private Value<?> value;
    private DependencyIdentifier<?> id;
    private Member target;

    public InjectionPoint(Value<?> value, DependencyIdentifier<?> id, Member target) {
        this.value = value;
        this.id = id;
        this.target = target;
    }

    /**
     * Value containing member which requested dependency injection
     * It will be either method, field or constructor
     */
    public Value<?> getValue() {
        return value;
    }

    /**
     * Id of dependency declared on the injection point
     */
    public DependencyIdentifier<?> getId() {
        return id;
    }

    /**
     * Target of the injection
     * It will be either method, field or constructor
     */
    public Member getTarget() {
        return target;
    }
}
