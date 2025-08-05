package vietravel.example.vietravel.Service;


import vietravel.example.vietravel.dto.PaymentMethodDto;

import java.util.List;

public interface PaymentMethodService {

    PaymentMethodDto createPaymentMethod(PaymentMethodDto dto);

    PaymentMethodDto updatePaymentMethod(PaymentMethodDto dto);

    void deletePaymentMethod(Long id);

    PaymentMethodDto getPaymentMethodById(Long id);

    List<PaymentMethodDto> getAllPaymentMethods();

}
