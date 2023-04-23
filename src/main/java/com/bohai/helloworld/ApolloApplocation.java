package com.bohai.helloworld;

import com.ctrip.framework.apollo.ConfigService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApolloApplocation {
    @RestController
    @RequestMapping(path = "/config/")
    public class ApolloConfigurationController {

        @RequestMapping(path = "/{key}")
        public String getConfigForKey(@PathVariable("key") String key){
            return key+"的值为:"+ConfigService.getAppConfig().getProperty( key, "undefined");
        }
    }
}
