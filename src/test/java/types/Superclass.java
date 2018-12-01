package types;

import java.util.Objects;

public class Superclass {
    public ComplexState complexObjSuper = new ComplexState();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Superclass that = (Superclass) o;
        return Objects.equals(complexObjSuper, that.complexObjSuper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(complexObjSuper);
    }
}
