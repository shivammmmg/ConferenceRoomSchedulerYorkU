package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdminRepositoryRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        boolean boolean2 = adminRepository0.adminExists("hi!");
        Class<?> wildcardClass3 = adminRepository0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        boolean boolean4 = adminRepository0.adminExists("hi!");
        adminRepository0.deleteAdmin("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean4 + "' != '" + false + "'", boolean4 == false);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("");
        java.util.Map<String, shared.model.Admin> strMap5 = adminRepository0.getAllAdmins();
        Class<?> wildcardClass6 = strMap5.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass6);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin3 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        boolean boolean2 = adminRepository0.adminExists("hi!");
        java.util.Map<String, shared.model.Admin> strMap3 = adminRepository0.getAllAdmins();
        Class<?> wildcardClass4 = strMap3.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap3);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass4);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin1 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin1);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        java.util.Map<String, shared.model.Admin> strMap1 = adminRepository0.getAllAdmins();
        boolean boolean3 = adminRepository0.adminExists("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean3 + "' != '" + false + "'", boolean3 == false);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        java.util.Map<String, shared.model.Admin> strMap1 = adminRepository0.getAllAdmins();
        Class<?> wildcardClass2 = adminRepository0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap1);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass2);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        java.util.Map<String, shared.model.Admin> strMap1 = adminRepository0.getAllAdmins();
        shared.model.Admin admin2 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin2);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap1);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("hi!");
        shared.model.Admin admin5 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin5);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("");
        java.util.Map<String, shared.model.Admin> strMap5 = adminRepository0.getAllAdmins();
        shared.model.Admin admin7 = adminRepository0.getAdmin("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin7);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("hi!");
        adminRepository0.deleteAdmin("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("");
        java.util.Map<String, shared.model.Admin> strMap5 = adminRepository0.getAllAdmins();
        boolean boolean7 = adminRepository0.adminExists("");
        adminRepository0.deleteAdmin("");
        adminRepository0.deleteAdmin("hi!");
        java.util.Map<String, shared.model.Admin> strMap12 = adminRepository0.getAllAdmins();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap12);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        boolean boolean2 = adminRepository0.adminExists("hi!");
        shared.model.Admin admin3 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin3);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        boolean boolean2 = adminRepository0.adminExists("hi!");
        adminRepository0.deleteAdmin("");
        shared.model.Admin admin5 = null;
        // The following exception was thrown during execution in test generation
        try {
            adminRepository0.addAdmin(admin5);
            org.junit.Assert.fail("Expected exception of type java.lang.NullPointerException; message: Cannot invoke \"shared.model.Admin.getUsername()\" because \"admin\" is null");
        } catch (NullPointerException e) {
        // Expected exception.
        }
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        shared.model.Admin admin2 = adminRepository0.getAdmin("");
        shared.model.Admin admin4 = adminRepository0.getAdmin("");
        java.util.Map<String, shared.model.Admin> strMap5 = adminRepository0.getAllAdmins();
        boolean boolean7 = adminRepository0.adminExists("");
        adminRepository0.deleteAdmin("");
        boolean boolean11 = adminRepository0.adminExists("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(strMap5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean7 + "' != '" + false + "'", boolean7 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean11 + "' != '" + false + "'", boolean11 == false);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        shared.model.AdminRepository adminRepository0 = shared.model.AdminRepository.getInstance();
        boolean boolean2 = adminRepository0.adminExists("hi!");
        shared.model.Admin admin4 = adminRepository0.getAdmin("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(adminRepository0);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertTrue("'" + boolean2 + "' != '" + false + "'", boolean2 == false);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNull(admin4);
    }
}

