package com.zhigimont.bookstore.resource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@RestController
public class LoginResource {

    @RequestMapping("/token")
    public Map<String,String> token(HttpSession session, HttpServletRequest request){
        System.out.println(request.getRemoteHost());
        String remoteHost = request.getRemoteHost();
        int port = request.getRemotePort();
        System.out.println(remoteHost +": "+port);
        System.out.println(request.getRemoteAddr());
        return Collections.singletonMap("token",session.getId());
    }

    @RequestMapping("/checkSession")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> checkSession(){
        SecurityContext context = SecurityContextHolder.getContext();
        UserDetails principal =(UserDetails) context.getAuthentication().getPrincipal();
        System.out.println("user with name " + principal.getUsername() + " have session");
        return Collections.singletonMap("message","Session is active!");
    }

    @RequestMapping(value = "user/logout", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> logout(HttpServletRequest request,HttpServletResponse response){
        System.out.println("Logout Successfully");
        SecurityContextHolder.clearContext();
        return Collections.singletonMap("message","Logout Successfully!");
    }


}
