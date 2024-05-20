package io.micronaut.guice.doc.examples.bindings.linked;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest(startApplication = false, environments = "linked")
@Guice(
    modules = BillingModule.class,
    environments = "linked"
)
class LinkedBindingTest {
    @Test
    void testLinkedBinding(TransactionLog transactionLog, DatabaseTransactionLog databaseTransactionLog) {
        Assertions.assertNotNull(transactionLog);
        Assertions.assertNotNull(databaseTransactionLog);
        Assertions.assertInstanceOf(MySqlDatabaseTransactionLog.class, transactionLog);
        Assertions.assertInstanceOf(MySqlDatabaseTransactionLog.class, databaseTransactionLog);
    }
}

class BillingModule extends AbstractModule {
    @Provides
    public TransactionLog provideTransactionLog(DatabaseTransactionLog databaseTransactionLog) {
        return databaseTransactionLog;
    }

    @Provides
    public DatabaseTransactionLog provideDatabaseTransactionLog(MySqlDatabaseTransactionLog impl) {
        return impl;
    }
}

