package vietravel.example.vietravel.Service;


import vietravel.example.vietravel.dto.PaymentDto;
import vietravel.example.vietravel.dto.PaymentMethodDto;

public interface PaymentMethodService {

    PaymentMethodDto createPaymentMethod(PaymentMethodDto dto);

    PaymentMethodDto updatePaymentMethod(PaymentMethodDto dto);

    void deletePaymentMethod(Long id);


}
