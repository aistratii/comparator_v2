package storageapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
public class OperationsUrlV1 {

    public final String workQueue = "/workqueue";

    public final String db = "/db";

}
