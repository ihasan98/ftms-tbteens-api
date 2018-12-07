package com.ftms.ftmsapi.payloadTest;

import com.ftms.ftmsapi.payload.UserSummary;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserSummaryTest {

    @Test
    public void testGetEmail() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903", 22L);
        assertEquals(user.getEmail(), "eceyucer@gmail.com");
    }

    @Test
    public void testSetEmail() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setEmail("eceyucer@icloud.com");
        assertEquals(user.getEmail(), "eceyucer@icloud.com");
    }

    @Test
    public void testGetId() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        assertEquals(user.getId(), new Long(32));
    }

    @Test
    public void testSetId() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setId(22L);
        assertEquals(user.getId(), new Long(22));
    }

    @Test
    public void testGetFirstname() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        assertEquals(user.getFirstname(), "Ece");
    }

    @Test
    public void testSetFirstname() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setFirstname("Edger");
        assertEquals(user.getFirstname(), "Edger");
    }

    @Test
    public void testGetLastname() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        assertEquals(user.getLastname(), "Yucer");
    }

    @Test
    public void testSetLastname() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setLastname("Yooger");
        assertEquals(user.getLastname(), "Yooger");
    }

    @Test
    public void testGetRole() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        assertEquals(user.getRole(), "Worker");
    }

    @Test
    public void testSetRole() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setRole("Superintendent");
        assertEquals(user.getRole(), "Superintendent");
    }

    @Test
    public void testGetCompany() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        assertEquals(user.getCompany(), new Long(22));
    }

    @Test
    public void testSetCompany() {
        UserSummary user = new UserSummary(32L, "eceyucer@gmail.com", "Ece", "Yucer",
                "Worker", "311923812903",22L);
        user.setCompany(33L);
        assertEquals(user.getCompany(), new Long(33));
    }
}
