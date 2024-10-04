//package com.application.apigateway.config.kafka;
//
//import com.application.apigateway.util.Utility;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaConfig {
//    @Bean
//    public NewTopic createNotificationTopic() {
//        return TopicBuilder
//                .name(Utility.notificationTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createDepositTopic() {
//        return TopicBuilder
//                .name(Utility.depositTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createWithDrawTopic() {
//        return TopicBuilder
//                .name(Utility.withdrawTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createFundTransferTopic() {
//        return TopicBuilder
//                .name(Utility.fundTransferTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createHistoryLogTopic() {
//        return TopicBuilder
//                .name(Utility.historyLogTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createNotificationRollBackTopic() {
//        return TopicBuilder
//                .name(Utility.notificationRollBackTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createDepositRollBackTopic() {
//        return TopicBuilder
//                .name(Utility.depositRollBackTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createWithDrawRollBackTopic() {
//        return TopicBuilder
//                .name(Utility.withdrawRollBackTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createFundTransferRollBackTopic() {
//        return TopicBuilder
//                .name(Utility.fundTransferRollBackTopic)
//                .build();
//    }
//
//    @Bean
//    public NewTopic createHistoryLogRollBackTopic() {
//        return TopicBuilder
//                .name(Utility.historyLogRollBackTopic)
//                .build();
//    }
//}
