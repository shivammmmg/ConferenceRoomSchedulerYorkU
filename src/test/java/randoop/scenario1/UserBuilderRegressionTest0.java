package randoop.scenario1;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserBuilderRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test01() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test01");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setName("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
    }

    @Test
    public void test02() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test02");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        Class<?> wildcardClass5 = userBuilder4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test03() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test03");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        Class<?> wildcardClass5 = userBuilder0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass5);
    }

    @Test
    public void test04() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test04");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        shared.model.UserType userType5 = null;
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setUserType(userType5);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
    }

    @Test
    public void test05() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test05");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        Class<?> wildcardClass1 = userBuilder0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }

    @Test
    public void test06() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test06");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        Class<?> wildcardClass3 = userBuilder0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass3);
    }

    @Test
    public void test07() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test07");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setStudentId("");
        Class<?> wildcardClass9 = userBuilder4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test08() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test08");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
    }

    @Test
    public void test09() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test09");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        shared.model.UserType userType7 = null;
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setUserType(userType7);
        shared.model.UserType userType9 = null;
        scenario1.builder.UserBuilder userBuilder10 = userBuilder8.setUserType(userType9);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
    }

    @Test
    public void test10() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test10");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setStudentId("");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder8.setOrgId("");
        scenario1.builder.UserBuilder userBuilder12 = userBuilder10.setPassword("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder12);
    }

    @Test
    public void test11() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test11");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder2.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setEmail("hi!");
        shared.model.UserType userType9 = null;
        scenario1.builder.UserBuilder userBuilder10 = userBuilder8.setUserType(userType9);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
    }

    @Test
    public void test12() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test12");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder2.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setEmail("hi!");
        Class<?> wildcardClass9 = userBuilder8.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test13() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test13");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setStudentId("");
        shared.model.UserType userType5 = null;
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setUserType(userType5);
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setEmail("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
    }

    @Test
    public void test14() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test14");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setEmail("");
        Class<?> wildcardClass9 = userBuilder8.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test15() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test15");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setName("hi!");
        Class<?> wildcardClass7 = userBuilder4.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test16() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test16");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setStudentId("");
        shared.model.UserType userType5 = null;
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setUserType(userType5);
        Class<?> wildcardClass7 = userBuilder6.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test17() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test17");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setEmail("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
    }

    @Test
    public void test18() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test18");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        shared.model.UserType userType7 = null;
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setUserType(userType7);
        scenario1.builder.UserBuilder userBuilder10 = userBuilder4.setStudentId("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
    }

    @Test
    public void test19() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test19");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setName("");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder6.setEmail("");
        Class<?> wildcardClass11 = userBuilder10.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass11);
    }

    @Test
    public void test20() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test20");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setPassword("");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder4.setPassword("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
    }

    @Test
    public void test21() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test21");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        shared.model.UserType userType3 = null;
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setUserType(userType3);
        scenario1.builder.UserBuilder userBuilder6 = userBuilder2.setEmail("");
        Class<?> wildcardClass7 = userBuilder2.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass7);
    }

    @Test
    public void test22() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test22");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setName("hi!");
        shared.model.UserType userType7 = null;
        scenario1.builder.UserBuilder userBuilder8 = userBuilder4.setUserType(userType7);
        Class<?> wildcardClass9 = userBuilder8.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test23() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test23");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        shared.model.UserType userType5 = null;
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setUserType(userType5);
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setName("");
        Class<?> wildcardClass9 = userBuilder8.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass9);
    }

    @Test
    public void test24() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test24");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setEmail("");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setOrgId("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
    }

    @Test
    public void test25() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test25");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        shared.model.UserType userType3 = null;
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setUserType(userType3);
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setStudentId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
    }

    @Test
    public void test26() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test26");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        shared.model.UserType userType1 = null;
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setUserType(userType1);
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setOrgId("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
    }

    @Test
    public void test27() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test27");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        shared.model.UserType userType3 = null;
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setUserType(userType3);
        scenario1.builder.UserBuilder userBuilder6 = userBuilder2.setEmail("");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setOrgId("hi!");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder6.setOrgId("hi!");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
    }

    @Test
    public void test28() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test28");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setEmail("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder0.setStudentId("");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder4.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setName("");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder6.setEmail("");
        shared.model.UserType userType11 = null;
        scenario1.builder.UserBuilder userBuilder12 = userBuilder10.setUserType(userType11);
        scenario1.builder.UserBuilder userBuilder14 = userBuilder12.setStudentId("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder12);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder14);
    }

    @Test
    public void test29() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test29");
        scenario1.builder.UserBuilder userBuilder0 = new scenario1.builder.UserBuilder();
        scenario1.builder.UserBuilder userBuilder2 = userBuilder0.setOrgId("");
        scenario1.builder.UserBuilder userBuilder4 = userBuilder2.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder6 = userBuilder2.setName("hi!");
        scenario1.builder.UserBuilder userBuilder8 = userBuilder6.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder10 = userBuilder8.setPassword("hi!");
        scenario1.builder.UserBuilder userBuilder12 = userBuilder10.setName("");
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder2);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder4);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder6);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder8);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder10);
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(userBuilder12);
    }

    @Test
    public void test30() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test30");
        Object obj0 = new Object();
        Class<?> wildcardClass1 = obj0.getClass();
        // Regression assertion (captures the current behavior of the code)
        org.junit.Assert.assertNotNull(wildcardClass1);
    }
}

