package types;

public class Stateless {

    // made public unit testing purposes
    public static long serialVersionUID = 12;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
