package storageapi.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
public final class OperationsUrlV1 {

    public static final String workQueue = "/v1/workqueue";

    public static final String db = "/v1/db";

}
