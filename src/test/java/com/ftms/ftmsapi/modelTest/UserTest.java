package com.ftms.ftmsapi.modelTest;
import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.model.User;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserTest {
    @Test
    public void testFirstName(){
        User user = new User();
        String Firstname = "Elias";
        user.setFirstName(Firstname);
        assertEquals(user.getFirstName(), "Elias");
    }

    @Test
    public void testLastName() {
        User user = new User();
        String Lastname = "Williams";
        user.setLastName(Lastname);
        assertEquals(user.getLastName(), "Williams");
    }

    @Test
    public void testEmail() {
        User user = new User();
        String email = "elias.William@google.com";
        user.setEmail(email);
        assertEquals(user.getEmail(), "elias.William@google.com");
    }

    @Test
    public void testPassword(){
        User user = new User();
        String password = "bananas";
        user.setPassword(password);
        assertEquals(user.getPassword(), "bananas");
    }

    @Test
    public void testNumber(){
        User user = new User();
        String number = "4169671111";
        user.setNumber(number);
        assertEquals(user.getNumber(), "4169671111");
    }

    @Test
    public void testRole(){
        User user = new User();
        String role = "Supreme ScrumMaster";
        user.setRole(role);
        assertEquals(user.getRole(), "Supreme ScrumMaster");
    }
    @Test
    public void testCompany() {
        User user = new User();
        Company company = new Company();
        user.setCompany(company);
        assertEquals(user.getCompany(), company);
    }

    @Test
    public void testActive(){
        User user = new User();
        user.setActive(true);
        assertTrue(user.getActive());
        user.setActive(false);
        assertFalse(user.getActive());
    }

}
