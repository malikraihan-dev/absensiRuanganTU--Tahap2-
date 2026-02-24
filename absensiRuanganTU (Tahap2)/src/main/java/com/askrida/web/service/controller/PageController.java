package com.askrida.web.service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.askrida.web.service.repository.RepositoryTes;
import com.askrida.web.service.model.RestResult;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    @Autowired
    private RepositoryTes repTes;

    @GetMapping("/")
    public String dashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        List<RestResult> dataAbsensi;
        try {
            dataAbsensi = repTes.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Dashboard] Error loading absensi: " + e.getMessage());
            dataAbsensi = new ArrayList<>();
        }
        model.addAttribute("dataAbsensi", dataAbsensi);
        model.addAttribute("totalAbsensi", dataAbsensi.size());

        long hadir = dataAbsensi.stream().filter(r -> r.getWaktu_input() != null).count();
        model.addAttribute("totalHadir", hadir);
        model.addAttribute("totalRuangan", 4);
        model.addAttribute("username", session.getAttribute("username"));
        return "dashboard";
    }

    @GetMapping("/monitoring")
    public String monitoring(Model model, HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        List<RestResult> dataAbsensi;
        try {
            dataAbsensi = repTes.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("[Monitoring] Error loading absensi: " + e.getMessage());
            dataAbsensi = new ArrayList<>();
        }
        model.addAttribute("dataAbsensi", dataAbsensi);
        model.addAttribute("username", session.getAttribute("username"));
        return "dashboard";
    }

    @GetMapping("/face-register")
    public String faceRegister(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "face-register";
    }

    @GetMapping("/face-absensi")
    public String faceAbsensi(HttpSession session) {
        if (session.getAttribute("loggedIn") == null) {
            return "redirect:/login";
        }
        return "face-absensi";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("loggedIn") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @PostMapping("/api/login")
    @ResponseBody
    public Map<String, Object> processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        // Simple authentication - replace with database auth as needed
        if ("admin".equals(username) && "admin123".equals(password)) {
            session.setAttribute("loggedIn", true);
            session.setAttribute("username", username);
            response.put("success", true);
            response.put("message", "Login berhasil");
        } else {
            response.put("success", false);
            response.put("message", "Username atau password salah");
        }
        return response;
    }
}
