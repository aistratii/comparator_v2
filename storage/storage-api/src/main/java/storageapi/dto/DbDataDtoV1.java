package storageapi.dto;

import java.util.Map;
import java.util.Set;

public class DbDataDtoV1 {
    private final Map<Integer, Set<String>> db;

    public DbDataDtoV1(Map<Integer, Set<String>> db) {
        this.db = db;
    }

    public Set<Map.Entry<Integer, Set<String>>> entries() {
        return db.entrySet();
    }
}
