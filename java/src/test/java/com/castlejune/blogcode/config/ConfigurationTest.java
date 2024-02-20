package com.castlejune.blogcode.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

class ConfigurationTest {

    @Test
    void configuration() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
        ac.register(MyConfig.class);
        ac.refresh();

        Bean1 bean1 = ac.getBean(Bean1.class);
        Bean2 bean2 = ac.getBean(Bean2.class);

        Assertions.assertThat(bean1.item).isSameAs(bean2.item);
    }

    @Test
    void proxyConfiguration() {
        MyConfigProxy myConfigProxy = new MyConfigProxy();

        Bean1 bean1 = myConfigProxy.bean1();
        Bean2 bean2 = myConfigProxy.bean2();

        Assertions.assertThat(bean1.item).isSameAs(bean2.item);
    }

    static class MyConfigProxy extends MyConfig {
        private Item item;

        @Override
        Item item() {
            if (this.item == null) this.item = super.item();

            return this.item;
        }
    }

    @Configuration
    static class MyConfig {
        @Bean
        Item item() {
            return new Item();
        }

        @Bean
        Bean1 bean1() {
            return new Bean1(item());
        }

        @Bean
        Bean2 bean2() {
            return new Bean2(item());
        }
    }


    static class Bean1 {
        private final Item item;

        Bean1(Item item) {
            this.item = item;
        }
    }

    static class Bean2 {
        private final Item item;

        Bean2(Item item) {
            this.item = item;
        }
    }

    static class Item {
    }

}
