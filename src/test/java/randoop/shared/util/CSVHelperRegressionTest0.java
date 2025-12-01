package randoop.shared.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CSVHelperRegressionTest0 {

    public static boolean debug = false;



    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        java.util.ArrayList<shared.model.User> userList1 = null;
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveUsers("hi!", userList1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.ArrayList.iterator()\" because \"list\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        shared.model.Booking[] bookingArray2 = new shared.model.Booking[] {};
        java.util.ArrayList<shared.model.Booking> bookingList3 = new java.util.ArrayList<shared.model.Booking>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<shared.model.Booking>) bookingList3, bookingArray2);
        shared.util.CSVHelper.saveBookings("hi!", (java.util.List<shared.model.Booking>) bookingList3);
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveBookings("", (java.util.List<shared.model.Booking>) bookingList3);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingArray2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        shared.model.User[] userArray1 = new shared.model.User[] {};
        java.util.ArrayList<shared.model.User> userList2 = new java.util.ArrayList<shared.model.User>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<shared.model.User>) userList2, userArray1);
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveUsers("", userList2);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userArray1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        Object obj0 = new Object();
        Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        shared.model.Room[] roomArray1 = new shared.model.Room[] {};
        java.util.ArrayList<shared.model.Room> roomList2 = new java.util.ArrayList<shared.model.Room>();
        boolean boolean3 = java.util.Collections.addAll((java.util.Collection<shared.model.Room>) roomList2, roomArray1);
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveRooms("", (java.util.List<shared.model.Room>) roomList2);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(roomArray1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        shared.util.CSVHelper cSVHelper0 = new shared.util.CSVHelper();
        Class<?> wildcardClass1 = cSVHelper0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        java.util.ArrayList<shared.model.User> userList2 = shared.util.CSVHelper.loadUsers("hi!");
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveUsers("", userList2);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userList2);
    }



    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        java.util.List<shared.model.Room> roomList1 = null;
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveRooms("", roomList1);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        shared.model.Booking[] bookingArray3 = new shared.model.Booking[] {};
        java.util.ArrayList<shared.model.Booking> bookingList4 = new java.util.ArrayList<shared.model.Booking>();
        boolean boolean5 = java.util.Collections.addAll((java.util.Collection<shared.model.Booking>) bookingList4, bookingArray3);
        shared.util.CSVHelper.saveBookings("hi!", (java.util.List<shared.model.Booking>) bookingList4);
        shared.util.CSVHelper.saveBookings("hi!", (java.util.List<shared.model.Booking>) bookingList4);
        shared.util.CSVHelper.saveBookings("hi!", (java.util.List<shared.model.Booking>) bookingList4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingArray3);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean5 + "' != '" + false + "'", boolean5 == false);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        java.util.ArrayList<shared.model.Booking> bookingList1 = shared.util.CSVHelper.loadBookings("hi!");
        Class<?> wildcardClass2 = bookingList1.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        shared.model.Room[] roomArray2 = new shared.model.Room[] {};
        java.util.ArrayList<shared.model.Room> roomList3 = new java.util.ArrayList<shared.model.Room>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<shared.model.Room>) roomList3, roomArray2);
        shared.util.CSVHelper.saveRooms("hi!", (java.util.List<shared.model.Room>) roomList3);
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveRooms("", (java.util.List<shared.model.Room>) roomList3);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(roomArray2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        java.util.ArrayList<shared.model.User> userList1 = shared.util.CSVHelper.loadUsers("hi!");
        Class<?> wildcardClass2 = userList1.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userList1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        java.util.ArrayList<shared.model.Booking> bookingList2 = shared.util.CSVHelper.loadBookings("hi!");
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveBookings("", (java.util.List<shared.model.Booking>) bookingList2);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        java.util.List<shared.model.Room> roomList1 = null;
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveRooms("hi!", roomList1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.util.List.iterator()\" because \"list\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        shared.model.Room[] roomArray2 = new shared.model.Room[] {};
        java.util.ArrayList<shared.model.Room> roomList3 = new java.util.ArrayList<shared.model.Room>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<shared.model.Room>) roomList3, roomArray2);
        shared.util.CSVHelper.saveRooms("hi!", (java.util.List<shared.model.Room>) roomList3);
        shared.util.CSVHelper.saveRooms("hi!", (java.util.List<shared.model.Room>) roomList3);
        Class<?> wildcardClass7 = roomList3.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(roomArray2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        shared.model.User[] userArray2 = new shared.model.User[] {};
        java.util.ArrayList<shared.model.User> userList3 = new java.util.ArrayList<shared.model.User>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<shared.model.User>) userList3, userArray2);
        shared.util.CSVHelper.saveUsers("hi!", userList3);
        shared.util.CSVHelper.saveUsers("hi!", userList3);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userArray2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        java.util.ArrayList<shared.model.User> userList2 = shared.util.CSVHelper.loadUsers("hi!");
        shared.util.CSVHelper.saveUsers("hi!", userList2);
        Class<?> wildcardClass4 = userList2.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        shared.model.User[] userArray2 = new shared.model.User[] {};
        java.util.ArrayList<shared.model.User> userList3 = new java.util.ArrayList<shared.model.User>();
        boolean boolean4 = java.util.Collections.addAll((java.util.Collection<shared.model.User>) userList3, userArray2);
        shared.util.CSVHelper.saveUsers("hi!", userList3);
        // The following exception was thrown during execution in test generation
        try {
            shared.util.CSVHelper.saveUsers("", userList3);
            org.junit.Assert.fail("Expected exception of type java.io.FileNotFoundException; message:  (No such file or directory)");
        } catch (java.io.FileNotFoundException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userArray2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }
}

