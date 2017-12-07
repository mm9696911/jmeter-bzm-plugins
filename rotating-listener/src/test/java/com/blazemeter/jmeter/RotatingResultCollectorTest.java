package com.blazemeter.jmeter;

import kg.apc.emulators.TestJMeterUtils;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;


public class RotatingResultCollectorTest {
    @BeforeClass
    public static void setUpClass()
            throws Exception {
        TestJMeterUtils.createJmeterEnv();
    }

    @Test
    public void rotatingRules() throws Exception {
        assertEquals("/tmp/abc.1", RotatingResultCollector.getRotatedFilename("/tmp/abc", "/tmp/abc"));
        assertEquals("/tmp/abc.3.5.jtl", RotatingResultCollector.getRotatedFilename("/tmp/abc.3.4.jtl", "/tmp/abc.3.4.jtl"));
        assertEquals("/tmp/abc.2.loglog", RotatingResultCollector.getRotatedFilename("/tmp/abc.1.loglog", "/tmp/abc.1.loglog"));
        assertEquals("/tmp/abc.1.1", RotatingResultCollector.getRotatedFilename("/tmp/abc.1", "/tmp/abc.1"));
        assertEquals("/tmp/abc.1.jtl", RotatingResultCollector.getRotatedFilename("/tmp/abc.jtl", "/tmp/abc.jtl"));
        assertEquals("/tmp/abc.2.log", RotatingResultCollector.getRotatedFilename("/tmp/abc.1.log", "/tmp/abc.1.log"));
        assertEquals("/tmp/abc.1.jtl", RotatingResultCollector.getRotatedFilename("/tmp/abc.0.jtl", "/tmp/abc.0.jtl"));
        assertEquals("/tmp/abc.5.jtl", RotatingResultCollector.getRotatedFilename("/tmp/abc.4.jtl", "/tmp/abc.4.jtl"));
        assertEquals("/tmp/abc.bbb.1.jtl", RotatingResultCollector.getRotatedFilename("/tmp/abc.bbb.jtl", "/tmp/abc.bbb.jtl"));
        // Fantastic case
        assertEquals("/tmp/abc.x.1", RotatingResultCollector.getRotatedFilename("/tmp/abc.x", "/tmp/abc"));
    }

    @Test
    public void flowDefault() throws Exception {
        RotatingResultCollector te = new RotatingResultCollector();
        te.setMaxSamplesCount("ZXC");
        assertEquals(Integer.MAX_VALUE, te.getMaxSamplesCountAsInt());

        File f = File.createTempFile("rotating", ".jtl");
        f.deleteOnExit();
        te.setFilename(f.getAbsolutePath());
        te.setMaxSamplesCount("3");
        te.testStarted();

        for (int n = 0; n < 10; n++) {
            SampleResult result = new SampleResult();
            result.setSampleLabel("#" + n);
            te.sampleOccurred(new SampleEvent(result, ""));
        }

        te.testEnded();
        assertTrue(te.filename.endsWith(".3.jtl"));
    }

    @Test
    public void testFlowWithoutExtension() throws Exception {
        RotatingResultCollector te = new RotatingResultCollector();
        te.setMaxSamplesCount("ZXC");
        assertEquals(Integer.MAX_VALUE, te.getMaxSamplesCountAsInt());

        File f = File.createTempFile("rotating", "");
        f.deleteOnExit();
        te.setFilename(f.getAbsolutePath());
        te.setMaxSamplesCount("3");
        te.testStarted();

        for (int n = 0; n < 10; n++) {
            SampleResult result = new SampleResult();
            result.setSampleLabel("#" + n);
            te.sampleOccurred(new SampleEvent(result, ""));
        }

        te.testEnded();
        assertTrue(te.filename, te.filename.endsWith(".3"));
    }

    @Test
    public void testFlowWithNumberExtension() throws Exception {
        RotatingResultCollector te = new RotatingResultCollector();
        te.setMaxSamplesCount("ZXC");
        assertEquals(Integer.MAX_VALUE, te.getMaxSamplesCountAsInt());

        File f = File.createTempFile("rotating", ".9");
        f.deleteOnExit();
        te.setFilename(f.getAbsolutePath());
        te.setMaxSamplesCount("3");
        te.testStarted();

        for (int n = 0; n < 10; n++) {
            SampleResult result = new SampleResult();
            result.setSampleLabel("#" + n);
            te.sampleOccurred(new SampleEvent(result, ""));
        }

        te.testEnded();
        assertTrue(te.filename, te.filename.endsWith(".3.9"));
    }
}