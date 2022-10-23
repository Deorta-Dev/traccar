package org.traccar.protocol;

import org.junit.Test;
import org.traccar.ProtocolTest;

public class AdmFrameDecoderTest extends ProtocolTest {

    @Test
    public void testDecode() throws Exception {

        var decoder = inject(new AdmFrameDecoder());

        verifyFrame(
                binary("38363931353330343235323337383400003728e000001402441d5f42c3711642930d000000c7000a461954f25fd82ed508000000000000000044000000010000000000140000"),
                decoder.decode(null, null, binary("38363931353330343235323337383400003728e000001402441d5f42c3711642930d000000c7000a461954f25fd82ed508000000000000000044000000010000000000140000")));

        verifyFrame(
                binary("000042033836393135333034323532333738340000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000078"),
                decoder.decode(null, null, binary("000042033836393135333034323532333738340000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000078")));

        verifyFrame(
                binary("010022003300072020000000000000000044062A330000000000107F10565D4A8310"),
                decoder.decode(null, null, binary("010022003300072020000000000000000044062A330000000000107F10565D4A8310")));

    }

}
