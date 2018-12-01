package types;

import java.util.Objects;

public class SimpleComplexState {
    private String message = "Hello";

    private SimpleState obj = new SimpleState();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SimpleComplexState that = (SimpleComplexState) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, obj);
    }
}
