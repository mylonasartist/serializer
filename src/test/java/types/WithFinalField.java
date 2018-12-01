package types;

import java.util.Objects;

public class WithFinalField {
    private final String foo;

    public WithFinalField() {
        this.foo = "Some initial default value";
    }

    public WithFinalField(String initValue) {
        this.foo = initValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithFinalField that = (WithFinalField) o;
        return Objects.equals(foo, that.foo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foo);
    }
}
