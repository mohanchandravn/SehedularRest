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
import javax.ws.rs.core.PathSegment;
import oracle.com.QuartzJobDetails;
import oracle.com.QuartzJobDetailsPK;

/**
 *
 * @author jyomohan
 */
@Stateless
@Path("oracle.com.quartzjobdetails")
public class QuartzJobDetailsFacadeREST extends AbstractFacade<QuartzJobDetails> {

    @PersistenceContext(unitName = "SchedulerPU")
    private EntityManager em;

    private QuartzJobDetailsPK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;schedName=schedNameValue;jobName=jobNameValue;jobGroup=jobGroupValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        oracle.com.QuartzJobDetailsPK key = new oracle.com.QuartzJobDetailsPK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> schedName = map.get("schedName");
        if (schedName != null && !schedName.isEmpty()) {
            key.setSchedName(schedName.get(0));
        }
        java.util.List<String> jobName = map.get("jobName");
        if (jobName != null && !jobName.isEmpty()) {
            key.setJobName(jobName.get(0));
        }
        java.util.List<String> jobGroup = map.get("jobGroup");
        if (jobGroup != null && !jobGroup.isEmpty()) {
            key.setJobGroup(jobGroup.get(0));
        }
        return key;
    }

    public QuartzJobDetailsFacadeREST() {
        super(QuartzJobDetails.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(QuartzJobDetails entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, QuartzJobDetails entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        oracle.com.QuartzJobDetailsPK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public QuartzJobDetails find(@PathParam("id") PathSegment id) {
        oracle.com.QuartzJobDetailsPK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuartzJobDetails> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    //@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public List<QuartzJobDetails> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
}
