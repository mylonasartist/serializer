package types;

import java.util.Arrays;

public class WithArray {
    private int[] arr = new int[] {1, 2, 3};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithArray withArray = (WithArray) o;
        return Arrays.equals(arr, withArray.arr);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }
}
