package com.bezkoder.springjwt.scheduler;

import com.bezkoder.springjwt.models.Message;
import com.bezkoder.springjwt.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
@ConditionalOnProperty(name = "application.enable-schedulers")
public class MessageScheduler {

    @Autowired
    private MessageRepository messageRepository;

//    @Scheduled(fixedDelayString = "${application.processor-scheduler-fixed-delay}")
    @Scheduled(cron = "0 0 3,15 * * ?")
//    will run at 3am and 3pm
    public void scheduleTaskWithFixedRate() {
        List<Message> messageList= messageRepository.findAll();
        List<Message> listToDelete= new ArrayList<>();
        for(Message message: messageList){
            Duration between = Duration.between(message.getCreated().toInstant(), Instant.now());

            if (between.get(ChronoUnit.DAYS)>7){
                listToDelete.add(message);
            }
        }

        messageRepository.deleteAll(listToDelete);
    }
}
