package org.okul.db;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.okul.config.Env;
import org.okul.model.Role;

public class Config {
    private static Jdbi jdbi;

    public static Jdbi getInstance() {

        if (jdbi == null) {
            String url = Env.get("DB_URL");
            String user = Env.get("DB_USER");
            String password = Env.get("DB_PASSWORD");

            if (url == null || user == null || password == null) {
                throw new IllegalStateException(
                        "Missing database environment variables (DB_URL, DB_USER, DB_PASSWORD). "
                                + "Set them in the environment or in a .env file in the project root."
                );
            }
            jdbi = Jdbi.create(url, user, password);
            jdbi.installPlugin(new SqlObjectPlugin());
            jdbi.registerColumnMapper(Role.class, (rs, col, ctx) ->
                    Role.valueOf(rs.getString(col))
            );
        }
        return jdbi;
    }
}