package com.joelmatth.gigstream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Controller
public class WebController {

    @Autowired Repository repository;
    @Autowired Search search;

    @GetMapping("/")
    public String index(Model model) {
        List<Gig> gigs = search.recent();
        model.addAttribute("title", "Recently added");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String q, Model model) {
        List<Gig> gigs = search.byTerm(q);
        model.addAttribute("title", gigs.size() + " results for " + q);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/all")
    public String all(Model model) {
        List<Gig> gigs = repository.findAllByOrderByDateDesc();
        model.addAttribute("title", "All gigs");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/location")
    public String location(Model model) {
        List<Gig> gigs = repository.findByLocationOrderByDateDesc(
                Config.mostCommonLocation);

        model.addAttribute("title", Config.mostCommonLocation);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/gig/{id}")
    public String gig(@PathVariable(name = "id") int id, Model model) {
        Optional<Gig> gig = search.load(id);

        if (!gig.isPresent()) {
            return "results";
        }

        model.addAttribute("gig", gig.get());
        return "gig";
    }

    @GetMapping("/reload")
    public String reload(Model model) {
        load();
        return "results";
    }

    @PostConstruct
    public void load() {
        URL url = UrlBuilder.gigList();

        try (Scanner scanner = new Scanner(url.openStream(), StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            ObjectMapper mapper = new ObjectMapper();
            List<Gig> gigs = mapper.readValue(json, new TypeReference<List<Gig>>(){});
            repository.deleteAll();
            repository.saveAll(gigs);
            Config.mostCommonLocation = search.mostCommonLocation();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

}
