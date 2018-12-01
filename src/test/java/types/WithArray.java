package types;

import java.util.Arrays;

public class WithArray {
    public int[] arr = new int[] {1, 2, 3};
    public Integer[] arr2 = new Integer[] {12, 13};
    public Object[] arr3 = new Object[] {Float.valueOf(23.12f), "Hello", new SimpleState(), new ComplexState()};

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WithArray withArray = (WithArray) o;
        return Arrays.equals(arr, withArray.arr) &&
                Arrays.equals(arr2, withArray.arr2) &&
                Arrays.equals(arr3, withArray.arr3);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(arr);
        result = 31 * result + Arrays.hashCode(arr2);
        result = 31 * result + Arrays.hashCode(arr3);
        return result;
    }
}
