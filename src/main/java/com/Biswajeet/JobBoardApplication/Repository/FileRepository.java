package com.Biswajeet.JobBoardApplication.Repository;
import com.Biswajeet.JobBoardApplication.Model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File,Long> {
    List<File> findByUserId(Long userId);
}
