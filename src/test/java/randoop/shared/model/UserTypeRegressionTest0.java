package randoop.shared.model;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserTypeRegressionTest0 {

    public static boolean debug = false;

    @Test
    public void test1() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test1");
        shared.model.UserType userType0 = shared.model.UserType.STAFF;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.STAFF + "'", userType0.equals(shared.model.UserType.STAFF));
    }

    @Test
    public void test2() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test2");
        shared.model.UserType userType0 = shared.model.UserType.PARTNER;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.PARTNER + "'", userType0.equals(shared.model.UserType.PARTNER));
    }

    @Test
    public void test3() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test3");
        shared.model.UserType userType0 = shared.model.UserType.STUDENT;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.STUDENT + "'", userType0.equals(shared.model.UserType.STUDENT));
    }

    @Test
    public void test4() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test4");
        shared.model.UserType userType0 = shared.model.UserType.CHIEF_EVENT_COORDINATOR;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.CHIEF_EVENT_COORDINATOR + "'", userType0.equals(shared.model.UserType.CHIEF_EVENT_COORDINATOR));
    }

    @Test
    public void test5() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test5");
        shared.model.UserType userType0 = shared.model.UserType.FACULTY;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.FACULTY + "'", userType0.equals(shared.model.UserType.FACULTY));
    }

    @Test
    public void test6() throws Throwable {
        if (debug)
            System.out.format("%n%s%n", "RegressionTest0.test6");
        shared.model.UserType userType0 = shared.model.UserType.ADMIN;
        org.junit.Assert.assertTrue("'" + userType0 + "' != '" + shared.model.UserType.ADMIN + "'", userType0.equals(shared.model.UserType.ADMIN));
    }
}

