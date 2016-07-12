package vkgroups2.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import vkgroups2.domain.User;
import vkgroups2.domain.UserGroupStat;
import vkgroups2.domain.VKGroup;

public interface UserGroupStatRepository extends CrudRepository<UserGroupStat, Long> {
    
    UserGroupStat findOneByUserAndVkGroup(User user, VKGroup vkGroup);
    
    List<UserGroupStat> findAllByUser(User user);

}
