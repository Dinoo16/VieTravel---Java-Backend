package vietravel.example.vietravel.Controller.AdminController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vietravel.example.vietravel.Service.PaymentMethodService;
import vietravel.example.vietravel.dto.PaymentMethodDto;

@RestController
@RequestMapping("/api/admin/payment-methods")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPaymentMethodController {
    private final PaymentMethodService paymentMethodService;

    // Create a payment method
    @PostMapping
    public ResponseEntity<PaymentMethodDto> createPaymentMethod(@RequestBody PaymentMethodDto dto) {
        PaymentMethodDto created = paymentMethodService.createPaymentMethod(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Update a payment method
    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> updatePaymentMethod(
            @PathVariable Long id,
            @RequestBody PaymentMethodDto dto
    ) {
        dto.setId(id); // Ensure the ID from path is set into the DTO
        PaymentMethodDto updated = paymentMethodService.updatePaymentMethod(dto);
        return ResponseEntity.ok(updated);
    }

    // Delete a payment method
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return ResponseEntity.noContent().build();
    }
}
