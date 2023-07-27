//package dev.umar.marriageApp;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.RestController;
//
//@CrossOrigin
//@RestController
//public class BaseController {
//    private final UserInfoRepository userInfoRepository;
//    private UserInfoService userInfoService;
//
//    @Autowired
//    public BaseController(UserInfoRepository userInfoRepository, UserInfoService userInfoService) {
//        this.userInfoRepository = userInfoRepository;
//        this.userInfoService = userInfoService;
//    }
//
////    @PostMapping("/upload")
////    public ResponseEntity<String> uploadFile(@RequestBody UploadRequestDTO request) {
////        try {
////
////            MultipartFile file = request.getFile();
////            // Retrieve file data
////            String fileName = file.getOriginalFilename();
////            String fileType = file.getContentType();
////            byte[] fileData = file.getBytes();
////
////            String email = request.getEmail();
////            String username = request.getUsername();
////            String gender = request.getGender();
////
////            // Create file model object
////            UserDetails userDetails = new UserDetails();
////            userDetails.setEmail(email);
////            userDetails.setUsername(username);
////            userDetails.setGender(gender);
////            userDetails.setFileName(fileName);
////            userDetails.setFileType(fileType);
////            userDetails.setFileData(fileData);
////
////            // Save file to MongoDB using repository or service
////            userInfoRepository.save(userDetails);
////
////            return ResponseEntity.ok().body("File uploaded successfully.");
////        } catch (Exception e) {
////            return ResponseEntity.ok().body("Error uploading file.");
////        }
////    }
//
//}
