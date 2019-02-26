package com.amairovi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoreTest {

    private Core core;

    @BeforeEach
    void setup(){
        core = new Core();
    }

    @Test
    @Disabled
    void works() throws InterruptedException {
//        core.createFeed("https://fletcherpenney.net/atom.xml", 5000);
        core.createFeed("http://rss.nytimes.com/services/xml/rss/nyt/World.xml", 5000);
        Thread.sleep(4500);

        core.hideProperty(1,"categories");

        Thread.sleep(4500);

//        core.hideProperty(1,"categories");

//        Thread.sleep(4500);

        core.showProperty(1,"categories");

        Thread.sleep(4500);
        System.out.println("works");
    }

}