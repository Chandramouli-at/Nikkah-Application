package dev.umar.marriageApp;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserService {
    private UserRepository userRepository;
    private JavaMailSender mailSender;
    private final MongoTemplate mongoTemplate;
//    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, JavaMailSender mailSender, MongoTemplate mongoTemplate) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.mongoTemplate = mongoTemplate;

    }

    public ResponseEntity<String> signup(String username, String email, String password) {
//        String hashedPassword = passwordEncoder.encode(password);

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        String otp = generateOTP();

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setOtp(otp);
        user.setCurrentTime(currentTimeInSeconds);

        try {
            userRepository.save(user);
            try{
                sendOTPEmail(email, otp);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Sending OTP Failed");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Storing Details Failed");
        }

        return ResponseEntity.ok("Registration successful");
    }

    private void sendOTPEmail(String email, String otp) {
        // Create and configure the email message
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("OTP Verification");
        message.setText("Your OTP is " + otp);

        // send mail
        mailSender.send(message);
    }

    private String generateOTP() {
        // Generate a random OTP using your preferred logic
        // For simplicity, let's assume a 6-digit OTP
        return String.format("%06d",new Random().nextInt(999999));
    }

    public ResponseEntity<String> login(String email, String password, String token) {
        User user = userRepository.findByEmail(email);

        boolean verify = verifyPassword(password, user.getPassword());

        if(!verify) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

//        if (user == null || !user.getPassword().equals(password)) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
        return ResponseEntity.status(HttpStatus.OK).body("Login successful " + token);
    }

    public boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public ResponseEntity<String> verifyOTP(String email, String otp){
        User user = userRepository.findByEmail(email);
        if(user == null)    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email does not exists");

        long currentTimeInSeconds = System.currentTimeMillis() / 1000;

        String userOTP = user.getOtp();
        long storedTime = user.getCurrentTime();

        if(currentTimeInSeconds - storedTime > 60) return ResponseEntity.status(HttpStatus.CONFLICT).body("OTP Incorrect");

        if(userOTP.equals(otp)) return ResponseEntity.status(HttpStatus.OK).body("OTP Verified");

        return ResponseEntity.status(HttpStatus.CONFLICT).body("OTP Not Verified");
    }

    public ResponseEntity<String> resendOTP(String email){
        User user = userRepository.findByEmail(email);

        String otp = generateOTP();
        long currentTimeInSeconds = System.currentTimeMillis() / 1000;
        user.setOtp(otp);
        user.setCurrentTime(currentTimeInSeconds);

        try{
            userRepository.save(user);
            try{
                sendOTPEmail(email, otp);
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Sending OTP Failed");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Storing Details Failed");
        }

        return ResponseEntity.status(HttpStatus.OK).body("OTP Sent");
    }

    public ResponseEntity<String> changePassword(String email, String password){
        User user = userRepository.findByEmail(email);

        if(user == null) return ResponseEntity.status(HttpStatus.CONFLICT).body("Email Not Found");

        user.setPassword(password);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body("Password changed successfully");
    }

}
