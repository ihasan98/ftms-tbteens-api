package com.ftms.ftmsapi.modelTest;

import com.ftms.ftmsapi.model.Company;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompanyTest {

    @Test
    public void testCompanyEmail(){

        Company company = new Company();

        company.setEmail("julie@coolperson.org");

        assertEquals(company.getEmail(),"julie@coolperson.org" );
    }
    @Test
    public void testCompanyName(){

        Company company = new Company();

        company.setName("Julie");

        assertEquals(company.getName(),"Julie" );
    }
    @Test
    public void testCompanyLogo(){

        Company company = new Company();

        company.setLogo("Cool :)");

        assertEquals(company.getLogo(),"Cool :)" );
    }
    @Test
    public void testCompanyNumber(){

        Company company = new Company();

        company.setNumber("111-2222");

        assertEquals(company.getNumber(),"111-2222" );
    }
}

