package vietravel.example.vietravel.dto;


import lombok.Data;
import vietravel.example.vietravel.Enum.CardType;

@Data
public class PaymentMethodDto {
    private Long id;
    private String cardHolderName;
    private String cardNumber;
    private String expiryDate;
    private CardType cardType;
    private Long userId;
}
