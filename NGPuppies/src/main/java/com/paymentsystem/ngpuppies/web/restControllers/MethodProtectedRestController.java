//package com.paymentsystem.ngpuppies.web.restControllers;
//
//import com.paymentsystem.ngpuppies.models.users.Admin;
//import com.paymentsystem.ngpuppies.models.users.ApplicationUser;
//import com.paymentsystem.ngpuppies.security.JwtTokenUtil;
//import com.paymentsystem.ngpuppies.services.AdminServiceImpl;
//import com.paymentsystem.ngpuppies.viewModels.AdminViewModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//
//@RestController
//@RequestMapping("protected")
//public class MethodProtectedRestController {
//
//    /**
//     * This is an example of some different kinds of granular restriction for endpoints. You can use the built-in SPEL expressions
//     * in @PreAuthorize such as 'hasRole()' to determine if a user has access. Remember that the hasRole expression assumes a
//     * 'ROLE_' prefix on all role names. So 'ADMIN' here is actually stored as 'ROLE_ADMIN' in database!
//     **/
//    @Autowired
//    private AdminServiceImpl adminService;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @GetMapping("/admin")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<?> getAdminProtectedGreeting() {
//        return ResponseEntity.ok("Greetings from admin protected method!");
//    }
//
//    @GetMapping("/admin/profile")
//    @PreAuthorize("hasRole('ADMIN')")
//    public AdminViewModel getAdminProfile(Authentication authentication) {
//        String username = authentication.getName();
//        Admin admin = adminService.getByUsername(username);
//
//        return AdminViewModel.fromModel(admin);
//    }
//
//    @GetMapping("/client")
//    @PreAuthorize("hasRole('CLIENT')")
//    public ResponseEntity<?> getClientProtectedGreeting() {
//        return ResponseEntity.ok("Greetings from CLIENT protected method!");
//    }
//
//}