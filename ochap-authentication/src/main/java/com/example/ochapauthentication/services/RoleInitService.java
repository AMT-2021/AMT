package com.example.ochapauthentication.services;

import com.example.ochapauthentication.entities.Role;
import com.example.ochapauthentication.entities.User;
import com.example.ochapauthentication.repository.RoleDAO;
import com.example.ochapauthentication.repository.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import static com.example.ochapauthentication.routes.AuthenticationController.generateSalt;
import static com.example.ochapauthentication.routes.AuthenticationController.hashPassword;

@Component
public class RoleInitService {

  final RoleDAO roleRepository;
  final UserDAO userRepository;
  final PlatformTransactionManager ptm;

  @Value("${admin.password}")
  private String adminPassword;

  @Autowired
  public RoleInitService(RoleDAO roleRepository, UserDAO userRepository,
      PlatformTransactionManager ptm) {
    this.roleRepository = roleRepository;
    this.userRepository = userRepository;
    this.ptm = ptm;
  }

  @PostConstruct
  public void init() {
    TransactionTemplate tmpl = new TransactionTemplate(ptm);
    tmpl.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {

        if (roleRepository.findByName("user") == null) {
          Role user = new Role();
          user.setName("user");
          roleRepository.save(user);
        }

        if (roleRepository.findByName("admin") == null) {
          Role admin = new Role();
          admin.setName("admin");
          roleRepository.save(admin);
        }

        if (userRepository.getUserByUsername("ochap").isEmpty()) {
          User admin = new User();
          admin.setUsername("ochap");

          byte[] salt = generateSalt();

          admin.setPasswordHash(hashPassword(adminPassword, salt));
          admin.setSalt(salt);
          admin.setRole(roleRepository.findByName("admin"));
          userRepository.save(admin);
        }
      }
    });
  }
}
