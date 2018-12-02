package types;

import java.util.Objects;

public class PrivateConstructor {
    public int n;
    private PrivateConstructor() {
        n = 10;
    }
    public static PrivateConstructor createInstance() {
        return new PrivateConstructor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrivateConstructor that = (PrivateConstructor) o;
        return n == that.n;
    }

    @Override
    public int hashCode() {
        return Objects.hash(n);
    }
}
