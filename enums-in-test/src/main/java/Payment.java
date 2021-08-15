import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Payment {

    long amount;
    String currency;
    String purpose;
    String inn;
    Period period;
    Payer payer;
}
