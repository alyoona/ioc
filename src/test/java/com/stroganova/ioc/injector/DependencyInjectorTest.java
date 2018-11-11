package com.stroganova.ioc.injector;

import com.stroganova.ioc.entity.Bean;
import com.stroganova.ioc.exception.BeanInitializationException;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class DependencyInjectorTest {


    @Test
    public void testInject() {
        class TheClassForTest {
            private byte byteField;
            private short shortField;
            private int intField;
            private long longField;
            private float floatField;
            private double doubleField;
            private char charField;
            private boolean boolField;
            private String stringField;

            public void setByteField(byte byteField) {
                this.byteField = byteField;
            }

            public void setShortField(short shortField) {
                this.shortField = shortField;
            }

            public void setIntField(int intField) {
                this.intField = intField;
            }

            public void setLongField(long longField) {
                this.longField = longField;
            }

            public void setFloatField(float floatField) {
                this.floatField = floatField;
            }

            public void setDoubleField(double doubleField) {
                this.doubleField = doubleField;
            }

            public void setCharField(char charField) {
                this.charField = charField;
            }

            public void setBoolField(boolean boolField) {
                this.boolField = boolField;
            }

            public void setStringField(String stringField) {
                this.stringField = stringField;
            }
        }

        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("byteField", "1");
        dependencies.put("shortField", "1");
        dependencies.put("intField", "1");
        dependencies.put("longField", "1");
        dependencies.put("floatField", "1");
        dependencies.put("doubleField", "1");
        dependencies.put("charField", "!");
        dependencies.put("boolField", "false");
        dependencies.put("stringField", "some string");

        TheClassForTest theClassForTest = new TheClassForTest();

        Bean bean = new Bean();
        bean.setValue(theClassForTest);

        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.inject(bean, dependencies);

        byte expectedByteValue = 1;
        assertEquals(expectedByteValue, theClassForTest.byteField);
        short expectedShortValue = 1;
        assertEquals(expectedShortValue, theClassForTest.shortField);
        int expectedIntValue = 1;
        assertEquals(expectedIntValue, theClassForTest.intField);
        assertEquals(1L, theClassForTest.longField);
        assertEquals(1F, theClassForTest.floatField, 1);
        assertEquals(1D, theClassForTest.doubleField, 1);
        assertEquals('!', theClassForTest.charField);
        assertEquals(false, theClassForTest.boolField);
        assertEquals("some string", theClassForTest.stringField);
    }

    @Test(expected = BeanInitializationException.class)
    public void testNoSetterInject() {
        class First {
            private byte byteField;
        }

        Map<String, String> dependencies = new HashMap<>();
        dependencies.put("byteField", "1");

        First first = new First();

        Bean bean = new Bean();
        bean.setValue(first);

        DependencyInjector dependencyInjector = new DependencyInjector();
        dependencyInjector.inject(bean, dependencies);
    }

}