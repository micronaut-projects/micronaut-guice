package io.micronaut.guice.doc.examples.bindings.multi;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.multibindings.Multibinder;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.net.URI;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false)
@Guice(
    modules = FlickrPluginModule.class,
    classes = FlickrPhotoSummarizer.class
)
public class MultiBinderTest {
    @Inject
    Set<UriSummarizer> uriSummarizers;

    @Test
    void testMultiBindings() {
        Assertions.assertNotNull(uriSummarizers);
        Assertions.assertEquals(2, uriSummarizers.size());
        Assertions.assertTrue(uriSummarizers.stream().anyMatch(s -> s instanceof FlickrPhotoSummarizer));
        Assertions.assertTrue(uriSummarizers.stream().anyMatch(s -> s instanceof GooglePhotoSummarizer));
    }
}

class FlickrPluginModule extends AbstractModule {
    public void configure() {
        Multibinder<UriSummarizer> uriBinder = Multibinder.newSetBinder(binder(), UriSummarizer.class);
        uriBinder.addBinding().to(FlickrPhotoSummarizer.class);
        uriBinder.addBinding().toInstance(new GooglePhotoSummarizer());
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
