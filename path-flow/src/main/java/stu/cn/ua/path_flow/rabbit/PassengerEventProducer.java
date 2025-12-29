package stu.cn.ua.path_flow.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import stu.cn.ua.path_flow.config.RabbitConfig;

@Slf4j
@Service
@RequiredArgsConstructor
public class PassengerEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public void send(PassengerCoordinateEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EXCHANGE,
                RabbitConfig.ROUTING_KEY,
                event
        );
    }
}
