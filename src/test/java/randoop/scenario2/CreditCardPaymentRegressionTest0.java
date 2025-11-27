package randoop.scenario2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CreditCardPaymentRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        Class<?> wildcardClass1 = creditCardPayment0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        Class<?> wildcardClass5 = paymentResult4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
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
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) 100.0f, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        scenario2.payment.PaymentResult paymentResult12 = creditCardPayment0.pay("hi!", (double) ' ', "hi!");
        Class<?> wildcardClass13 = creditCardPayment0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("hi!", 100.0d, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("hi!", 0.0d, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        Class<?> wildcardClass9 = creditCardPayment0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        scenario2.payment.PaymentResult paymentResult12 = creditCardPayment0.pay("", (double) 10L, "");
        Class<?> wildcardClass13 = paymentResult12.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass13);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (-1.0f), "");
        Class<?> wildcardClass5 = paymentResult4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (-1.0f), "");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) 100L, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("", (double) 1L, "");
        Class<?> wildcardClass9 = paymentResult8.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        scenario2.payment.PaymentResult paymentResult12 = creditCardPayment0.pay("", (double) 10.0f, "");
        scenario2.payment.PaymentResult paymentResult16 = creditCardPayment0.pay("", (double) (byte) 1, "hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult16);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) 10L, "hi!");
        Class<?> wildcardClass5 = paymentResult4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        scenario2.payment.PaymentResult paymentResult12 = creditCardPayment0.pay("hi!", (double) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult16 = creditCardPayment0.pay("", (double) 0L, "hi!");
        Class<?> wildcardClass17 = creditCardPayment0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult16);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass17);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) ' ', "");
        Class<?> wildcardClass5 = paymentResult4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        scenario2.payment.CreditCardPayment creditCardPayment0 = new scenario2.payment.CreditCardPayment();
        scenario2.payment.PaymentResult paymentResult4 = creditCardPayment0.pay("", (double) (short) 100, "hi!");
        scenario2.payment.PaymentResult paymentResult8 = creditCardPayment0.pay("hi!", (double) '#', "");
        scenario2.payment.PaymentResult paymentResult12 = creditCardPayment0.pay("hi!", (double) 1.0f, "hi!");
        scenario2.payment.PaymentResult paymentResult16 = creditCardPayment0.pay("hi!", (double) 100, "");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(paymentResult16);
    }
}

