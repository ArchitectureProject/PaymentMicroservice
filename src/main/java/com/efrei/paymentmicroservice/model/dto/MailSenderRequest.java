package com.efrei.paymentmicroservice.model.dto;

import java.util.List;

public record MailSenderRequest(String templateName, List<MailSenderParam> emailParams, String receiverMailAdress, String mailObject) {
}
