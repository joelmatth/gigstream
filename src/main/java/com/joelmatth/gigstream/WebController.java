package com.joelmatth.gigstream;

import com.joelmatth.gigstream.data.Data;
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

    Data data;
    Config config;
    GigUrlFactory gigUrlFactory;

    @ModelAttribute("config")
    public Config config() {
        return config;
    }

    @ModelAttribute("mostCommonLocation")
    public String mostCommonLocation() {
        return data.mostCommonLocation();
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
        List<GigUrl> gigs = gigUrlFactory.of(data.byRecentlyAdded());
        model.addAttribute("title", "Recently added");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String q, Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(data.search(q));
        model.addAttribute("title", gigs.size() + " results for " + q);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/all")
    public String all(Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(data.byDateDescending());
        model.addAttribute("title", "All gigs");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/location")
    public String location(Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(data.byMostCommonLocation());
        model.addAttribute("title", data.mostCommonLocation());
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/gig/{id}")
    public String gig(@PathVariable(name = "id") int id, Model model) {
        Optional<Gig> gig = data.byId(id);

        if (!gig.isPresent()) {
            return "error";
        }

        model.addAttribute("gig", gigUrlFactory.of(gig.get()));
        return "gig";
    }

}
