package cn.syx.cache;


import jakarta.annotation.Resource;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyxApplicationListener implements ApplicationListener<ApplicationEvent> {

    @Resource
    private List<SyxCachePlugin> plugins;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent eve) {
            plugins.forEach(pl -> {
                pl.init();
                pl.startup();
            });
        } else if (event instanceof ContextClosedEvent eve) {
            plugins.forEach(SyxCachePlugin::shutdown);
        }
    }
}
