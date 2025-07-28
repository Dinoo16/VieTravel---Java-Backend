package vietravel.example.vietravel.Model;

import jakarta.persistence.*;
import lombok.*;
import vietravel.example.vietravel.Enum.CardType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false, unique = true, length = 16)
    private String cardNumber;

    // Expiry date (stored as string "MM/YY")
    @Column(nullable = false, length = 5)
    private String expiryDate;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    // User who owns this payment method (Many methods per user)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
