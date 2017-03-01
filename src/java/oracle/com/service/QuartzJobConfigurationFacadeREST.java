/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import oracle.com.QuartzJobConfiguration;
import oracle.quartz.QuartzAPI;
import java.util.logging.Logger;
import javax.persistence.Query;
/**
 *
 * @author vaduri
 */
@Stateless
@Path("oracle.com.quartzjobconfiguration")
public class QuartzJobConfigurationFacadeREST extends AbstractFacade<QuartzJobConfiguration> {
private static final Logger LOGGER = Logger.getLogger(QuartzJobConfigurationFacadeREST.class.getName()); 
    @PersistenceContext(unitName = "SchedulerPU")
    private EntityManager em;

    public QuartzJobConfigurationFacadeREST() {
        super(QuartzJobConfiguration.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(QuartzJobConfiguration entity) {
        super.create(entity);
        LOGGER.info("Entity value :"+entity);
        QuartzAPI apiObj=new QuartzAPI();
        LOGGER.info("apiObj value :"+apiObj);
        if(entity.getJobStatus()!=null&&"Running".equalsIgnoreCase(entity.getJobStatus().toString()))
        { 
        apiObj.saveAndRunJob(entity);
    }
    }

    @PUT
    @Path("updateRow/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, QuartzJobConfiguration entity) {
        super.edit(entity);
        QuartzAPI apiObj=new QuartzAPI();
        if(entity.getJobStatus()!=null&&"stop".equalsIgnoreCase(entity.getJobStatus().toString()))
        { 
            if(entity.getJobName()!=null)
            {
               apiObj.stopJobNow(entity.getJobName()); //stop
    }
        } 
        else if(entity.getJobStatus()!=null&&"running".equalsIgnoreCase(entity.getJobStatus().toString()))
        { 
            apiObj.saveAndRunJob(entity);
        } 

    }

    @DELETE
    @Path("deleteConfigData/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
     @Produces(MediaType.APPLICATION_JSON)
    public void remove(@PathParam("id") Integer id) {
        QuartzJobConfiguration entityRow=this.find(id);
        QuartzAPI apiObj=new QuartzAPI();
        LOGGER.info("JobName value :"+entityRow+"::"+entityRow.getJobName() );
        if(entityRow!=null&&entityRow.getJobName()!=null)
        {
           LOGGER.info("JobName value :"+entityRow.getJobName());
            apiObj.deleteJob(entityRow.getJobName());
        }
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public QuartzJobConfiguration find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<QuartzJobConfiguration> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<QuartzJobConfiguration> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @GET
    @Path("/getjobconfigbyjobname/{jobName}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuartzJobConfiguration> getjobConfigByJobname(@PathParam("jobName") String jobName) {
        List<QuartzJobConfiguration> result = null; 
        try {
            Query query = getEntityManager().createNamedQuery("QuartzJobConfiguration.getjobconfigbyjobname");
            query.setParameter("jobname_bind",jobName.toUpperCase()+'%');
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
}
        return result;
    }
    
    @GET
    @Path("/getjobconfigbyjobstatus/{jobStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuartzJobConfiguration> getjobConfigByJobstatus(@PathParam("jobStatus") String jobStatus) {
        List<QuartzJobConfiguration> result = null; 
        try {
            Query query = getEntityManager().createNamedQuery("QuartzJobConfiguration.getjobconfigbyjobstatus");
            query.setParameter("jobstatus_bind",jobStatus.toUpperCase()+'%');
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("/getjobconfigbyname&status/{jobName}/{jobStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuartzJobConfiguration> getjobConfigByNameStatus(@PathParam("jobName") String jobName,@PathParam("jobStatus") String jobStatus) {
        List<QuartzJobConfiguration> result = null; 
        try {
            Query query = getEntityManager().createNamedQuery("QuartzJobConfiguration.getjobconfigbyname&status");
            query.setParameter("jobname_bind",jobName.toUpperCase()+'%');
            query.setParameter("jobstatus_bind",jobStatus.toUpperCase()+'%');
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("/runJobNow/{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void runJobNow(@PathParam("jobId") Integer jobId) {
        LOGGER.info("Job Id val :"+jobId);  
       QuartzAPI apiObj=new QuartzAPI(); 
       QuartzJobConfiguration entity=(QuartzJobConfiguration)this.find(jobId);
       if(entity!=null)
       {
         LOGGER.info("Job running immediately :"+entity.getJobName());  
        apiObj.runJobNow(entity.getJobName(),entity.getClassName());//run job now
       }
    }
}
