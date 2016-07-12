package vkgroups2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vkgroups2.domain.User;
import vkgroups2.domain.UserGroupStat;
import vkgroups2.domain.VKGroup;
import vkgroups2.repository.GroupRepository;
import vkgroups2.repository.UserGroupStatRepository;
import vkgroups2.repository.UserRepository;

/**
 * @author ogolv
 *
 */
@Service
public class GroupService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserGroupStatRepository userGroupStatRepository;
    
    @Autowired
    private VKApiHelper vkApiHelper;

    public GroupService() {
    }
    
        
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GroupRepository getGroupRepository() {
        return groupRepository;
    }

    public void setGroupRepository(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public VKApiHelper getVkApiHelper() {
        return vkApiHelper;
    }

    public void setVkApiHelper(VKApiHelper vkApiHelper) {
        this.vkApiHelper = vkApiHelper;
    }
    
    public Iterable<VKGroup> findAll() {
        return groupRepository.findAll();
    }

    
    public VKGroup findOne(Long id) {
        return groupRepository.findOne(id);
    }


    /**
     * 
     */
    public void saveGroup(String name, String url, String userId) {
        VKGroup group = groupRepository.findOneByUrl(url);
        User currentUser = userRepository.findOne(userId);
        UserGroupStat userGroupStat;
        if (group == null) {
            group = new VKGroup(name, url);
            userGroupStat = new UserGroupStat(currentUser, group);
        } else {
            userGroupStat = userGroupStatRepository.findOneByUserAndVkGroup(currentUser, group);
        }
        group = checkAndSetMessages(group);
        boolean isSubscribed = vkApiHelper.checkMembership(group.getUrl(), currentUser.getVkId());
        userGroupStat.setIsSubscriber(isSubscribed);
        userGroupStat.setReadMessages(group.getTotalMessages());
        userGroupStatRepository.save(userGroupStat);
    }
    
    public void checkMessagesForAllGroups() {
        Iterable<VKGroup> groups = groupRepository.findAll();
        groups.forEach(this::checkAndSetMessages);
    }


    private VKGroup checkAndSetMessages(VKGroup group) {
        int totalMessages = vkApiHelper.retrieveGroupMessages(group.getUrl());
        group.setTotalMessages(totalMessages);
        return groupRepository.save(group);
    }



    

}
