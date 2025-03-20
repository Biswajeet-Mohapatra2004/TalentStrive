package com.Biswajeet.JobBoardApplication.Repository;

import com.Biswajeet.JobBoardApplication.Model.AssessmentReport;
import com.Biswajeet.JobBoardApplication.Model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentReportRepository extends JpaRepository<AssessmentReport,Long> {

}
