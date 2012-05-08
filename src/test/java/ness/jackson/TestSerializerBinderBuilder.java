package ness.jackson;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.nesscomputing.config.Config;
import com.nesscomputing.config.ConfigModule;

public class TestSerializerBinderBuilder {

    @Inject
    @Json
    Function<Integer, String> serializer;

    @Inject
    @Json
    Function<String, Integer> deserializer;

    @Inject
    @Smile
    Function<Integer, byte[]> serializerBytes;

    @Inject
    @Smile
    Function<byte[], Integer> deserializerBytes;

    @Test
    public void testBasicSerialization() {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install (new ConfigModule(Config.getEmptyConfig()));
                install (new NessJacksonModule());

                JacksonSerializerBinder.bindSerializer(binder(), Integer.class)
                    .build();
            }
        }).injectMembers(this);

        String serialized = serializer.apply(3);

        assertEquals("3", serialized);

        assertEquals(3, (int) deserializer.apply(serialized));
    }

    @Test
    public void testSmileSerialization() {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                install (new ConfigModule(Config.getEmptyConfig()));
                install (new NessJacksonModule());

                JacksonSerializerBinder.bindSerializer(binder(), Integer.class)
                    .build();
            }
        }).injectMembers(this);

        byte[] serialized = serializerBytes.apply(3);

        assertArrayEquals(new byte[] {58, 41, 10, 1, -58}, serialized);

        assertEquals(3, (int) deserializerBytes.apply(serialized));
    }
}