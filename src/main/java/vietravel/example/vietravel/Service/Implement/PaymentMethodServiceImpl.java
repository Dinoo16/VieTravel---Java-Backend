package vietravel.example.vietravel.Service.Implement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vietravel.example.vietravel.Model.PaymentMethod;
import vietravel.example.vietravel.Model.User;
import vietravel.example.vietravel.Repository.PaymentMethodRepository;
import vietravel.example.vietravel.Repository.UserRepository;
import vietravel.example.vietravel.Service.PaymentMethodService;
import vietravel.example.vietravel.dto.PaymentMethodDto;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;
    private final UserRepository userRepository;

    @Override
    public PaymentMethodDto createPaymentMethod(PaymentMethodDto dto) {
        PaymentMethod paymentMethod = toEntity(dto);
        PaymentMethod saved = paymentMethodRepository.save(paymentMethod);
        return toDto(saved);
    }

    @Override
    public PaymentMethodDto updatePaymentMethod(PaymentMethodDto dto) {
        Optional<PaymentMethod> optional = paymentMethodRepository.findById(dto.getId());
        if (optional.isEmpty()) {
            throw new RuntimeException("Payment Method not found");
        }
        PaymentMethod existing = optional.get();
        existing.setCardHolderName(dto.getCardHolderName());
        existing.setCardNumber(dto.getCardNumber());
        existing.setExpiryDate(dto.getExpiryDate());
        existing.setCardType(dto.getCardType());
        return toDto(paymentMethodRepository.save(existing));
    }

    @Override
    public void deletePaymentMethod(Long id) {
        if (!paymentMethodRepository.existsById(id)) {
            throw new RuntimeException("Payment Method not found");
        }
        paymentMethodRepository.deleteById(id);
    }

    private PaymentMethodDto toDto(PaymentMethod entity) {
        PaymentMethodDto dto = new PaymentMethodDto();
        dto.setId(entity.getId());
        dto.setCardHolderName(entity.getCardHolderName());
        dto.setCardNumber(entity.getCardNumber());
        dto.setExpiryDate(entity.getExpiryDate());
        dto.setCardType(entity.getCardType());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getUserId() : null);
        return dto;
    }

    private PaymentMethod toEntity(PaymentMethodDto dto) {
        PaymentMethod entity = new PaymentMethod();
        entity.setCardHolderName(dto.getCardHolderName());
        entity.setCardNumber(dto.getCardNumber());
        entity.setExpiryDate(dto.getExpiryDate());
        entity.setCardType(dto.getCardType());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            entity.setUser(user);
        }

        return entity;
    }
}
