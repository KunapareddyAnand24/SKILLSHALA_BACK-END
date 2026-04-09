package com.placement.controller;

import com.placement.dto.UserDTO;
import com.placement.service.FileStorageService;
import com.placement.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "https://skill-shala.netlify.app"})
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'EMPLOYER', 'STUDENT')")
    public List<UserDTO> getAllUsers(@RequestParam(required = false) String status) {
        if (status != null && !status.isEmpty()) {
            return userService.getAllUsers().stream()
                    .filter(u -> status.equalsIgnoreCase(u.getStatus()))
                    .collect(java.util.stream.Collectors.toList());
        }
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OFFICER', 'ADMIN', 'STUDENT', 'EMPLOYER')")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT', 'EMPLOYER')")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDetails) {
        return userService.updateUser(id, userDetails);
    }

    /**
     * Upload profile image/resume for Student.
     */
    @PostMapping("/{id}/upload")
    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    public Map<String, String> uploadFile(@PathVariable Long id, 
                                          @RequestParam("file") MultipartFile file, 
                                          @RequestParam("type") String type) {
        
        String subDir = type.equals("avatar") ? "avatars" : "resumes";
        String fileName = fileStorageService.storeFile(file, subDir);
        
        UserDTO user = userService.getUserById(id);
        if (type.equals("avatar")) {
            user.setAvatar(fileName);
        } else {
            user.setResumeUrl(fileName);
        }
        userService.updateUser(id, user);
        
        return Map.of("fileName", fileName, "type", type, "message", "Upload successful");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
 
