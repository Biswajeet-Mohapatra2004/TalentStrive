package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    void deleteByUserId(Long id);

    @Query("SELECT a FROM Application a WHERE a.user.id = :userid")
    List<Application> findApplicationByUserId(@Param("userid") Long userId);

    @Query("SELECT a FROM Application a WHERE a.jobPost.employer.id = :empId ")
    List<Application> findApplicationByEmployerId(@Param("empId") Long id);
}
