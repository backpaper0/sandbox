package memo;

import javax.enterprise.context.RequestScoped;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/*
 * @PersistenceContextでJNDI名を登録できるっぽい。
 * java:comp/env/MemoEMで参照できる。
 * このJNDI名ってどこまで有効なんだろう……
 */
@PersistenceContext(name = "MemoEM")
@RequestScoped
@Transactional
@Path("{id}")
public class MemoResource {

    /**
     * メモを参照する。
     * 
     * @param id
     * @return
     */
    @GET
    public String get(@PathParam("id") int id) {
        EntityManager em = lookupEntityManager();
        Memo memo = em.find(Memo.class, id);
        if (memo == null) {
            throw new NotFoundException();
        }
        return memo.getContent();
    }

    /**
     * メモを登録する。
     * 
     * @param id
     * @param content
     */
    @POST
    public void post(@PathParam("id") int id, String content) {
        EntityManager em = lookupEntityManager();
        Memo memo = em.find(Memo.class, id);
        if (memo != null) {
            memo.setContent(content);
        } else {
            memo = new Memo();
            memo.setId(id);
            memo.setContent(content);
            em.persist(memo);
        }
    }

    private EntityManager lookupEntityManager() {
        try {
            return InitialContext.doLookup("java:comp/env/MemoEM");
        } catch (NamingException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
