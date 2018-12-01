package types;

import java.util.Map;
import java.util.Objects;

public class WithMap {
    public Map<Object, Object> mapField;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithMap withMap = (WithMap) o;
        return Objects.equals(mapField, withMap.mapField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapField);
    }
}
