package io.micronaut.guice.doc.examples.bindings.map;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.multibindings.Multibinder;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.net.URI;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = "map")
@Guice(
    modules = FlickrPluginModule.class,
    classes = FlickrPhotoSummarizer.class,
    environments = "map"
)
@Disabled("Map binding not yet supported")
public class MapBinderTest {
    @Inject
    Map<String, UriSummarizer> uriSummarizers;

    @Test
    void testMultiBindings() {
        Assertions.assertNotNull(uriSummarizers);
        Assertions.assertEquals(2, uriSummarizers.size());
        Assertions.assertInstanceOf(FlickrPhotoSummarizer.class, uriSummarizers.get("flickr"));
        Assertions.assertInstanceOf(GooglePhotoSummarizer.class, uriSummarizers.get("google"));
    }
}

class FlickrPluginModule extends AbstractModule {
    public void configure() {
        MapBinder<String, UriSummarizer> uriBinder = MapBinder.newMapBinder(binder(), String.class, UriSummarizer.class);
        uriBinder.addBinding("flickr").to(FlickrPhotoSummarizer.class);
        uriBinder.addBinding("google").toInstance(new GooglePhotoSummarizer());
    }
}
interface UriSummarizer {
    /**
     * Returns a short summary of the URI, or null if this summarizer doesn't
     * know how to summarize the URI.
     */
    String summarize(URI uri);
}

class FlickrPhotoSummarizer implements UriSummarizer {
    @Override
    public String summarize(URI uri) {
        return "flickr";
    }
}
class GooglePhotoSummarizer implements UriSummarizer {
    @Override
    public String summarize(URI uri) {
        return "google";
    }
}
