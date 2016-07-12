package vkgroups2.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import vkgroups2.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, String>{

}
