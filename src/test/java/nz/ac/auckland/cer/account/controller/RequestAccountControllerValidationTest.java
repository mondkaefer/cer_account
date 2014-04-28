package nz.ac.auckland.cer.account.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

import nz.ac.auckland.cer.project.dao.ProjectDatabaseDao;
import nz.ac.auckland.cer.project.pojo.Affiliation;
import nz.ac.auckland.cer.project.pojo.InstitutionalRole;
import nz.ac.auckland.cer.project.util.AffiliationUtil;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/RequestAccountControllerValidationTest-context.xml", "/root-context.xml"})
@WebAppConfiguration
public class RequestAccountControllerValidationTest {

	  @Autowired
	  private WebApplicationContext wac;
	  @Autowired
	  private ProjectDatabaseDao projectDatabaseDao;
	  @Autowired
	  private AffiliationUtil affiliationUtilMock;
	  
	  private MockMvc mockMvc;

	  @Before
	  public void setup() throws Exception {
	    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	  }

	  @Test
	  public void postAccountRequestSuccess() throws Exception {
		  when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
		  when(affiliationUtilMock.getInstitutionFromAffiliationString("Test Institution")).thenReturn("Test Institution");
		  this.mockMvc.perform(post("/request_account").param("fullName", "Jane Doe")
		    .param("institution", "Test Institution").param("institutionalRoleId", "42")
		    .param("email", "test@test.org").param("phone", "12345"))
            //.andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(view().name("request_account_success"))
	        .andExpect(model().attributeHasNoErrors("requestaccount"));
	  }

      @Test
      public void postAccountRequestMissingFullName() throws Exception {
          when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
          when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
          when(affiliationUtilMock.getInstitutionFromAffiliationString("Test Institution")).thenReturn("Test Institution");
          this.mockMvc.perform(post("/request_account").param("phone", "12345").param("email", "test@test.org")
            .param("institution", "Test Institution").param("institutionalRoleId", "42"))
            .andExpect(status().isOk())
            .andExpect(view().name("request_account"))
            //.andDo(print())
            .andExpect(model().attributeErrorCount("requestaccount", 1))
            .andExpect(model().attributeHasFieldErrors("requestaccount", "fullName"));
      }

	  @Test
	  public void postAccountRequestMissingEmail() throws Exception {
		  when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
          when(affiliationUtilMock.getInstitutionFromAffiliationString("Test Institution")).thenReturn("Test Institution");
	      this.mockMvc.perform(post("/request_account").param("fullName", "Jane Doe")
	        .param("institution", "Test Institution").param("institutionalRoleId", "42").param("phone", "12345"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("request_account"))
	        //.andDo(print())
	        .andExpect(model().attributeErrorCount("requestaccount", 1))
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "email"));
	  }

	  @Test
	  public void postAccountRequestMissingInstitution() throws Exception {
		  when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/request_account").param("fullName", "Jane Doe")
	        .param("institutionalRoleId", "42").param("email", "test@test.org").param("phone", "12345"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("request_account"))
	        .andExpect(model().attributeErrorCount("requestaccount", 1))
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "institution"));
	  }

	  @Test
	  public void postAccountRequestMissingInstitutionalRoleId() throws Exception {
		  when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
	      this.mockMvc.perform(post("/request_account").param("fullName", "Jane Doe")
	        .param("institution", "Test Institution").param("email", "test@test.org").param("phone", "12345"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("request_account"))
	        .andExpect(model().attributeErrorCount("requestaccount", 1))
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "institutionalRoleId"));
	  }

	  @Test
	  public void postAccountRequestInvalidEmail() throws Exception {
		  when(projectDatabaseDao.getInstitutionalRoles()).thenReturn(new LinkedList<InstitutionalRole>());
		  when(affiliationUtilMock.getAffiliationStrings((List<Affiliation>) anyObject())).thenReturn(new LinkedList<String>());
          when(affiliationUtilMock.getInstitutionFromAffiliationString("Test Institution")).thenReturn("Test Institution");
	      this.mockMvc.perform(post("/request_account").param("fullName", "Jane Doe")
	        .param("institution", "Test Institution")
	        .param("institutionalRoleId", "42").param("email", "test").param("phone", "12345"))
	        .andExpect(status().isOk())
	        .andExpect(view().name("request_account"))
	        .andExpect(model().attributeErrorCount("requestaccount", 1))
	        .andExpect(model().attributeHasFieldErrors("requestaccount", "email"));
	  }

}
