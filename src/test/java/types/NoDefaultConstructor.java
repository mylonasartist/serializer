package types;

import java.util.Objects;

public class NoDefaultConstructor {
    Integer n;
    public NoDefaultConstructor(Integer n) {
        this.n = n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoDefaultConstructor that = (NoDefaultConstructor) o;
        return n == that.n;
    }

    @Override
    public int hashCode() {
        return Objects.hash(n);
    }
}
