package types;

import java.util.Objects;

public class SimpleComplexState {
    private String message = "Hello";
    private Integer n = Integer.valueOf(10);
    private Float f = Float.valueOf(3.14f);
    private Double d = Double.valueOf(1000.444);
    private Byte b = Byte.valueOf((byte) 1);
    private Short sh = Short.valueOf((short)10);
    private Boolean boo = Boolean.valueOf(true);

    private SimpleState obj = new SimpleState();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleComplexState that = (SimpleComplexState) o;
        return Objects.equals(message, that.message) &&
                Objects.equals(n, that.n) &&
                Objects.equals(f, that.f) &&
                Objects.equals(d, that.d) &&
                Objects.equals(b, that.b) &&
                Objects.equals(sh, that.sh) &&
                Objects.equals(boo, that.boo) &&
                Objects.equals(obj, that.obj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, n, f, d, b, sh, boo, obj);
    }
}
