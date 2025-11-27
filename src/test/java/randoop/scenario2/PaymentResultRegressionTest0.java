package randoop.scenario2;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PaymentResultRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        Class<?> wildcardClass1 = status0.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        Class<?> wildcardClass3 = paymentResult2.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.PENDING;
        Class<?> wildcardClass1 = status0.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.PENDING + "'", status0.equals(scenario2.payment.PaymentResult.Status.PENDING));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        scenario2.payment.PaymentResult.Status status0 = null;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
        scenario2.payment.PaymentResult paymentResult4 = new scenario2.payment.PaymentResult(status0, "");
        Class<?> wildcardClass5 = paymentResult4.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        scenario2.payment.PaymentResult paymentResult5 = new scenario2.payment.PaymentResult(status3, "");
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status3.equals(scenario2.payment.PaymentResult.Status.FAILED));
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        scenario2.payment.PaymentResult.Status status4 = paymentResult2.getStatus();
        Class<?> wildcardClass5 = paymentResult2.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status3.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status4 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status4.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        Class<?> wildcardClass4 = status3.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status3.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.PENDING;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        Class<?> wildcardClass4 = status3.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.PENDING + "'", status0.equals(scenario2.payment.PaymentResult.Status.PENDING));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.PENDING + "'", status3.equals(scenario2.payment.PaymentResult.Status.PENDING));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
        scenario2.payment.PaymentResult paymentResult4 = new scenario2.payment.PaymentResult(status0, "");
        scenario2.payment.PaymentResult paymentResult6 = new scenario2.payment.PaymentResult(status0, "hi!");
        Class<?> wildcardClass7 = paymentResult6.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.PENDING;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        Class<?> wildcardClass3 = status0.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.PENDING + "'", status0.equals(scenario2.payment.PaymentResult.Status.PENDING));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
        Class<?> wildcardClass3 = paymentResult2.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        scenario2.payment.PaymentResult.Status status4 = paymentResult2.getStatus();
        scenario2.payment.PaymentResult.Status status5 = paymentResult2.getStatus();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status3.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status4 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status4.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status5 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status5.equals(scenario2.payment.PaymentResult.Status.FAILED));
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.FAILED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        scenario2.payment.PaymentResult.Status status3 = paymentResult2.getStatus();
        Class<?> wildcardClass4 = paymentResult2.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status0.equals(scenario2.payment.PaymentResult.Status.FAILED));
        org.junit.Assert.assertTrue("'" + status3 + "' != '" + scenario2.payment.PaymentResult.Status.FAILED + "'", status3.equals(scenario2.payment.PaymentResult.Status.FAILED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        scenario2.payment.PaymentResult.Status status0 = null;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "hi!");
        Class<?> wildcardClass3 = paymentResult2.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        scenario2.payment.PaymentResult.Status status0 = scenario2.payment.PaymentResult.Status.APPROVED;
        scenario2.payment.PaymentResult paymentResult2 = new scenario2.payment.PaymentResult(status0, "");
        Class<?> wildcardClass3 = status0.getClass();
        org.junit.Assert.assertTrue("'" + status0 + "' != '" + scenario2.payment.PaymentResult.Status.APPROVED + "'", status0.equals(scenario2.payment.PaymentResult.Status.APPROVED));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }
}

