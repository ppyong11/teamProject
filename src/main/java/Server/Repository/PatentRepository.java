package Server.Repository;

import Server.Entity.PatentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatentRepository extends JpaRepository<PatentEntity, Long> {
    //CPC 조회
    @Query(value = "SELECT * FROM patent WHERE cpc_code LIKE :category", nativeQuery = true)
    List<PatentEntity> findByCategory(@Param("category") String category);

    @Query(value = "SELECT * FROM patent WHERE " +
            "(cpc_code LIKE :category1 OR cpc_code LIKE :category2)", nativeQuery = true)
    List<PatentEntity> findBy2Categories(@Param("category1") String category1,
                                     @Param("category2") String category2);


    @Query(value = "SELECT * FROM patent WHERE " +
            "(cpc_code LIKE :category1 OR cpc_code LIKE :category2 OR cpc_code LIKE :category3)", nativeQuery = true)
    List<PatentEntity> findBy3Categories(@Param("category1") String category1,
                                     @Param("category2") String category2,
                                     @Param("category3") String category3);

    /*
    // 여러 카테고리의 교집합으로 CPC 데이터 조회
    @Query(value = "SELECT c.* FROM patent c WHERE c.cpcCode IN :categories GROUP BY c.category HAVING COUNT(DISTINCT c.cpcCode) = :categoryCount", nativeQuery = true)
    List<PatentDto> findByCategories(@Param("categories") List<String> categories, @Param("categoryCount") long categoryCount);*/
}
