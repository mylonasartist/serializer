package types;

import java.util.Objects;

public class Subclass extends Superclass {

    public ComplexState complexObj = new ComplexState();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subclass subclass = (Subclass) o;
        return Objects.equals(complexObj, subclass.complexObj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), complexObj);
    }
}
