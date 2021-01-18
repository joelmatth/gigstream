package com.joelmatth.gigstream;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface Repository extends MongoRepository<Gig, Integer> {

    List<Gig> findAll();

    List<Gig> findAllByOrderByDateDesc();

    List<Gig> findByLocationOrderByDateDesc(String location);

    List<Gig> findByArtistContainingIgnoreCaseOrNameContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String artist, String name, String location);

    List<Gig> findById(int id);

    List<Gig> findTop12ByOrderByIdDesc();

}
