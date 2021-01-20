package com.joelmatth.gigstream.controller;

import com.joelmatth.gigstream.Config;
import com.joelmatth.gigstream.data.Repository;
import com.joelmatth.gigstream.model.Gig;
import lombok.Value;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Value
public class WebController implements ErrorController {

    Repository repository;
    Config config;

    @ModelAttribute("config")
    public Config config() {
        return config;
    }

    @ModelAttribute("mostCommonLocation")
    public String mostCommonLocation() {
        return repository.mostCommonLocation();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping("/error")
    public String error(Model model) {
        model.addAttribute("message", "An exception occurred in the program");
        return "error";
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Gig> gigs = repository.byRecentlyAdded();
        model.addAttribute("title", "Recently added");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String q, Model model) {
        List<Gig> gigs = repository.search(q);
        model.addAttribute("title", gigs.size() + " results for " + q);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/all")
    public String all(Model model) {
        List<Gig> gigs = repository.byDateDescending();
        model.addAttribute("title", "All gigs");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/location")
    public String location(Model model) {
        List<Gig> gigs = repository.byMostCommonLocation();
        model.addAttribute("title", repository.mostCommonLocation());
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/gig/{id}")
    public String gig(@PathVariable(name = "id") int id, Model model) {
        Optional<Gig> gig = repository.byId(id);

        if (!gig.isPresent()) {
            return "error";
        }

        model.addAttribute("gig", gig.get());
        return "gig";
    }

}
