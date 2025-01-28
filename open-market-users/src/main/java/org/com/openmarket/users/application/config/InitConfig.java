package org.com.openmarket.users.application.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.com.openmarket.users.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.users.application.gateway.message.dto.UserCreatedMessageDTO;
import org.com.openmarket.users.application.gateway.message.enumeration.EnumMessageEvent;
import org.com.openmarket.users.application.gateway.message.enumeration.EnumMessageType;
import org.com.openmarket.users.core.domain.entity.User;
import org.com.openmarket.users.core.domain.repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.com.openmarket.users.application.config.constants.QueueConstants.User.USER_QUEUE;

@Component
@RequiredArgsConstructor
public class InitConfig implements CommandLineRunner {
    @Value("${application.access.bank-adm-email}")
    private String BANK_ADM_EMAIL;

    @Value("${application.access.bank-adm-user}")
    private String BANK_ADM_USER;

    @Value("${application.access.bank-adm-password}")
    private String BANK_ADM_PASSWORD;

    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws JsonProcessingException {
        Optional<User> bankAdminUser = userRepository.findByEmail(BANK_ADM_EMAIL);
        if (bankAdminUser.isPresent()) return;

        String hashedNewPassword = BCrypt.hashpw(BANK_ADM_PASSWORD, BCrypt.gensalt(6));

        User bankUser = new User();
        bankUser.setEmail(BANK_ADM_EMAIL);
        bankUser.setPassword(hashedNewPassword);
        bankUser.setUserName(BANK_ADM_USER);
        bankUser.setEnabled(true);

        bankUser = userRepository.save(bankUser);

        CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
            .type(EnumMessageType.CREATED)
            .event(EnumMessageEvent.USER_EVENT)
            .data(mapper.writeValueAsString(new UserCreatedMessageDTO(String.valueOf(bankUser.getId()), bankUser.getUserName(), bankUser.getEmail(), bankUser.getPassword())))
            .build();

        rabbitTemplate.convertAndSend(USER_QUEUE, mapper.writeValueAsString(commonMessageDTO));
    }
}
