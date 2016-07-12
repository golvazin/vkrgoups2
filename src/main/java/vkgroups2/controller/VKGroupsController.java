package vkgroups2.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import vkgroups2.domain.User;
import vkgroups2.domain.UserGroupStat;
import vkgroups2.domain.VKGroup;
import vkgroups2.repository.UserGroupStatRepository;
import vkgroups2.repository.UserRepository;
import vkgroups2.service.GroupService;

@Controller
public class VKGroupsController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserGroupStatRepository userGroupStatRepository;
    
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model) {
        model.addAttribute("title", "Start page");
        return "index";
    }
    

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model, Principal principal) {
        
        User currentUser = userRepository.findOne(principal.getName());
        
        model.addAttribute("title", "Dashboard")
        .addAttribute("currentUser", currentUser)
        .addAttribute("messageStats", userGroupStatRepository.findAllByUser(currentUser));;
        return "dashboard";
    }
    
    
    @RequestMapping(value = "/addGroup", method = RequestMethod.POST)
    public String addGroup(Principal principal, @RequestParam("name") String name, @RequestParam("url") String url) {
        groupService.saveGroup(name, url, principal.getName());
        return "redirect:/dashboard";
    }
    
    @RequestMapping(value = "/setRead", method = RequestMethod.POST)
    public @ResponseBody String setRead(Principal principal, @RequestParam("groupId") String groupIdString) {
        Long groupId = Long.parseLong(groupIdString);
        User user = userRepository.findOne(principal.getName());
        VKGroup group = groupService.findOne(groupId);
        UserGroupStat userGroupStat = userGroupStatRepository.findOneByUserAndVkGroup(user, group);
        userGroupStat.setReadMessages(group.getTotalMessages());
        userGroupStat = userGroupStatRepository.save(userGroupStat);
        return String.valueOf(group.getTotalMessages() - userGroupStat.getReadMessages());
    }


}
