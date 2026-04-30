package pt.isel;

import kotlin.Pair;
import java.util.Map;

import static java.util.Map.entry;

/*
 * This is a Baseline to guide the code generation of a Mapper from a class to another
 * using the interface Mapper.
 * This is specific for ArtistSpotify to Artist mapping without reference types
 * (including Lists).
 */

public class ArtistSpotify2ArtistBaseline implements Mapper<ArtistSpotify, Artist> {

    @Override
    public Artist mapFrom(ArtistSpotify src) {
        return new Artist(
                src.getId(),
                src.getName(),
                src.getKind()
        );
    }
}