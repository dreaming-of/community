package com.chl.community.controller;

import com.chl.community.entity.Message;
import com.chl.community.entity.Page;
import com.chl.community.entity.User;
import com.chl.community.service.MessageService;
import com.chl.community.service.UserService;
import com.chl.community.utils.CommunityUtil;
import com.chl.community.utils.HostHolder;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        List<Message> conversationList = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations = new ArrayList<>();
        if (conversationList != null){
            for (Message msg : conversationList){
                Map<String, Object> map = new HashMap<>();
                map.put("conversation", msg);
                map.put("unreadCount", messageService.findLetterUnreadCount(user.getId(),msg.getConversationId()));
                map.put("letterCount", messageService.findLetterCount(msg.getConversationId()));
                int targetId = user.getId() == msg.getFromId() ? msg.getToId() : msg.getFromId();
                map.put("target", userService.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations", conversations);
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", letterUnreadCount);
        return "/site/letter";
    }

    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Page page, Model model){
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters = new ArrayList<>();
        if (letterList != null){
            for (Message msg : letterList){
                Map<String, Object> map = new HashMap<>();
                map.put("letter", msg);
                map.put("fromUser",userService.findUserById(msg.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        model.addAttribute("target", getLetterTarget(conversationId));
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty())
            messageService.readMessae(ids);
        return "/site/letter-detail";
    }

    private User getLetterTarget(String conversationId){
        String[] ids = conversationId.split("_");
        int id0 = Integer.parseInt(ids[0]);
        int id1 = Integer.parseInt(ids[1]);

        if (hostHolder.getUser().getId() == id0)
            return userService.findUserById(id1);
        else return userService.findUserById(id0);
    }

    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();

        if (letterList != null){
            for (Message msg : letterList){
                if (hostHolder.getUser().getId() == msg.getToId() && msg.getStatus() == 0)
                    ids.add(msg.getId());
            }
        }

        return ids;
    }

    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content){
        User target = userService.findUserByName(toName);
        if (target == null)
            return CommunityUtil.getJSONString(1, "目标用户不存在");
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId())
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        else message.setConversationId(message.getToId() + "_" + message.getFromId());
        message.setContent(content);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0);
    }
}