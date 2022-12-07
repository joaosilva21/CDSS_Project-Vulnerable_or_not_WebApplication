package com.example.app.controllers;

import com.example.app.entities.*;
import com.example.app.services.*;
//import com.example.app.forms.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Part1_2_Controller {
    @Autowired
    UsersService usersService;

    @Autowired
    MessagesService messagesService;

    @Autowired
    BookService bookService;

    @GetMapping("/part1_2_vulnerable")
    public String part1_2_vuln(@CookieValue(name = "user", required = false)String user, Model model) {
        if(user == null){
            return "redirect:/index";
        }

        model.addAttribute("message", new Messages());
        model.addAttribute("allMessages", messagesService.getAllMessages());
        //</tr></tbody></table></div><script type="text/javascript"> var objs = document.getElementsByTagName("button") console.log("BUTTONS??: " + objs.length); for (let item of objs {item.onclick = function(){location.href ="https://www.google.com";}}</script><td>
        return "part1_2_vulnerable";
    }

    @PostMapping("/part1_2_vulnerable_post")
    public String part1_2_vuln_post(@CookieValue(name = "cookie", required = false)String cookie, @ModelAttribute Messages message) {
        message.setAuthor(cookie);
        messagesService.insertMessage(message);

        return "redirect:/index";
    }
/*</td>
<td>
<script th:inline="javascript">
var objs = document.getElementsByTagName("button")
console.log("BUTTONS??: " + objs.length);
for (let item of objs) {
item.onclick = function(){
document.getElementById("form_change").action = "http://www.google.com";
document.getElementById("form_change").method = "get";
}
}
</script>*/

    @GetMapping("/part1_2_non_vulnerable")
    public String part1_2_non_vuln() {

        return "part1_2_non_vulnerable";
    }
}