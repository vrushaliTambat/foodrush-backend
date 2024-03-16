package com.vrush.controller;

import com.vrush.config.JwtProvider;
import com.vrush.model.Cart;
import com.vrush.model.USER_ROLE;
import com.vrush.model.User;
import com.vrush.repository.CartRepository;
import com.vrush.repository.UserRepository;
import com.vrush.request.LoginRequest;
import com.vrush.response.AuthResponse;
import com.vrush.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist=userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already used with another account");
        }
        User createdUser=new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        //saving in the database, it returns the saved entity
        User savedUser=userRepository.save(createdUser);

        Cart cart=new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //generating jwt with authentication with method created in JWTProvider class
        String jwt=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Register success");
        authResponse.setRole(savedUser.getRole());

    return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    //login method
    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req){
        String username=req.getEmail();
        String password=req.getPassword();

        Authentication authentication=authenticate(username,password);
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
        String jwt=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setMessage("Login success");
        authResponse.setRole(USER_ROLE.valueOf(role));


        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password) {

        //UserDetails interface is a interface that represents essential information about a user in a Spring Security context.
        UserDetails userDetails=customUserDetailsService.loadUserByUsername(username);
        if (userDetails==null){
            throw new BadCredentialsException("Invalid username...");
        }
        //if user is present then check if the password is matching in database it is correct or not
        //passwordEncoder is an interface in Spring Security it matches raw password(password) with encoded(userDetails.getPassword()
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password...");
        }
        //The UsernamePasswordAuthenticationToken is a class in Spring Security that implements the Authentication interface. It is commonly used to represent an authentication request when the principal is a username and the credentials are a password.
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
