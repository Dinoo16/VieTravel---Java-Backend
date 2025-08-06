package vietravel.example.vietravel.Service;

import vietravel.example.vietravel.Enum.PaymentStatus;
import vietravel.example.vietravel.dto.PaymentDto;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto dto);

    PaymentDto updatePaymentStatus(Long paymentId, PaymentStatus paymentStatus);

    PaymentDto getPaymentById(Long id);


}
