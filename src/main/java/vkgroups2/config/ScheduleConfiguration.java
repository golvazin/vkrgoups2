package vkgroups2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import vkgroups2.service.GroupService;

@Configuration
@EnableAsync
@EnableScheduling
public class ScheduleConfiguration {
    
    @Autowired
    private GroupService groupService;
    
    @Scheduled(fixedRate=5*60*1000)
    public void checkNewPosts() {
        groupService.checkMessagesForAllGroups();
    }

}
