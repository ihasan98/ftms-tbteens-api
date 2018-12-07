package com.ftms.ftmsapi.modelTest;
import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.model.Employee;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {
    @Test
    public void testFirstName(){
        Employee employee = new Employee();
        String Firstname = "Elias";
        employee.setFirstName(Firstname);
        assertEquals(employee.getFirstName(), "Elias");
    }

    @Test
    public void testLastName() {
        Employee employee = new Employee();
        String Lastname = "Williams";
        employee.setLastName(Lastname);
        assertEquals(employee.getLastName(), "Williams");
    }

    @Test
    public void testEmail() {
        Employee employee = new Employee();
        String email = "elias.William@google.com";
        employee.setEmail(email);
        assertEquals(employee.getEmail(), "elias.William@google.com");
    }

    @Test
    public void testPassword(){
        Employee employee = new Employee();
        String password = "bananas";
        employee.setPassword(password);
        assertEquals(employee.getPassword(), "bananas");
    }

    @Test
    public void testNumber(){
        Employee employee = new Employee();
        String number = "4169671111";
        employee.setNumber(number);
        assertEquals(employee.getNumber(), "4169671111");
    }

    @Test
    public void testRole(){
        Employee employee = new Employee();
        String role = "Supreme ScrumMaster";
        employee.setRole(role);
        assertEquals(employee.getRole(), "Supreme ScrumMaster");
    }
    @Test
    public void testCompany() {
        Employee employee = new Employee();
        Company company = new Company();
        employee.setCompany(company);
        assertEquals(employee.getCompany(), company);
    }

    @Test
    public void testActive(){
        Employee employee = new Employee();
        employee.setActive(true);
        assertTrue(employee.getActive());
        employee.setActive(false);
        assertFalse(employee.getActive());
    }

}
