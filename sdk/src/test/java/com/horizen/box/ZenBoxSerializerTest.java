package com.horizen.box;

import com.horizen.fixtures.BoxFixtureClass;
import com.horizen.proposition.PublicKey25519Proposition;
import com.horizen.utils.BytesUtils;
import com.horizen.utils.Ed25519;
import com.horizen.utils.Pair;
import org.junit.Before;
import org.junit.Test;
import scala.util.Try;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ZenBoxSerializerTest extends BoxFixtureClass
{
    ZenBox box;

    @Before
    public void setUp() {
        Pair<byte[], byte[]> keyPair = Ed25519.createKeyPair("12345".getBytes());
        // Note: current box bytes are also stored in "src/test/resources/zenbox_hex"
        box = getZenBox(new PublicKey25519Proposition(keyPair.getValue()), 1000, 10);

//     Uncomment and run if you want to update regression data.
//        try {
//            BufferedWriter out = new BufferedWriter(new FileWriter("src/test/resources/zenbox_hex"));
//            out.write(BytesUtils.toHexString(box.bytes()));
//            out.close();
//        } catch (Throwable e) {
//        }
    }

    @Test
    public void serializationTest() {
        BoxSerializer<ZenBox> serializer = box.serializer();
        byte[] bytes = serializer.toBytes(box);

        ZenBox box2 = serializer.parseBytesTry(bytes).get();
        assertEquals("Boxes expected to be equal", box, box2);


        boolean failureExpected = serializer.parseBytesTry("broken bytes".getBytes()).isFailure();
        assertTrue("Failure during parsing expected", failureExpected);

    }

    @Test
    public void regressionTest() {
        byte[] bytes;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            FileReader file = new FileReader(classLoader.getResource("zenbox_hex").getFile());
            bytes = BytesUtils.fromHexString(new BufferedReader(file).readLine());
        }
        catch (Exception e) {
            fail(e.toString());
            return;
        }

        BoxSerializer<ZenBox> serializer = box.serializer();
        Try<ZenBox> t = serializer.parseBytesTry(bytes);
        assertTrue("Box serialization failed.", t.isSuccess());

        ZenBox parsedBox = t.get();
        assertTrue("Box is different to origin.", Arrays.equals(box.id(), parsedBox.id()));
        assertTrue("Box is different to origin.", Arrays.equals(box.bytes(), parsedBox.bytes()));
    }
}