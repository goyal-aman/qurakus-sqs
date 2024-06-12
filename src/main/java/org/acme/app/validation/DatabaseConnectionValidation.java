package org.acme.app.validation;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import javax.sql.DataSource;
import java.sql.Connection;

@ApplicationScoped
public class DatabaseConnectionValidation {

    public static final Logger LOG = Logger.getLogger(DatabaseConnectionValidation.class);

    @Inject
    DataSource dataSource;

    void onStart(@Observes StartupEvent ev) {
        try (Connection connection = dataSource.getConnection()) {
            LOG.info("Database connection established");
        }catch (Exception e){
            LOG.info("Database connection failed");
        }
    }
}
