package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V7__mysql_lob_columns extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        String driverName = context.getConnection().getMetaData().getDriverName();
        if (driverName.contains("MySQL")) {
            try (Statement stmt = context.getConnection().createStatement()) {
                stmt.execute("ALTER TABLE artifact MODIFY source_content LONGTEXT NOT NULL");
                stmt.execute("ALTER TABLE artifact MODIFY rendered_html LONGTEXT NOT NULL");
            }
        }
    }
}
