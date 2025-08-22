package vietravel.example.vietravel.Controller.UserController;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vietravel.example.vietravel.Model.Payment;
import vietravel.example.vietravel.Service.Implement.PaymentServiceImpl;
import vietravel.example.vietravel.dto.PaymentDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @PostMapping("/create/{bookingId}")
    public ResponseEntity<?> createPayment(@PathVariable Long bookingId) {
        try {
            String approvalLink = paymentService.createPayment(bookingId);
            return ResponseEntity.ok(Map.of("approvalUrl", approvalLink, "bookingId", bookingId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create payment: " + e.getMessage()));
        }
    }

    @PostMapping("/capture/{bookingId}/{orderId}")
    public ResponseEntity<?> capturePayment(
            @PathVariable Long bookingId,
            @PathVariable String orderId
    ) {
        try {
            PaymentDto paymentdto = paymentService.capturePayment(bookingId, orderId);
            return ResponseEntity.ok(paymentdto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to capture payment: " + e.getMessage()));
        }
    }   

    // User get payment by id
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPayment(@PathVariable Long paymentId) {
        try {
            PaymentDto paymentdto = paymentService.getPaymentById(paymentId);
            return ResponseEntity.ok(paymentdto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Payment not found: " + e.getMessage()));
        }
    }


}

