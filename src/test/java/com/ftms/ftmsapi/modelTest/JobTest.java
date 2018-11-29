package com.ftms.ftmsapi.modelTest;

public class JobTest {

  @Test
  public void testJobTitle(){

      Job job = new Job();

      job.setJobTitle("Weld Part");

      assertEquals(job.getJobTitle(),"Weld Part" );
  }
  @Test
  public void testJobDescription(){

      Job job = new Job();

      job.setDescription("Must weld part please!");

      assertEquals(job.getDescription(),"Must weld part please!" );
  }
  @Test
  public void testJobCompanyTest(){

      Job job = new Job();

      job.setCompany("Google");

      assertEquals(job.getCompany(),"Google" );
  }
  @Test
  public void testJobSite(){

      Job job = new Job();

      job.setSiteName("Robart's");

      assertEquals(job.getSiteName(),"Robart's" );
  }
  
}
