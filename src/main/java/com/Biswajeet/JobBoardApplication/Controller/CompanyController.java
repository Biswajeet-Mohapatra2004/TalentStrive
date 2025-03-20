package com.Biswajeet.JobBoardApplication.Controller;

import com.Biswajeet.JobBoardApplication.Model.Company;
import com.Biswajeet.JobBoardApplication.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class CompanyController {

    @Autowired
    private CompanyService service;

    @PostMapping(path = "/company/register" , consumes = "application/json")
    public void register(@RequestBody Company company){
        service.registerCompany(company);
    }

    @GetMapping(path = "/company/viewAll", produces = "application/json")
    public List<Company> getAllCompanies(){
        return service.getAllCompanies();
    }
    @PostMapping(path = "/company/add-all", consumes = "application/json")
    public void addAll(@RequestBody List<Company> companies){
        service.saveAll(companies);
    }


}
