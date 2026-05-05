package pt.isel;

import kotlin.Pair;
import java.util.Map;

import static java.util.Map.entry;

/*
 * This is a Baseline to guide the code generation of a Mapper from a class to another
 * using the interface Mapper.
 * As a baseline, it users as sample a mapper from ArtistSpotify to Artist and
 * consider the following reference types:
 *  - mapper from State to Country;
 *  - mapper form Song to Track.
 */

public class ArtistSpotify2ArtistBaseline implements Mapper<ArtistSpotify, Artist> {
    // 'mappers' is a static constant that maps from a pair (Class, Class) to a Mapper.
    // e.g., (State.class, Country.class) to Mapper<State, Country>
    private static final Map< Pair< Class<?>, Class<?> >, Mapper<?, ?>> mappers = Map.ofEntries(
            entry(new Pair<>(State.class, Country.class), new State2CountryBaseline()),
            entry(new Pair<>(Song.class, Track.class), new Song2TrackBaseline())
    );

    // Mapper (source to dest)
    private static <T, R> Mapper<T, R> loadMapper(Class<T> srcClass, Class<R> destClass) {
        // Create the pair
        Pair<Class<T>, Class<R>> pair = new Pair<>(srcClass, destClass);
        // Get the mapper correspondent to the pair (srcClass, destClass), casting to Mapper<T, R>
        return (Mapper<T, R>) mappers.get(pair);
    }

    @Override
    public Artist mapFrom(ArtistSpotify src) {
        return new Artist(
                src.getId(),
                src.getName(),
                src.getKind(),
                loadMapper(State.class, Country.class).mapFrom(src.getOrigin()),
                loadMapper(Song.class, Track.class).mapFromList(src.getSongs())
        );
    }
}

class State2CountryBaseline implements Mapper<State, Country> {

    @Override
    public Country mapFrom(State src) {

        return new Country(src.getName(), src.getIdiom());
    }
}

class Song2TrackBaseline implements Mapper<Song, Track> {

    @Override
    public Track mapFrom(Song src) {

        return new Track(src.getName(), src.getYear());
    }
}