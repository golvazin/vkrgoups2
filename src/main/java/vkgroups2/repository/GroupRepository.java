package vkgroups2.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import vkgroups2.domain.VKGroup;

@Repository
public interface GroupRepository extends CrudRepository<VKGroup, Long>{
    
    VKGroup findOneByUrl(String url);

}
