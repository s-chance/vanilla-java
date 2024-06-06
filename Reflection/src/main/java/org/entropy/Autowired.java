package org.entropy;

import java.lang.annotation.*;

@Documented
@Target(ElementType.CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
