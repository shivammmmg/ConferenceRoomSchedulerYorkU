package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SystemUserRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        java.lang.Object obj0 = new java.lang.Object();
        java.lang.Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        systemUser6.setName("");
        java.time.LocalDateTime localDateTime9 = systemUser6.getCreatedAt();
        boolean boolean10 = systemUser6.isYorkUEmail();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(localDateTime9);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean10 + "' != '" + false + "'", boolean10 == false);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        java.lang.String str7 = systemUser6.getOrgId();
        boolean boolean8 = systemUser6.isActive();
        systemUser6.activate();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!" + "'", str7.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + true + "'", boolean8 == true);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        systemUser6.setName("");
        systemUser6.setOrgId("");
        systemUser6.deactivate();
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        systemUser6.deactivate();
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        java.lang.String str7 = systemUser6.getOrgId();
        boolean boolean8 = systemUser6.isActive();
        java.lang.Class<?> wildcardClass9 = systemUser6.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!" + "'", str7.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + true + "'", boolean8 == true);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        systemUser6.setName("");
        systemUser6.setPasswordHash("");
        systemUser6.setName("");
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        java.lang.String str7 = systemUser6.getOrgId();
        boolean boolean8 = systemUser6.isActive();
        java.util.UUID uUID9 = systemUser6.getUserId();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!" + "'", str7.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + true + "'", boolean8 == true);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(uUID9);
        // Regression assertion (captures the current behavior of the code)
// flaky:         org.junit.Assert.assertEquals(uUID9.toString(), "e80ffc59-b9e8-491e-b8b8-5ef59d75ba63");
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        boolean boolean7 = systemUser6.validate();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        java.lang.String str7 = systemUser6.getOrgId();
        boolean boolean8 = systemUser6.isActive();
        boolean boolean9 = systemUser6.isActive();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!" + "'", str7.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + true + "'", boolean8 == true);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean9 + "' != '" + true + "'", boolean9 == true);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        systemUser6.setName("");
        java.time.LocalDateTime localDateTime9 = systemUser6.getCreatedAt();
        boolean boolean10 = systemUser6.validate();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(localDateTime9);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean10 + "' != '" + false + "'", boolean10 == false);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        shared.model.UserType userType3 = null;
        shared.model.SystemUser systemUser6 = new shared.model.SystemUser("", "", "hi!", userType3, "hi!", "");
        java.lang.String str7 = systemUser6.getOrgId();
        boolean boolean8 = systemUser6.isActive();
        systemUser6.setOrgId("");
        systemUser6.setName("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + str7 + "' != '" + "hi!" + "'", str7.equals("hi!"));
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean8 + "' != '" + true + "'", boolean8 == true);
    }
}
