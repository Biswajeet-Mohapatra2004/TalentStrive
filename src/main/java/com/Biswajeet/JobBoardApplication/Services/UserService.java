package com.Biswajeet.JobBoardApplication.Services;
import com.Biswajeet.JobBoardApplication.DTO.ApplicationDTO;
import com.Biswajeet.JobBoardApplication.DTO.JobPostDTO;
import com.Biswajeet.JobBoardApplication.Model.Application;
import com.Biswajeet.JobBoardApplication.Model.JobPostSchema;
import com.Biswajeet.JobBoardApplication.Model.Users;
import com.Biswajeet.JobBoardApplication.Repository.ApplicationRepository;
import com.Biswajeet.JobBoardApplication.Repository.JobPostSchemaRepository;
import com.Biswajeet.JobBoardApplication.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private JobPostSchemaRepository jobRepo;

    @Autowired
    private ApplicationRepository applicationRepository;

    private BCryptPasswordEncoder encoder= new BCryptPasswordEncoder(12);


    public void applyForJob(Application application,Users users) {
        applicationRepository.save(application);
        users.setApplications(application);
    }

    public void registerUser(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    public Optional<Users> findUserById(Long id) {
        return userRepo.findById(id);
    }

    public void updateUser(Users old_user,Users new_user) {
        old_user.setName(new_user.getName());
        old_user.setUsername(new_user.getUsername());
        old_user.setRole(new_user.getRole());
        userRepo.save(old_user);
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public void updatePassword(Long id, String password) {
        Optional<Users> user=userRepo.findById(id);
        if(user.isPresent()){
            Users details=user.get();
            details.setPassword(encoder.encode(password));
            userRepo.save(details);
        }
    }

    public List<Users> findAll() {
       return  userRepo.findAll();
    }

    public Users findUserByUserName(String username) {
        List<Users> list= userRepo.findAll();
        Users userDetails= new Users();
        for(Users user:list){
            if(user.getUsername().equals(username)){
                userDetails=user;
                break;
            }
        }
        return userDetails;


    }

    public Users findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public void deleteApplication(Long userId, ApplicationDTO applicationDTO) {
       Optional<Application> application=applicationRepository.findById(applicationDTO.getId());
       Optional<Users> user=userRepo.findById(userId);

       user.get().removeApplication(application.get());
       userRepo.save(user.get());
       applicationRepository.deleteById(applicationDTO.getId());


    }
}
