package syh.year2021

import syh.library.AbstractAocDay

class Puzzle16 : AbstractAocDay(2021, 16) {
    override fun doA(file: String): String {
        val hexadecimalString = readSingleLineFile(file)[0]

        val binary = hexadecimalString.map { hexToBinary(it) }.joinToString("")
        println(hexadecimalString)
        println(binary)

        val (packet, _) = readPacket(binary)

        return packet.calculateVersion().toString()
    }

    override fun doB(file: String): String {
        val hexadecimalString = readSingleLineFile(file)[0]

        val binary = hexadecimalString.map { hexToBinary(it) }.joinToString("")
        println(hexadecimalString)
        println(binary)

        val (packet, _) = readPacket(binary)

        return packet.calculateValue().toString()
    }

    private fun readPacket(totalBinary: String): Pair<Packet, String> {
        println("reading $totalBinary")
        val packetVersion = totalBinary.slice(0..2).toInt(2)
        val packetType = totalBinary.slice(3..5).toInt(2)

        // if packetType == 4 then it's a packet with a value
        if (packetType == 4) {
            var leftover = totalBinary.substring(6)
            var literal = ""
            while (leftover[0] == '1') {
                literal += leftover.take(5).drop(1)
                leftover = leftover.drop(5)
            }
            // do the last literal
            literal += leftover.take(5).drop(1)
            leftover = leftover.drop(5)

            println("Read literal with version $packetVersion type $packetType literal ${literal.toLong(2)} leftover $leftover")
            return LiteralPacket(packetVersion, packetType, literal.toLong(2)) to leftover
        }

        // otherwise contains a packet with subpacket(s)
        val lengthType = if (totalBinary[6] == '0') 15 else 11
        if (lengthType == 15) {
            println("read container packet with version $packetVersion type $packetType")
            val subpacketLength = totalBinary.slice(7..21).toInt(2)
            val subpacketString = totalBinary.drop(22)
            var offset = 0
            val subpackets = mutableListOf<Packet>()

            while (offset < subpacketLength) {
                val (subpacket, leftover) = readPacket(subpacketString.drop(offset))
                subpackets.add(subpacket)
                offset = subpacketString.length - leftover.length
//                println("read subpacket ${subpacket.version}, with offset $offset and leftoverString $leftover")
            }

            return ContainingPacket(packetVersion, packetType, subpackets) to subpacketString.drop(offset)
        } else {
            val repetitions = totalBinary.slice(7..17).toInt(2)
            val subpackets = mutableListOf<Packet>()
            var leftoverString = totalBinary.substring(18)
//            println("read container packet with version $packetVersion type $packetType repetitions $repetitions")

            repeat(repetitions) {
                val (subpacket, leftover) = readPacket(leftoverString)
                subpackets.add(subpacket)
                leftoverString = leftover
            }
            return ContainingPacket(packetVersion, packetType, subpackets) to leftoverString
        }
    }

    private fun hexToBinary(str: Char): String {
        return when (str) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> throw IllegalArgumentException("$str is not a valid hexadecimal character")
        }
    }

    interface Packet {
        val version: Int
        val type: Int
        fun calculateVersion(): Int
        fun calculateValue(): Long
    }

    data class LiteralPacket(override val version: Int, override val type: Int, val literal: Long) : Packet {
        override fun calculateVersion(): Int {
            return version
        }

        override fun calculateValue(): Long {
            return literal
        }
    }

    data class ContainingPacket(override val version: Int, override val type: Int, val subPackets: List<Packet>) : Packet {
        override fun calculateVersion(): Int {
            return version + subPackets.sumOf { it.calculateVersion() }
        }

        override fun calculateValue(): Long {
            return when (type) {
                0 -> subPackets.sumOf { it.calculateValue() }
                1 -> subPackets.fold(1) { acc, new -> acc * new.calculateValue() }
                2 -> subPackets.minOf { it.calculateValue() }
                3 -> subPackets.maxOf { it.calculateValue() }
                5 -> if (subPackets[0].calculateValue() > subPackets[1].calculateValue()) 1 else 0
                6 -> if (subPackets[0].calculateValue() < subPackets[1].calculateValue()) 1 else 0
                7 -> if (subPackets[0].calculateValue() == subPackets[1].calculateValue()) 1 else 0
                else -> throw IllegalStateException("Container packet should not have type $type")
            }
        }
    }

}