package com.example.demo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/view")
class ViewController {
    @RequestMapping("/memberview")
    fun memberView(model: Model): String {
        return "memberView"
    }

    @RequestMapping("/findmember")
    fun findMember(
        model: Model,
        @RequestParam("id")id: Long,
    ): String {
        model.addAttribute("id", id)
        return "findMember"
    }

    @RequestMapping("/savemember")
    fun saveMember(model: Model): String {
        return "saveMember"
    }

    @RequestMapping("/updatemember")
    fun updateMember(
        model: Model,
        @RequestParam("id")id: Long,
    ): String {
        model.addAttribute("id", id)
        return "updateMember"
    }

    @RequestMapping("/savegamecard")
    fun saveGameCard(
        model: Model,
        @RequestParam("id")id: Long,
    ): String {
        model.addAttribute("id", id)
        return "saveGameCard"
    }
}
