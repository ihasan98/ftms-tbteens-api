package com.ftms.ftmsapi.modelTest;

import com.ftms.ftmsapi.model.Company;
import com.ftms.ftmsapi.controller.JobController;
import com.ftms.ftmsapi.model.Job;
import org.junit.Test;

import javax.persistence.*;

import static org.junit.Assert.assertEquals;

@Entity
@Table(name="jobs")
public class JobTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

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

      Company company = new Company();

      Job job = new Job();

      job.setCompany(company);

      assertEquals(job.getCompany(),company );
  }
  @Test
  public void testJobSite(){

      Job job = new Job();

      job.setSiteName("Robart's");

      assertEquals(job.getSiteName(),"Robart's" );
  }
  
}
