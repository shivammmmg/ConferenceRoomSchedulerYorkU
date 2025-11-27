package randoop.scenario2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookingBuilderRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        Object obj0 = new Object();
        Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        // The following exception was thrown during execution in test generation
        try {
            shared.model.Booking booking1 = bookingBuilder0.build();
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalStateException; message: Booking requires: bookingId, roomId, userId, startTime, and endTime.");
        } catch (IllegalStateException e) {
        // Expected exception.
        }
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder0.setPaymentStatus("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        Class<?> wildcardClass1 = bookingBuilder0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder4.setRoomId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setDepositAmount((double) (-1L));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        java.time.LocalDateTime localDateTime9 = null;
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setStartTime(localDateTime9);
        // The following exception was thrown during execution in test generation
        try {
            shared.model.Booking booking11 = bookingBuilder8.build();
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalStateException; message: Booking requires: bookingId, roomId, userId, startTime, and endTime.");
        } catch (IllegalStateException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        java.time.LocalDateTime localDateTime9 = null;
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setStartTime(localDateTime9);
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder10.setUserId("");
        // The following exception was thrown during execution in test generation
        try {
            shared.model.Booking booking13 = bookingBuilder12.build();
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalStateException; message: Booking requires: bookingId, roomId, userId, startTime, and endTime.");
        } catch (IllegalStateException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        java.time.LocalDateTime localDateTime3 = null;
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder0.setStartTime(localDateTime3);
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setUserId("");
        // The following exception was thrown during execution in test generation
        try {
            shared.model.Booking booking7 = bookingBuilder6.build();
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalStateException; message: Booking requires: bookingId, roomId, userId, startTime, and endTime.");
        } catch (IllegalStateException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPurpose("hi!");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder6.setRoomId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder4.setEndTime(localDateTime7);
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder4.setStatus("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        java.time.LocalDateTime localDateTime9 = null;
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder6.setEndTime(localDateTime9);
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder14 = bookingBuilder12.setBookingId("hi!");
        scenario2.builder.BookingBuilder bookingBuilder16 = bookingBuilder12.setRoomId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder14);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder16);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setUserId("hi!");
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder10.setPaymentStatus("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setBookingId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        java.time.LocalDateTime localDateTime3 = null;
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder0.setStartTime(localDateTime3);
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setUserId("");
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setBookingId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        java.time.LocalDateTime localDateTime9 = null;
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setStartTime(localDateTime9);
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder10.setPurpose("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setUserId("hi!");
        // The following exception was thrown during execution in test generation
        try {
            shared.model.Booking booking11 = bookingBuilder8.build();
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalStateException; message: Booking requires: bookingId, roomId, userId, startTime, and endTime.");
        } catch (IllegalStateException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder2.setBookingId("hi!");
        Class<?> wildcardClass11 = bookingBuilder2.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setRoomId("hi!");
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setPurpose("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        java.time.LocalDateTime localDateTime3 = null;
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder0.setStartTime(localDateTime3);
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder0.setPurpose("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        java.time.LocalDateTime localDateTime3 = null;
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder0.setStartTime(localDateTime3);
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setStartTime(localDateTime5);
        Class<?> wildcardClass7 = bookingBuilder4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setUserId("hi!");
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder8.setRoomId("");
        java.time.LocalDateTime localDateTime13 = null;
        scenario2.builder.BookingBuilder bookingBuilder14 = bookingBuilder12.setEndTime(localDateTime13);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder14);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        Class<?> wildcardClass9 = bookingBuilder6.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder6.setPaymentStatus("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPurpose("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder4.setEndTime(localDateTime7);
        java.time.LocalDateTime localDateTime9 = null;
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder8.setStartTime(localDateTime9);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        java.time.LocalDateTime localDateTime5 = null;
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder2.setEndTime(localDateTime5);
        java.time.LocalDateTime localDateTime7 = null;
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder2.setStartTime(localDateTime7);
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder2.setBookingId("hi!");
        scenario2.builder.BookingBuilder bookingBuilder12 = bookingBuilder10.setUserId("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder12);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        scenario2.builder.BookingBuilder bookingBuilder0 = new scenario2.builder.BookingBuilder();
        scenario2.builder.BookingBuilder bookingBuilder2 = bookingBuilder0.setBookingId("");
        scenario2.builder.BookingBuilder bookingBuilder4 = bookingBuilder2.setPaymentStatus("hi!");
        scenario2.builder.BookingBuilder bookingBuilder6 = bookingBuilder4.setDepositAmount((double) (short) 0);
        scenario2.builder.BookingBuilder bookingBuilder8 = bookingBuilder6.setPaymentStatus("");
        scenario2.builder.BookingBuilder bookingBuilder10 = bookingBuilder6.setStatus("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(bookingBuilder10);
    }
}

