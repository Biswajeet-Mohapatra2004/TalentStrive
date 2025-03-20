package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    void deleteByUserId(Long id);
}
