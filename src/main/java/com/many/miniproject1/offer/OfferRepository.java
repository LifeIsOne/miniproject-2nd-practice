package com.many.miniproject1.offer;

import com.many.miniproject1.post.PostResponse;
import com.many.miniproject1.resume.Resume;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OfferRepository {
    private final EntityManager em;



    public List<Offer> findAll() {
        Query query = em.createNativeQuery("SELECT * FROM offer_tb", Offer.class);

        return query.getResultList();
    }

    public List<Resume> personFindAllOffer(int id) {
        String q = """
                SELECT rt.* 
                FROM offer_tb ot 
                INNER JOIN resume_tb rt 
                ON ot.resume_id = rt.id 
                WHERE ot.company_id = ?
                """;
        Query query = em.createNativeQuery(q, Resume.class);

        query.setParameter(1,id);

        return query.getResultList();
    }

    public List<OfferResponse.companyFindAllOfferDTO> companyFindAllOffer() {
        Query query = em.createNativeQuery("SELECT * " +
                "FROM offer_tb o\n" +
                "INNER JOIN resume_tb r\n" +
                "ON o.resume_id = r.id", OfferResponse.companyFindAllOfferDTO.class);
        try {
            Offer offer = (Offer) query.getSingleResult();
            return query.getResultList();
        }catch (Exception e){
            return null;
        }
    }

    public Offer findById(int id) {
        Query query = em.createNativeQuery("SELECT * FROM offer_tb WHERE id=?", Offer.class);
        query.setParameter(1, id);

        try {
            Offer offer = (Offer) query.getSingleResult();
            return offer;
        }catch (Exception e){
            return null;
        }

    }

    public List<OfferResponse.OfferBoardDTO> findCompanyOffersWithId(int oid) {
        String q = """
                SELECT ot.id, ut.company_name, ot.post_id, pt.title, ot.created_at , pt.profile, 
                FROM offer_tb ot 
                INNER JOIN user_tb ut ON ot.company_id = ut.id 
                INNER JOIN post_tb pt ON ot.post_id = pt.id 
                WHERE ot.person_id= ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, oid);

        List<Object[]> resultList = query.getResultList();

        List<OfferResponse.OfferBoardDTO> responseLIst = new ArrayList<>();
        for (Object[] row : resultList) {
            Integer id = (Integer) row[0];
            String companyName = (String) row[1];
            Integer postId = (Integer) row[2];
            String title = (String) row[3];
            Timestamp createdAt = (Timestamp) row[4];
            String profile = (String) row[5];

            OfferResponse.OfferBoardDTO responseDTO = new OfferResponse.OfferBoardDTO();

            responseDTO.setId(id);
            responseDTO.setCompanyName(companyName);
            responseDTO.setPostId(postId);
            responseDTO.setTitle(title);
            responseDTO.setCreatedAt(createdAt);
            responseDTO.setProfile(profile);

            responseLIst.add(responseDTO);
        }
        return responseLIst;
    }
//    public Offer List<Offer> fintAllSelect(int id) {
//        Query query = em.createNativeQuery("SELECT * FROM offer_tb WHERE id=?", Of);
//        query.setParameter(1, id);
//
//        Offer offer = (Offer) query.getSingleResult();
//
//        return offer;
//    }

    @Transactional
    public void save(OfferRequest.SaveDTO requestDTO, int id) {
        Query query = em.createNativeQuery("INSERT INTO offer_tb() VALUES ()");
        query.setParameter(1, id);

        query.executeUpdate();
    }

    @Transactional
    public void update(OfferRequest.UpdateDTO requestDTO, int id) {
        Query query = em.createNativeQuery("UPDATE offer_tb SET where id = ?");
        query.setParameter(1, id);

        query.executeUpdate();
    }

    @Transactional
    public void delete(int id) {
        Query query = em.createNativeQuery("DELETE FROM offer_tb WHERE id = ?");
        query.setParameter(1, id);

        query.executeUpdate();
    }
}
