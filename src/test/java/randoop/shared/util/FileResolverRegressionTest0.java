package randoop.shared.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileResolverRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        shared.util.FileResolver fileResolver0 = new shared.util.FileResolver();
        Class<?> wildcardClass1 = fileResolver0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        String str1 = shared.util.FileResolver.resolve("hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        Object obj0 = new Object();
        Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        String str1 = shared.util.FileResolver.resolve("");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/hi!"));
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        String str1 = shared.util.FileResolver.resolve("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU");
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertTrue("'" + str1 + "' != '" + "/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU" + "'", str1.equals("/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU/Users/meemmorshed/PycharmProjects/ConferenceRoomSchedulerYorkU"));
    }
}
