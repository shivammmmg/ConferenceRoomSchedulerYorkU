package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BookingRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        double double12 = booking9.getDepositAmount();
        java.time.LocalDateTime localDateTime13 = null;
        // The following exception was thrown during execution in test generation
        try {
            booking9.extendTo(localDateTime13);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: New end time cannot be null");
        } catch (IllegalArgumentException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double12 + "' != '" + 1.0d + "'", double12 == 1.0d);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        // The following exception was thrown during execution in test generation
        try {
            String str12 = booking9.toString();
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"java.time.LocalDateTime.format(java.time.format.DateTimeFormatter)\" because \"this.startTime\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        Object obj0 = new Object();
        Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        java.time.LocalDateTime localDateTime12 = booking9.getEndTime();
        java.time.LocalDateTime localDateTime13 = null;
        booking9.setStartTime(localDateTime13);
        java.time.LocalDateTime localDateTime15 = null;
        // The following exception was thrown during execution in test generation
        try {
            booking9.extendTo(localDateTime15);
            org.junit.Assert.fail("Expected exception of type java.lang.IllegalArgumentException; message: New end time cannot be null");
        } catch (IllegalArgumentException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime12);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "hi!", (double) 1.0f);
        double double10 = booking9.getDepositAmount();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double10 + "' != '" + 1.0d + "'", double10 == 1.0d);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        booking9.setStatus("");
        java.time.LocalDateTime localDateTime14 = booking9.getStartTime();
        booking9.setPurpose("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime14);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        double double12 = booking9.getDepositAmount();
        java.time.LocalDateTime localDateTime13 = booking9.getStartTime();
        String str14 = booking9.getStatus();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double12 + "' != '" + 1.0d + "'", double12 == 1.0d);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime13);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str14 + "' != '" + "" + "'", str14.equals(""));
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        booking9.setStatus("");
        java.time.LocalDateTime localDateTime14 = booking9.getStartTime();
        java.time.LocalDateTime localDateTime15 = booking9.getEndTime();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime14);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime15);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("", "", "", localDateTime3, localDateTime4, "", "", "", (double) 100);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        double double12 = booking9.getDepositAmount();
        java.time.LocalDateTime localDateTime13 = booking9.getStartTime();
        String str14 = booking9.getBookingId();
        booking9.setRoomId("hi!");
        java.time.LocalDateTime localDateTime17 = booking9.getStartTime();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double12 + "' != '" + 1.0d + "'", double12 == 1.0d);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime13);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str14 + "' != '" + "hi!" + "'", str14.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime17);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        double double12 = booking9.getDepositAmount();
        java.time.LocalDateTime localDateTime13 = booking9.getStartTime();
        String str14 = booking9.getRoomId();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double12 + "' != '" + 1.0d + "'", double12 == 1.0d);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime13);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str14 + "' != '" + "" + "'", str14.equals(""));
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "hi!", (double) 1.0f);
        booking9.setPurpose("");
        String str12 = booking9.getPurpose();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str12 + "' != '" + "" + "'", str12.equals(""));
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        String str12 = booking9.getPurpose();
        booking9.setPaymentStatus("");
        java.time.LocalDateTime localDateTime15 = null;
        booking9.setEndTime(localDateTime15);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str12 + "' != '" + "" + "'", str12.equals(""));
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        double double12 = booking9.getDepositAmount();
        java.time.LocalDateTime localDateTime13 = booking9.getStartTime();
        String str14 = booking9.getBookingId();
        booking9.setRoomId("hi!");
        String str17 = booking9.getPurpose();
        booking9.setDepositAmount((double) (byte) 10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + double12 + "' != '" + 1.0d + "'", double12 == 1.0d);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime13);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str14 + "' != '" + "hi!" + "'", str14.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str17 + "' != '" + "" + "'", str17.equals(""));
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        java.time.LocalDateTime localDateTime3 = null;
        java.time.LocalDateTime localDateTime4 = null;
        shared.model.Booking booking9 = new shared.model.Booking("hi!", "", "hi!", localDateTime3, localDateTime4, "", "", "", 1.0d);
        booking9.setPurpose("");
        java.time.LocalDateTime localDateTime12 = booking9.getEndTime();
        java.time.LocalDateTime localDateTime13 = null;
        booking9.setStartTime(localDateTime13);
        java.time.LocalDateTime localDateTime15 = null;
        booking9.setStartTime(localDateTime15);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(localDateTime12);
    }
}

