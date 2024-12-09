package net.fullstack7.studyShare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import net.fullstack7.studyShare.domain.EmailCode;

// SELECT * FROM EmailCode 
// WHERE userId = ? AND code = ? 
// AND createdAt > DATE_SUB(NOW(), INTERVAL 1 HOUR)
// AND used = false
// ORDER BY createdAt DESC 
// LIMIT 1
public interface EmailCodeRepository extends JpaRepository<EmailCode, Long> {
    @Query(value = """
        SELECT * FROM EmailCode 
        WHERE userId = :userId 
        AND code = :code 
        AND createdAt > DATE_SUB(NOW(), INTERVAL 1 HOUR)
        AND used = false 
        ORDER BY createdAt DESC 
        LIMIT 1
    """, nativeQuery = true)    
    Optional<EmailCode> findValidCode(@Param("userId") String userId, @Param("code") String code);

    void deleteByCode(String code);
}
