package noodnik.thousandeyes;

import static java.util.Arrays.asList;
import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class PacketBufferTests {

    @Test
    public void submittedSimpleTestCase() {
        Packet packetWritten = new Packet(1, "data".getBytes());
        assertCorrectResults(asList(packetWritten), asList(packetWritten), new SubmittedPacketBuffer());
    }

    @Test
    public void submittedChallengeTestCase1() {
        assertChallenge1(new SubmittedPacketBuffer());
    }

    @Test
    public void ringSimpleTestCase() {
        Packet packetWritten = new Packet(1, "data".getBytes());
        assertCorrectResults(asList(packetWritten), asList(packetWritten), new RingPacketBuffer(1));
    }

    @Test
    public void ringChallengeTestCase1() {
        assertChallenge1(new RingPacketBuffer(2));
    }

    void assertChallenge1(PacketBuffer packetBuffer) {

        //add(1)
        //add(2)
        //read() -> 1
        //read() -> 2
        //add(1)
        //add(3)
        //read() -> 3

        Packet packet1 = new Packet(1, "data1".getBytes());
        Packet packet2 = new Packet(2, "data2".getBytes());
        Packet packet3 = new Packet(3, "data3".getBytes());

        packetBuffer.addToBuffer(packet1);
        packetBuffer.addToBuffer(packet2);
        assertEquals(packet1, packetBuffer.readFromBuffer());
        assertEquals(packet2, packetBuffer.readFromBuffer());
        packetBuffer.addToBuffer(packet1);
        packetBuffer.addToBuffer(packet3);
        assertEquals(packet3, packetBuffer.readFromBuffer());

    }

    void assertCorrectResults(
        List<Packet> expectedPacketsRead,
        List<Packet> packetsWritten,
        PacketBuffer packetBuffer
    ) {
        packetsWritten.stream().forEach(packet -> packetBuffer.addToBuffer(packet));
        List<Packet> actualPacketsRead = new LinkedList<>();
        while(true) {
            Packet packetRead = packetBuffer.readFromBuffer();
            if (packetRead == null) {
                break;
            }
            actualPacketsRead.add(packetRead);
        }
        log("packetWritten(%s), packetRead(%s)", packetsWritten, actualPacketsRead);
        assertEquals(expectedPacketsRead, actualPacketsRead);
    }

    /* Create a packet buffer.
     *
     *            +-----------------+
     *            |   Application   |
     *            +-----------------+
     *                    |
     *                    | readFromBuffer()
     *                    V
     *            +-----------------+
     *            |  Packet Buffer  |
     *            +-----------------+
     *                    ^
     *                    | addToBuffer()
     *                    |
     *            +-----------------+
     *            |Network Interface|
     *            +-----------------+
     * 
     * - Think of this as a middle layer between something that is consuming the
     *   packets off of the wire, and some application that will read the data.
     * 
     * - There is a single producer (the network) and a single consumer (the
     *   application)
     *
     * - The buffer will receive a single stream of packets from the lower layer,
     *   one packet at a time. These packets can arrive out of order and there may be
     *   duplicates.
     *
     * - The upper layer will request one packet at a time. You must output packets
     *   to the upper layer in order, starting at seqNum 1, with no duplicates or gaps.
     */

    static class Packet implements Comparable<Packet> {
        
        int seqNum;
        byte[] data;

        public Packet(int seqNum, byte[] data) {
            this.seqNum = seqNum;
            this.data = data;
        }

        public String toString() {
            return String.format("[%d]=%s", seqNum, new String(data));
        }

        public int compareTo(Packet p) {
            if (p == null) {
                return -1;
            }
            return (
                seqNum < p.seqNum  ? 1 
               : seqNum == p.seqNum ? 0
               : -1
            );
        }

    }

    interface PacketBuffer {

        /** Called by lower layer. Packets may be out of order or duplicated */
        void addToBuffer(Packet p);
        
        /** Called by upper layer. Packets must be emitted sequentially */        
        Packet readFromBuffer();

    }

    static class RingPacketBuffer implements PacketBuffer {

        final Packet[] ringBuffer;
        int writeIndex = 0;
        int readIndex = 0;
        int lastSeqRead = 0;

        RingPacketBuffer(int bufferSize) {
            ringBuffer = new Packet[bufferSize];
        }

        public void addToBuffer(Packet p) {
            if (p.seqNum < lastSeqRead) {
                log("ignored duplicate packet (already consumed)");
                return;
            }
            writeIndex = wrapIndex(writeIndex);            
            ringBuffer[writeIndex++] = p;
        }

        public Packet readFromBuffer() {

            if (readIndex == writeIndex) {
                log("buffer is empty");
                return null;
            }

            // if head is not desired sequence,
            //   if can't find index of desired sequence,
            //     return can't find
            //   else swap head with desired
            readIndex = wrapIndex(readIndex);
            if (ringBuffer[readIndex].seqNum != lastSeqRead + 1) {
                if (!canSwap()) {
                    log("next packet not present");
                    return null;
                }
            }

            Packet packetRead = ringBuffer[readIndex++];
            lastSeqRead = packetRead.seqNum;
            return packetRead;
        }

        /**
         *  @return true iff found desired packet and swapped it into next read position
         */
        boolean canSwap() {

            if (ringBuffer.length < 2) {
                return false;
            }

            int tryIndex = wrapIndex(readIndex) + 1;
            while(tryIndex != wrapIndex(readIndex)) {
                if (ringBuffer[tryIndex].seqNum == lastSeqRead + 1) {
                    Packet p = ringBuffer[tryIndex];
                    ringBuffer[tryIndex] = ringBuffer[readIndex];
                    ringBuffer[readIndex] = p;
                    return true;
                }
                tryIndex = wrapIndex(tryIndex + 1);
            }

            return false;
        }

        int wrapIndex(int ringIndex) {
            if (ringIndex >= ringBuffer.length) {
                return 0;
            }
            return ringIndex;
        }

    }

    static class SubmittedPacketBuffer implements PacketBuffer {

        Map<Integer, Packet> queue = new HashMap<>();
        int lastReturnedSeq = 0;

        public void addToBuffer(Packet p) {
            if (p == null) {
                // warning: null packet ignored
                return;
            }
            if (p.seqNum < lastReturnedSeq) {
                // ignored since already consumed
                return;
            }
            if (queue.get(p.seqNum) != null) {
                // warning: duplicate packet ignored
                return;
            }
            queue.put(p.seqNum, p);
        }

        public Packet readFromBuffer() {
            Packet nextPacket = queue.get(lastReturnedSeq + 1);
            if (nextPacket == null) {
                log("buffer is empty");
                return null;
            }
            lastReturnedSeq = nextPacket.seqNum;
            queue.remove(nextPacket.seqNum);
            return nextPacket;
        }

    }

}
