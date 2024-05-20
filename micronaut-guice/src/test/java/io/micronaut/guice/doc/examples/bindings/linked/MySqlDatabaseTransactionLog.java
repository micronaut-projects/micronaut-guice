package io.micronaut.guice.doc.examples.bindings.linked;

import com.google.inject.Inject;

class MySqlDatabaseTransactionLog extends DatabaseTransactionLog {
    @Inject
    public MySqlDatabaseTransactionLog() {
    }
}
