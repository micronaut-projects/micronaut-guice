package io.micronaut.guice.doc.examples.bindings.provider;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.micronaut.guice.annotation.Guice;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import java.sql.Connection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@MicronautTest(startApplication = false, environments = "provider")
@Guice(modules = BillingModule.class, environments = "provider")
public class ProviderBindingTest {
    @MockBean Connection connection = Mockito.mock(Connection.class);
    @Inject TransactionLog transactionLog;

    @Test
    void testProviderInjection() {
        assertInstanceOf(DatabaseTransactionLog.class, transactionLog);
        DatabaseTransactionLog dtl = (DatabaseTransactionLog) transactionLog;
        Assertions.assertNotNull(dtl.connection());
    }
}

class BillingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TransactionLog.class)
            .toProvider(DatabaseTransactionLogProvider.class);
    }
}
interface TransactionLog {}
class DatabaseTransactionLogProvider implements Provider<TransactionLog> {
    private final Connection connection;

    @Inject
    public DatabaseTransactionLogProvider(Connection connection) {
        this.connection = connection;
    }

    public TransactionLog get() {
        return new DatabaseTransactionLog(connection);
    }
}

record DatabaseTransactionLog(Connection connection) implements TransactionLog {}
