package com.studium.scheduler;

import com.studium.models.Message;
import com.studium.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@ConditionalOnProperty(name = "application.enable-schedulers")
public class MessageScheduler {

    @Autowired
    private MessageRepository messageRepository;

    //    will run at 3am and 3pm
    @Scheduled(cron = "0 0 3,15 * * ?")
    public void scheduleTaskWithFixedRate() {
        List<Message> messageList= messageRepository.findAll();
        List<Message> listToDelete= new ArrayList<>();
        for(Message message: messageList){
            Duration between = Duration.between(message.getCreated().toInstant(), Instant.now());

            if (between.toDays()>7){
                listToDelete.add(message);
            }
        }
        if (listToDelete.size()>0){
            messageRepository.deleteAll(listToDelete);
        }
    }
}
