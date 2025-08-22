package vietravel.example.vietravel.Service.ServiceInterface;

import vietravel.example.vietravel.Enum.PaymentStatus;
import vietravel.example.vietravel.dto.PaymentDto;

import java.util.List;

public interface PaymentService {

    String createPayment(Long bookingId);

    PaymentDto capturePayment(Long bookingId, String orderId);

    PaymentDto getPaymentById(Long paymentId);

    List<PaymentDto> getAllPayments();


}
