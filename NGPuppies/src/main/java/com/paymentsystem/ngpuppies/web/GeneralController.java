package com.paymentsystem.ngpuppies.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_CLIENT')")
@RequestMapping("${common.basepath}/invoice")
public class GeneralController {
    
}
