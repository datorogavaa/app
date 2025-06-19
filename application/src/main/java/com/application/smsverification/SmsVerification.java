//package com.application.smsverification;
//import com.twilio.Twilio;
//import com.twilio.rest.verify.v2.service.Verification;
//import com.twilio.rest.verify.v2.service.VerificationCheck;
//
//import java.util.Scanner;
//
//public class SmsVerification {
//        // Find your Account Sid and Token at twilio.com/console
//        public static final String ACCOUNT_SID = "ACbb2b2730de72e4e021d4570023c0d639";
//        public static final String AUTH_TOKEN = "d2f77ffd6a43e7e43aa883f8d3d88f63";
//        private final Integer number;
//        private static final String ServiceSID = "VA7395d45a9b4f66c4e1ecd658a2a9a3fb";
//
//            public SmsVerification(Integer number) {
//                this.number = number;
//            }
//            public boolean sendCode() {
//                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//                    Verification verification = Verification.creator(
//                                    ServiceSID,
//                                    "+995" + this.number,
//                                    "sms").create();
//                    Scanner scanner = new Scanner(System.in);
//                    System.out.println("Enter the code: ");
//                    String code = scanner.nextLine();
//                    String verify = verify(code);
//                    if(verify.equals("approved")) {
//                        System.out.println("Verified");
//                        return true;
//                    }else {
//                        System.out.println("Wrong Code or something else");
//                        return false;
//                    }
//            }
//            private String verify(String code) {
//                String verify = VerificationCheck.creator(ServiceSID).setTo("+995"+this.number.toString()).setCode(code).create().getStatus();
//                return verify;
//            };
//
//}

