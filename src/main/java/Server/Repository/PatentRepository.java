package Server.Repository;

import Server.Entity.PatentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface PatentRepository extends JpaRepository<PatentEntity, Long> {
    //CPC 조회
    @Query("SELECT p FROM PatentEntity p WHERE p.InternationalpatentclassificationNumber LIKE CONCAT(:category, '%|%') OR " +
            "p.InternationalpatentclassificationNumber LIKE CONCAT('%|', :category, '%')") //A~|~ or ~|A~에만 걸리게
    List<PatentEntity> findByCategory(@Param("category") String category, Pageable pageable);
    

    @Query("SELECT p FROM PatentEntity p WHERE (p.InternationalpatentclassificationNumber LIKE CONCAT(:category1, '%|%') OR " +
            "p.InternationalpatentclassificationNumber LIKE CONCAT('%|', :category1, '%')) AND (p.InternationalpatentclassificationNumber LIKE " +
            "CONCAT(:category2, '%|%') OR p.InternationalpatentclassificationNumber LIKE CONCAT('%|', :category2, '%'))")
    List<PatentEntity> findBy2Categories(@Param("category1") String category1, @Param("category2") String category2, Pageable pageable);


    @Query("SELECT p FROM PatentEntity p WHERE (p.InternationalpatentclassificationNumber LIKE CONCAT(:category1, '%|%') OR " +
            "p.InternationalpatentclassificationNumber LIKE CONCAT('%|', :category1, '%')) AND (p.InternationalpatentclassificationNumber LIKE " +
            "CONCAT(:category2, '%|%') OR p.InternationalpatentclassificationNumber LIKE CONCAT('%|', :category2, '%')) AND " +
            "(p.InternationalpatentclassificationNumber LIKE CONCAT(:category3, '%|%') OR p.InternationalpatentclassificationNumber LIKE " +
            "CONCAT('%|', :category3, '%'))")
    List<PatentEntity> findBy3Categories(@Param("category1") String category1, @Param("category2") String category2, @Param("category3") String category3, Pageable pageable);


    //상세 데이터 조회
    @Query("SELECT p FROM PatentEntity p WHERE p.ApplicationNumber= :appNumber")
    List<PatentEntity> findByAppNumber(@Param("appNumber") String appNumber);
}
