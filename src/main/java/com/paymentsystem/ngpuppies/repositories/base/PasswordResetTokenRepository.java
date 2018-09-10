package com.paymentsystem.ngpuppies.repositories.base;

import com.paymentsystem.ngpuppies.models.PasswordResetToken;

public interface PasswordResetTokenRepository {
    boolean create(PasswordResetToken passwordResetToken);

    PasswordResetToken findByToken(String token);
}
