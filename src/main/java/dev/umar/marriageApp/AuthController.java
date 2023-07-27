package dev.umar.marriageApp;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

import static dev.umar.marriageApp.JwtUtil.generateToken;

@CrossOrigin
@RestController
public class AuthController {
    private final UserRepository userRepository;
    private UserService userService;
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(@RequestBody User user, HttpSession session) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";

        // Check if the user already exists in the database
        User existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.OK).body("Email already exists");
        }

        if(!email.matches(regex)){
            return ResponseEntity.status(HttpStatus.OK).body("Enter valid email address");
        }

        session.setAttribute("email", user.getEmail());
        String em = (String) session.getAttribute("email");
//        String token = generateToken(email);
//        session.setAttribute("token", token);

//        userRepository.save(user);
//        return ResponseEntity.ok("Registration successful " + password);
        return userService.signup(username, email, password);
    }

    @PostMapping("/api/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        String email = user.getEmail();
        String password = user.getPassword();

        String token = generateToken(email);
//        LoginResponse response = new LoginResponse(token);

        session.setAttribute("email", user.getEmail());
        session.setAttribute("token", token);

//      return ResponseEntity.ok("Login successful");
        return userService.login(email, password, token);
    }


    @PostMapping("/api/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody User user){
        String email = user.getEmail();
        String otp = user.getOtp();

//        return ResponseEntity.status(HttpStatus.OK).body("Before userService otp:" + user.getEmail() + " " + user.getOtp());
        return userService.verifyOTP(email, otp);
    }

    @PostMapping("/api/resend")
    public ResponseEntity<String> resendOTP(@RequestBody User user){
        String email = user.getEmail();

        return userService.resendOTP(email);
    }


    @GetMapping("/api/getEmail")
    public ResponseEntity<String> getEmailId(HttpSession session) {
        String email = (String) session.getAttribute("email");

        return ResponseEntity.ok(email);
    }

    @GetMapping("/api/getToken")
    public ResponseEntity<String> getToken(HttpSession session) {
        String token = (String) session.getAttribute("token");

        return ResponseEntity.ok(token);
    }

    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpSession session){
        // Invalidate Session
        session.invalidate();

        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/api/forgot")
    public ResponseEntity<String> forgot(@RequestBody User user, HttpSession session){
        String email = user.getEmail();

        User userEmail = userRepository.findByEmail(email);

        if(userEmail == null)   return ResponseEntity.status(HttpStatus.CONFLICT).body("Email does not exists.");

        session.setAttribute("forgotEmail", email);

        return userService.resendOTP(email);
    }

    @GetMapping("/api/forgotgetemail")
    public ResponseEntity<String> forgotGetEmail(HttpSession session) {
        String email = (String) session.getAttribute("forgotEmail");

        if(email == null)   return ResponseEntity.status(HttpStatus.CONFLICT).body("No email found");

        return ResponseEntity.status(HttpStatus.OK).body(email);
    }

    @PostMapping("/api/forgotverify")
    public ResponseEntity<String> forgotSendOTP(@RequestBody User user){
        String email = user.getEmail();
        String otp = user.getOtp();

        return userService.verifyOTP(email, otp);
    }

    @PostMapping("/api/change")
    public ResponseEntity<String> changePassword(@RequestBody User user){
        String email = user.getEmail();
        String password = user.getPassword();

        return userService.changePassword(email, password);
    }

}
