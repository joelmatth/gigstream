package com.joelmatth.gigstream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Controller
public class WebController {

    private final Repository repository;
    private final Search search;
    private final Config config;
    private final GigUrlFactory gigUrlFactory;

    WebController(Repository repository, Search search, Config config, GigUrlFactory gigUrlFactory) {
        this.repository = repository;
        this.search = search;
        this.config = config;
        this.gigUrlFactory = gigUrlFactory;
    }

    @ModelAttribute("config")
    public Config config() {
        return config;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(search.recent());
        model.addAttribute("title", "Recently added");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String q, Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(search.byTerm(q));
        model.addAttribute("title", gigs.size() + " results for " + q);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/all")
    public String all(Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(repository.findAllByOrderByDateDesc());
        model.addAttribute("title", "All gigs");
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/location")
    public String location(Model model) {
        List<GigUrl> gigs = gigUrlFactory.of(repository.findByLocationOrderByDateDesc(
                config.mostCommonLocation));

        model.addAttribute("title", config.mostCommonLocation);
        model.addAttribute("gigs", gigs);
        return "results";
    }

    @GetMapping("/gig/{id}")
    public String gig(@PathVariable(name = "id") int id, Model model) {
        Optional<Gig> gig = search.load(id);

        if (!gig.isPresent()) {
            return "results";
        }

        model.addAttribute("gig", gigUrlFactory.of(gig.get()));
        return "gig";
    }

    @GetMapping("/reload")
    public String reload(Model model) {
        load();
        return "results";
    }

    @PostConstruct
    public void load() {
        URL url = null;

        try {
            URL rootUrl = new URL(config.gigStore);
            url = new URL(rootUrl, config.gigList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try (Scanner scanner = new Scanner(url.openStream(), StandardCharsets.UTF_8.toString()))
        {
            scanner.useDelimiter("\\A");
            String json = scanner.hasNext() ? scanner.next() : "";
            ObjectMapper mapper = new ObjectMapper();
            List<Gig> gigs = mapper.readValue(json, new TypeReference<List<Gig>>(){});
            repository.deleteAll();
            repository.saveAll(gigs);
            config.mostCommonLocation = search.mostCommonLocation();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

}
