package types;

import java.util.Objects;

public class ComplexState {
    public String message = "Hello";
    public Integer n = Integer.valueOf(10);
    public Float f = Float.valueOf(3.14f);
    public Double d = Double.valueOf(1000.444);
    public Byte b = Byte.valueOf((byte) 1);
    public Short sh = Short.valueOf((short)10);
    public Boolean boo = Boolean.valueOf(true);

    public SimpleState obj = new SimpleState();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexState that = (ComplexState) o;
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
