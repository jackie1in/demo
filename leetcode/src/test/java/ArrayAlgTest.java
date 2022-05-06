import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ArrayAlgTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void rotateForce() {
    }

    @Test
    void rotate() {
        int[] nums = new int[]{1,2,3,4,5,6};
        ArrayAlg.rotate(nums, 3);
        System.out.println(Arrays.toString(nums));
    }
}