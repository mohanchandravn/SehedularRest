/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oracle.com.service;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import oracle.com.RuleConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author vaduri
 */
@Stateless
@Path("oracle.com.ruleconfiguration")
public class RuleConfigurationFacadeREST extends AbstractFacade<RuleConfiguration> {
private static final Logger LOGGER = Logger.getLogger(RuleConfigurationFacadeREST.class.getName()); 
    @PersistenceContext(unitName = "SchedulerPU")
    private EntityManager em;

    public RuleConfigurationFacadeREST() {
        super(RuleConfiguration.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(RuleConfiguration entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") String id, RuleConfiguration entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") String id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public RuleConfiguration find(@PathParam("id") String id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<RuleConfiguration> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<RuleConfiguration> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    @Path("/getRulesConfigInfoByJobId/{jobId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RuleConfiguration> getjobConfigByJobId(@PathParam("jobId") Integer jobId) {
        List<RuleConfiguration> result = null; 
        
         try {
            Query query = getEntityManager().createNamedQuery("RuleConfiguration.getRulesConfigInfoByJobId");
            query.setParameter("jobId_bind",jobId);
            result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @POST    
    @Path("/updateRuleConfigByJobId/{jobId}/{keysAndValues}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RuleConfiguration> updateRuleConfigByJobId(@PathParam("jobId") Integer jobId,@PathParam("keysAndValues") RuleConfiguration keysAndValues) {
        List<RuleConfiguration> result = null;        
        
         try {
              //getEntityManager().getTransaction().begin();
            
            if(keysAndValues != null){
                
                LOGGER.info(keysAndValues.getRuleKey());
                JSONArray jsonArray = new JSONArray(keysAndValues.getRuleKey());
                LOGGER.info("jsonArray.length()"+ jsonArray.length());
                                
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jobj = jsonArray.optJSONObject(i);
                jobj.get("ui_label");
                jobj.get("key_value");
                LOGGER.info("label "+jobj.get("ui_label")+ " value" + jobj.get("key_value"));
                
                Query query = getEntityManager().createNamedQuery("RuleConfiguration.updateRuleConfigByJobId");
                query.setParameter("jobId_bind",jobId); 
                query.setParameter("ui_label_bind", jobj.get("ui_label"));
                query.setParameter("rule_value_bind",jobj.get("key_value"));
                int updated  = query.executeUpdate();
                LOGGER.info("update result" + updated);
    
            }
            }
           // result = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
