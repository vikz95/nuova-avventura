package util;

import business.Pagamento;

import java.util.Random;

public class PaymentSimulator {

    public static Pagamento.StatoPagamento pay() {
        Random random = new Random();
        int n = random.nextInt(10) + 1;
        if (1 <= n && n <= 7) {
            return Pagamento.StatoPagamento.ACCETTATO;
        } else if (n == 8) {
            return Pagamento.StatoPagamento.DATI_SCORRETTI;
        } else if (n == 9) {
            return Pagamento.StatoPagamento.SUPERAMENTO_TETTO;
        } else {
            return Pagamento.StatoPagamento.CREDITO_INSUFFICIENTE;
        }
    }
}
