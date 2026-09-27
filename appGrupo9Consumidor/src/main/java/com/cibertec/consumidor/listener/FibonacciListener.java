package com.cibertec.consumidor.listener;

import com.cibertec.consumidor.config.RabbitMQConfig;
import com.cibertec.consumidor.service.FibonacciService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FibonacciListener {

    private final FibonacciService fibonacciService;

    @Autowired
    public FibonacciListener(FibonacciService fibonacciService) {
        this.fibonacciService = fibonacciService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String cadenaNumeros) {
        System.out.println("[Grupo9Queue] Mensaje recibido: " + cadenaNumeros);

        // parte el string por ";"
        Integer[] integerArray = Stream.of(cadenaNumeros.split(";"))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        List<Integer> positions = Arrays.asList(integerArray);

        // espera que pide el enunciado (20s)
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("La espera de 20 segundos fue interrumpida.");
        }

        List<Long> fibonacciResult = fibonacciService.calculateSequence(positions);

        System.out.println("Posiciones solicitadas: " + positions);
        System.out.println("Resultado Fibonacci: " + fibonacciResult);
    }
}
