package client.ai.target;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TargetListTest {
    @Test
    void test() {
        Random r = new Random();
        TargetList list = new TargetList(5);

        int biggestKey = 0;
        int biggestScore = 0;

        for (int i = 0; i < 10000; i++) {
            int value = r.nextInt();

            if (value > biggestScore) {
                biggestKey = i;
                biggestScore = value;
            }

            list.add(i, value);
        }

        assertEquals(biggestKey, list.pick());
    }
}
