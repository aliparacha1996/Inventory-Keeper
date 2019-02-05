package SkipList.src;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class SkipListTests {

    private SkipList<Integer> list = new SkipList<>();

    @Test
    public void testInsert() {
        for(int j=0; j<1000; j++) {
            Random rand = new Random();
            ArrayList<Integer> arr = new ArrayList<>();
            list.clear();
            for (int i = 0; i < 500; i++) {
                int x = rand.nextInt(2000);
                if(!arr.contains(x)) {
                    arr.add(x);
                    assertFalse(list.search(x));
//                    System.out.println(j + " " + x);
                    assertTrue(list.insert(x));
                    assertFalse(list.insert(x));
                }
                else i--;
            }
            for(int i = 0; i < 500; i++) {
                assertTrue(list.search(arr.get(i)));
            }
            System.out.println();
        }

    }

    @Test
    public void testDelete() {
        list.clear();

        for(int j=0; j<1000; j++) {
            list.clear();
            Random rand = new Random();
            ArrayList<Integer> arr = new ArrayList<>();
            for (int i = 0; i < 500; i++) {
                int x = rand.nextInt(1000);
                if(!arr.contains(x)) {
                    arr.add(x);
//                    System.out.println(j + " " + x);
                   assertTrue(list.insert(x));
                   assertFalse(list.insert(x));
                }
                else i--;
            }
//            if(j==2) {
//                System.out.println("break");
//            }
//            System.out.println(arr);
            Random rand2 = new Random();
//            System.out.println(list.toString());
            for(int i = 0; i < 500; i++) {
                int x = rand2.nextInt(arr.size());
                int atX = arr.get(x);
                arr.remove(x);
                assertTrue(list.search(atX));
                assertTrue(list.delete(atX));
//                System.out.println(atX);
//                if(j==2 && i==2) {
//                    System.out.println("break");
//                }
                assertFalse(list.search(atX));
                assertFalse(list.delete(atX));
            }
//            System.out.println();
        }

    }

    @Test
    public void testDel() {
        for(int i = 0; i< 2000; i++) {
            list.clear();
            list.insert(1);
            list.insert(7);
            list.insert(2);
            list.insert(6);
            list.insert(3);
            if(i == 2)
                System.out.println();
            list.delete(1);
            assertFalse(list.search(1));
            list.delete(2);
            assertFalse(list.search(2));
        }
    }

}
