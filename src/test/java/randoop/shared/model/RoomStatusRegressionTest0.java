package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RoomStatusRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test1() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test1");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.RESERVED;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.RESERVED + "'", roomStatus0.equals(shared.model.RoomStatus.RESERVED));
    }

    @Test
    public void test2() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test2");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.DISABLED;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.DISABLED + "'", roomStatus0.equals(shared.model.RoomStatus.DISABLED));
    }

    @Test
    public void test3() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test3");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.IN_USE;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.IN_USE + "'", roomStatus0.equals(shared.model.RoomStatus.IN_USE));
    }

    @Test
    public void test4() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test4");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.UNKNOWN;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.UNKNOWN + "'", roomStatus0.equals(shared.model.RoomStatus.UNKNOWN));
    }

    @Test
    public void test5() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test5");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.NO_SHOW;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.NO_SHOW + "'", roomStatus0.equals(shared.model.RoomStatus.NO_SHOW));
    }

    @Test
    public void test6() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test6");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.MAINTENANCE;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.MAINTENANCE + "'", roomStatus0.equals(shared.model.RoomStatus.MAINTENANCE));
    }

    @Test
    public void test7() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test7");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.AVAILABLE;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.AVAILABLE + "'", roomStatus0.equals(shared.model.RoomStatus.AVAILABLE));
    }

    @Test
    public void test8() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test8");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.OCCUPIED;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.OCCUPIED + "'", roomStatus0.equals(shared.model.RoomStatus.OCCUPIED));
    }

    @Test
    public void test9() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test9");
        shared.model.RoomStatus roomStatus0 = shared.model.RoomStatus.ENABLED;
        org.junit.Assert.assertTrue("'" + roomStatus0 + "' != '" + shared.model.RoomStatus.ENABLED + "'", roomStatus0.equals(shared.model.RoomStatus.ENABLED));
    }
}

