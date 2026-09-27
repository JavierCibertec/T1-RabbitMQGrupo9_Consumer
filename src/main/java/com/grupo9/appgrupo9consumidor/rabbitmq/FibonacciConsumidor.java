package com.grupo9.appgrupo9consumidor.rabbitmq;

import com.grupo9.appgrupo9consumidor.config.RabbitMqConfig;
import com.grupo9.appgrupo9consumidor.service.FibonacciService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class FibonacciConsumidor {

    private final FibonacciService fibonacciService;

    @RabbitListener(queues = RabbitMqConfig.QUEUE)
    public void procesarFibonacci(String cadenaNumeros) throws InterruptedException {
        log.info("Mensaje recibido: " + cadenaNumeros);

        Integer[] integerArray = Stream.of(cadenaNumeros.split(";"))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

        log.info("Calculando Fibonacci... pausa de 20 segundos");
        Thread.sleep(20000);

        for (Integer posicion : integerArray) {
            long resultado = fibonacciService.fibonacci(posicion);
            log.info("Posición " + posicion + " -> " + resultado);
        }
    }
}