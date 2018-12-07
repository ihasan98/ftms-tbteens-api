package com.ftms.ftmsapi.modelTest;

import com.ftms.ftmsapi.model.ClientUser;
import com.ftms.ftmsapi.model.Company;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientUserTest {

    @Test
    public void testFirstName(){
        ClientUser clientUser = new ClientUser();
        String Firstname = "Elias";
        clientUser.setFirstName(Firstname);
        assertEquals(clientUser.getFirstName(), "Elias");
    }

    @Test
    public void testLastName() {
        ClientUser clientUser = new ClientUser();
        String Lastname = "Williams";
        clientUser.setLastName(Lastname);
        assertEquals(clientUser.getLastName(), "Williams");
    }

    @Test
    public void testEmail() {
        ClientUser clientUser = new ClientUser();
        String email = "elias.William@google.com";
        clientUser.setEmail(email);
        assertEquals(clientUser.getEmail(), "elias.William@google.com");
    }

    @Test
    public void testPassword(){
        ClientUser clientUser = new ClientUser();
        String password = "bananas";
        clientUser.setPassword(password);
        assertEquals(clientUser.getPassword(), "bananas");
    }

    @Test
    public void testNumber(){
        ClientUser clientUser = new ClientUser();
        String number = "4169671111";
        clientUser.setNumber(number);
        assertEquals(clientUser.getNumber(), "4169671111");
    }

    @Test
    public void testRole(){
        ClientUser clientUser = new ClientUser();
        String role = "Supreme ScrumMaster";
        clientUser.setRole(role);
        assertEquals(clientUser.getRole(), "Supreme ScrumMaster");
    }
    @Test
    public void testCompany() {
        ClientUser clientUser = new ClientUser();
        Company company = new Company();
        clientUser.setCompany(company);
        assertEquals(clientUser.getCompany(), company);
    }

    @Test
    public void testActive(){
        ClientUser clientUser = new ClientUser();
        boolean activitation = true;
        boolean activitation2 = false;
        clientUser.setActive(activitation);
        assertTrue(clientUser.getActive());
        clientUser.setActive(activitation2);
        assertFalse(clientUser.getActive());
    }

}