package randoop.scenario2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaymentContextRegressionTest0 {

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
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        Class<?> wildcardClass2 = paymentContext1.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        Class<?> wildcardClass6 = paymentResult5.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 10L, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("hi!", 100.0d, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("hi!", 0.0d, "");
        Class<?> wildcardClass6 = paymentResult5.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentStrategy paymentStrategy8 = null;
        paymentContext1.setStrategy(paymentStrategy8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", 0.0d, "hi!");
        scenario2.payment.PaymentResult paymentResult15 = paymentContext1.executePayment("", (double) (byte) 10, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult15);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", (double) 100, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy12 = null;
        paymentContext1.setStrategy(paymentStrategy12);
        scenario2.payment.PaymentStrategy paymentStrategy14 = null;
        paymentContext1.setStrategy(paymentStrategy14);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) (-1.0f), "");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", (double) ' ', "hi!");
        Class<?> wildcardClass12 = paymentContext1.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass12);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", 0.0d, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy12 = null;
        paymentContext1.setStrategy(paymentStrategy12);
        scenario2.payment.PaymentResult paymentResult17 = paymentContext1.executePayment("hi!", 10.0d, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult17);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentStrategy paymentStrategy2 = null;
        paymentContext1.setStrategy(paymentStrategy2);
        scenario2.payment.PaymentResult paymentResult7 = paymentContext1.executePayment("", (double) (short) 10, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult7);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("", (double) (-1.0f), "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", 0.0d, "hi!");
        scenario2.payment.PaymentResult paymentResult15 = paymentContext1.executePayment("hi!", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult19 = paymentContext1.executePayment("hi!", (double) 100, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult15);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult19);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", 0.0d, "hi!");
        scenario2.payment.PaymentResult paymentResult15 = paymentContext1.executePayment("hi!", (double) (short) 100, "hi!");
        Class<?> wildcardClass16 = paymentResult15.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult15);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass16);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("hi!", (double) 0, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        scenario2.payment.PaymentStrategy paymentStrategy0 = null;
        scenario2.payment.PaymentContext paymentContext1 = new scenario2.payment.PaymentContext(paymentStrategy0);
        scenario2.payment.PaymentResult paymentResult5 = paymentContext1.executePayment("", (double) 0L, "hi!");
        scenario2.payment.PaymentStrategy paymentStrategy6 = null;
        paymentContext1.setStrategy(paymentStrategy6);
        scenario2.payment.PaymentResult paymentResult11 = paymentContext1.executePayment("hi!", (double) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult15 = paymentContext1.executePayment("", 1.0d, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult11);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult15);
    }
}

