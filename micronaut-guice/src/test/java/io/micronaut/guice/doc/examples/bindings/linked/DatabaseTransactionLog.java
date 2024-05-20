package io.micronaut.guice.doc.examples.bindings.linked;

import com.google.inject.Inject;

public class DatabaseTransactionLog implements TransactionLog {
    @Inject
    public DatabaseTransactionLog() {
    }
}
