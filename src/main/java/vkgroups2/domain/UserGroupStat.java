package vkgroups2.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_message_stats", 
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "vk_group_id"})})
public class UserGroupStat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name="vk_group_id")
    private VKGroup vkGroup;
    
    @Column(name = "is_subscriber")
    private Boolean isSubscriber = false;
    
    @Column(name = "read_messages")
    private Integer readMessages = 0;

    public UserGroupStat() {
    }

    public UserGroupStat(User user, VKGroup vkGroup) {
        this.user = user;
        this.vkGroup = vkGroup;
    }

    public UserGroupStat(User user, VKGroup vkGroup, Boolean isSubscriber, Integer readMessages) {
        this.user = user;
        this.vkGroup = vkGroup;
        this.isSubscriber = isSubscriber;
        this.readMessages = readMessages;
    }

    public UserGroupStat(Long id, User user, VKGroup vkGroup, Boolean isSubscriber, Integer readMessages) {
        this.id = id;
        this.user = user;
        this.vkGroup = vkGroup;
        this.isSubscriber = isSubscriber;
        this.readMessages = readMessages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VKGroup getVkGroup() {
        return vkGroup;
    }

    public void setVkGroup(VKGroup vkGroup) {
        this.vkGroup = vkGroup;
    }

    public Boolean getIsSubscriber() {
        return isSubscriber;
    }

    public void setIsSubscriber(Boolean isSubscriber) {
        this.isSubscriber = isSubscriber;
    }

    public Integer getReadMessages() {
        return readMessages;
    }

    public void setReadMessages(Integer readMessages) {
        this.readMessages = readMessages;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((isSubscriber == null) ? 0 : isSubscriber.hashCode());
        result = prime * result + ((readMessages == null) ? 0 : readMessages.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        result = prime * result + ((vkGroup == null) ? 0 : vkGroup.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserGroupStat other = (UserGroupStat) obj;
        if (isSubscriber == null) {
            if (other.isSubscriber != null)
                return false;
        } else if (!isSubscriber.equals(other.isSubscriber))
            return false;
        if (readMessages == null) {
            if (other.readMessages != null)
                return false;
        } else if (!readMessages.equals(other.readMessages))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        if (vkGroup == null) {
            if (other.vkGroup != null)
                return false;
        } else if (!vkGroup.equals(other.vkGroup))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserMessageStat [user=" + user + ", vkGroup=" + vkGroup + ", isSubscriber=" + isSubscriber
                + ", readMessages=" + readMessages + "]";
    }
    

    

}
