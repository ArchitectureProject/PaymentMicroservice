package com.efrei.paymentmicroservice.provider.session;

import com.efrei.paymentmicroservice.model.dto.PaymentActualisationRequest;

public interface SessionProvider {
    void sendPaymentActualisation(String bearerToken, String sessionId, PaymentActualisationRequest paymentActualisationRequest);
}
