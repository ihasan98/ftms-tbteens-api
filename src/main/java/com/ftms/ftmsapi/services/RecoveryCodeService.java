package com.ftms.ftmsapi.services;

import com.ftms.ftmsapi.model.RecoveryCode;
import com.ftms.ftmsapi.model.User;
import com.ftms.ftmsapi.payload.ApiResponse;
import com.ftms.ftmsapi.payload.Time;
import com.ftms.ftmsapi.repository.RecoveryCodeRepository;
import com.ftms.ftmsapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class RecoveryCodeService {
    @Autowired
    private RecoveryCodeRepository recoveryCodeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository<User> userRepository;

    RecoveryCodeService() {}

    public ResponseEntity<?> createNewRecoveryCode(Long userID, String sendType) {
        System.out.println("creating code");
        try {
            userRepository.getOne(userID);
            TimeZone timezone = TimeZone.getTimeZone("GMT-5");
            Calendar created = Calendar.getInstance(timezone);
            Calendar expired = Calendar.getInstance(timezone);

            expired.setTimeInMillis(expired.getTime().getTime() + 3600000);

            String alphabet = "ABCDEFGHIJKLMOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
            StringBuilder code = new StringBuilder();

            while (code.length() < 10) {
                int character = (int) Math.floor(Math.random() * alphabet.length());
                code.append(alphabet.charAt(character));
            }

            String finalCode = code.toString();

            if (sendType.equals("email")) {
                sendCodeByEmail(finalCode, userID);
            }

            RecoveryCode newCode = new RecoveryCode(created.getTime(), expired.getTime(), finalCode, userID);
            recoveryCodeRepository.save(newCode);
            return new ResponseEntity<Object>(new ApiResponse(true, "Code created for user #" +
                    userID + "."), HttpStatus.OK);
        } catch (Exception error) {
            error.printStackTrace();
            return new ResponseEntity<Object>(new ApiResponse(false, error.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> verifyCodeEntry(Long userID, String code) {
        for (RecoveryCode recoveryCode : recoveryCodeRepository.findAll()) {
            if (code.equals(recoveryCode.getCode()) && userID.equals(recoveryCode.getUserID())) {
                Time timeInterval = new Time(recoveryCode.getExpiredAt(), new Date());
                if (timeInterval.isValidTimeInterval()) {
                    return new ResponseEntity<Object>(new ApiResponse(true, "Code verified"),
                            HttpStatus.OK);
                }
                else {
                    new ResponseEntity<Object>(new ApiResponse(false, "The code has expired!"),
                            HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity<Object>(new ApiResponse(false, "Cannot find the association " +
                "between the verification code entered with the current user."), HttpStatus.OK);
    }

    private void sendCodeByEmail(String code, Long userID) {
        System.out.println("sending code");
        User user = userRepository.getOne(userID);
        String email = user.getEmail();
        String name = user.getFirstName();

        String content = emailService.getRecoveryByEmailContent(name, code);

        emailService.sendEmail(name, email, content, "Nor-Weld Account Recovery");
    }
}
