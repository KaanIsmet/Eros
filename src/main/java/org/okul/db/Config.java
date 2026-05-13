package org.okul.db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.okul.model.Role;

public class Config {
    private static Jdbi jdbi;

    public static Jdbi getInstance() {
        if (jdbi == null) {
            String url = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            if (url == null || user == null || password == null)
                throw new IllegalStateException("Missing database environmental variables");
            jdbi = Jdbi.create(url, user, password);
            jdbi.installPlugin(new SqlObjectPlugin());
            jdbi.registerColumnMapper(Role.class, (rs, col, ctx) ->
                    Role.valueOf(rs.getString(col))
            );
        }
        return jdbi;
    }
}
