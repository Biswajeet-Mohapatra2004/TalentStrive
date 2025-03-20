package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobPostSchemaRepository extends JpaRepository<JobPostSchema,Long> {


}
