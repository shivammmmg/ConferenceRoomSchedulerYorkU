package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookingRepositoryRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList3 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList3);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getBookingsForRoom("");
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList6 = bookingRepository0.getBookingsForRoom("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList6);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getBookingsForRoom("");
        Class<?> wildcardClass3 = bookingList2.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        Class<?> wildcardClass1 = bookingRepository0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList4 = bookingRepository0.getBookingsForRoom("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList4);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        Class<?> wildcardClass2 = bookingRepository0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getBookingsForRoom("");
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList5 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList5);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList1 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList1);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getBookingsForRoom("");
        java.util.List<shared.model.Booking> bookingList4 = bookingRepository0.getBookingsForRoom("hi!");
        shared.model.Booking booking6 = bookingRepository0.findById("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking6);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getAllBookings();
        java.util.List<shared.model.Booking> bookingList3 = bookingRepository0.getAllBookings();
        java.util.List<shared.model.Booking> bookingList4 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList3);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList4);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        shared.model.Booking booking4 = bookingRepository0.findById("");
        java.util.List<shared.model.Booking> bookingList5 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList5);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        shared.model.Booking booking2 = bookingRepository0.findById("hi!");
        Class<?> wildcardClass3 = bookingRepository0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        shared.model.Booking booking4 = bookingRepository0.findById("");
        java.util.List<shared.model.Booking> bookingList6 = bookingRepository0.getBookingsForRoom("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList6);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getAllBookings();
        shared.model.Booking booking4 = bookingRepository0.findById("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking4);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        shared.model.Booking booking2 = bookingRepository0.findById("hi!");
        java.util.List<shared.model.Booking> bookingList4 = bookingRepository0.getBookingsForRoom("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList4);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getBookingsForRoom("");
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        bookingRepository0.saveAll();
        shared.model.Booking booking7 = bookingRepository0.findById("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(booking7);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        shared.model.BookingRepository bookingRepository0 = shared.model.BookingRepository.getInstance();
        bookingRepository0.saveAll();
        java.util.List<shared.model.Booking> bookingList2 = bookingRepository0.getAllBookings();
        java.util.List<shared.model.Booking> bookingList4 = bookingRepository0.getBookingsForRoom("");
        java.util.List<shared.model.Booking> bookingList5 = bookingRepository0.getAllBookings();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingList5);
    }
}

