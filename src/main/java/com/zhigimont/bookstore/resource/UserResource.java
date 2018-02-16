package com.zhigimont.bookstore.resource;

import com.zhigimont.bookstore.config.SecurityConfig;
import com.zhigimont.bookstore.config.SecurityUtility;
import com.zhigimont.bookstore.domain.User;
import com.zhigimont.bookstore.domain.security.Role;
import com.zhigimont.bookstore.domain.security.UserRole;
import com.zhigimont.bookstore.service.UserService;
import com.zhigimont.bookstore.utility.MailConstructor;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jws.soap.SOAPBinding;
import javax.mail.SendFailedException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping(value = "/user")
public class UserResource {
    @Autowired
    private UserService userService ;
    @Autowired
    private MailConstructor mailConstructor;
    @Autowired
    private JavaMailSender mailSender;


    @RequestMapping(value = "/newUser", method = RequestMethod.POST)
    public ResponseEntity newUserPost( HttpServletRequest request, @RequestBody HashMap<String,String> mapper ) throws Exception {
        String username = mapper.get("username");
        String email = mapper.get("email");
        if(userService.findByUsername(username)!=null){
            return new ResponseEntity( "usernameExist", HttpStatus.BAD_REQUEST);
        }
        if(userService.findByEmail(email)!=null){
            return new ResponseEntity("emailExists", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        String password = SecurityUtility.randomPassword();
        String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
        user.setPassword(encryptedPassword);
        Role role = new Role();
        role.setRoleId(1);
        role.setName("ROLE_USER");
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(new UserRole(user, role));
        userService.createUser(user, userRoles);
        SimpleMailMessage mailMessage = mailConstructor.constructNewUserEmail(user, password);
        try{
            mailSender.send(mailMessage);
        }catch (MailException ex){
            ex.printStackTrace();
        }

        return new ResponseEntity(Collections.singletonMap("message","User Added Successfully"),HttpStatus.OK);
    }

    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)

    public ResponseEntity editProfileInfo(@RequestBody HashMap<String,String> mapper) throws Exception{
        long id = 0;
        if (mapper.get("id")!=null){
            id = Integer.valueOf( mapper.get("id"));
        }else {
            System.out.println("user id not found");
        }

        String username = mapper.get("username");
        String firstName = mapper.get("firstName");
        String lastName = mapper.get("lastName");
        String email = mapper.get("email");
        String newPassword = mapper.get("newPassword");
        String currentPassword = mapper.get("currentPassword");
        User currentUser = userService.findById(id);
        BCryptPasswordEncoder bCryptPasswordEncoder = SecurityUtility.passwordEncoder();
        if(currentUser == null){
            throw new Exception("User not found");
        }
        if(currentPassword == null && currentPassword.isEmpty() && currentPassword.equals("")){
            return new ResponseEntity("Empty current password", HttpStatus.BAD_REQUEST);
        }
        if (userService.findByEmail(email) == null){
            return new ResponseEntity("email not found!", HttpStatus.BAD_REQUEST);
        }
        if (userService.findByUsername(username)!= null){
            if (userService.findByUsername(username).getId() != currentUser.getId()){
                return new ResponseEntity("Username not found!", HttpStatus.BAD_REQUEST);
            }
        }else {
            return new ResponseEntity("Username not found!", HttpStatus.BAD_REQUEST);
        }


        SecurityConfig securityConfig = new SecurityConfig();
        String dbPassword = currentUser.getPassword();
        if(bCryptPasswordEncoder.matches(currentPassword, dbPassword)) {
            if(newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")){
                currentUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
            }
        }else {
            return new ResponseEntity("Incorrect current password!", HttpStatus.BAD_REQUEST);
        }


        currentUser.setUsername(username);
        currentUser.setLastName(lastName);
        currentUser.setFirstName(firstName);
        currentUser.setEmail(email);
        userService.save(currentUser);

        return new ResponseEntity(Collections.singletonMap("message","User update Successfully"),HttpStatus.OK);

    }

    @RequestMapping("getCurrentUser")
    public User getCurrentUser(Principal principal){
        User user = new User();
        if (principal != null){
            user = userService.findByUsername(principal.getName());
        }
        return user;
    }


}
